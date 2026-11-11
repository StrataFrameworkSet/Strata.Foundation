//////////////////////////////////////////////////////////////////////////////
// CountedCloseableTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@Tag("CommitStage")
public
class CountedCloseableTest
{
    private AtomicInteger                     closeCount;
    private AutoCloseable                     resource;
    private CountedCloseable<AutoCloseable>   subject;

    @BeforeEach
    public void
    setUp()
    {
        closeCount = new AtomicInteger(0);
        resource = closeCount::incrementAndGet;
        subject = new CountedCloseable<>(resource);
    }

    @Test
    public void
    testGetReturnsWrappedCloseable()
    {
        assertSame(resource,subject.get());
    }

    @Test
    public void
    testConstructorRejectsNull()
    {
        assertThrows(NullPointerException.class,() -> new CountedCloseable<AutoCloseable>((AutoCloseable)null));
    }

    @Test
    public void
    testOfCountedRejectsNull()
    {
        assertThrows(NullPointerException.class,() -> CountedCloseable.ofCounted(null));
    }

    @Test
    public void
    testCloseSingleReferenceClosesResource() throws Exception
    {
        subject.close();

        assertEquals(1,closeCount.get());
    }

    @Test
    public void
    testCopyReturnsDistinctCountedCloseable()
    {
        ICopyable copy = subject.copy();

        assertInstanceOf(CountedCloseable.class,copy);
        assertNotSame(subject,copy);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void
    testCopySharesResource()
    {
        CountedCloseable<AutoCloseable> copy =
            (CountedCloseable<AutoCloseable>)subject.copy();

        assertSame(resource,copy.get());
    }

    @Test
    public void
    testCopyConstructorSharesResource()
    {
        CountedCloseable<AutoCloseable> copy = new CountedCloseable<>(subject);

        assertSame(resource,copy.get());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void
    testResourceNotClosedUntilAllCopiesClosed() throws Exception
    {
        CountedCloseable<AutoCloseable> copy1 =
            (CountedCloseable<AutoCloseable>)subject.copy();
        CountedCloseable<AutoCloseable> copy2 = new CountedCloseable<>(copy1);

        subject.close();
        assertEquals(0,closeCount.get());

        copy2.close();
        assertEquals(0,closeCount.get());

        copy1.close();
        assertEquals(1,closeCount.get());
    }

    @Test
    public void
    testCloseOrderDoesNotMatter() throws Exception
    {
        CountedCloseable<AutoCloseable> copy = new CountedCloseable<>(subject);

        copy.close();
        assertEquals(0,closeCount.get());

        subject.close();
        assertEquals(1,closeCount.get());
    }

    @Test
    public void
    testCopyOfCountedCopySharesSameCount() throws Exception
    {
        CountedCloseable<AutoCloseable> copy1 = new CountedCloseable<>(subject);
        CountedCloseable<AutoCloseable> copy2 = new CountedCloseable<>(copy1);
        CountedCloseable<AutoCloseable> copy3 = new CountedCloseable<>(subject);

        for (CountedCloseable<AutoCloseable> c : List.of(copy3,copy1,subject))
        {
            c.close();
            assertEquals(0,closeCount.get());
        }

        copy2.close();
        assertEquals(1,closeCount.get());
    }

    @Test
    public void
    testExtraClosesDoNotCloseResourceAgain() throws Exception
    {
        subject.close();
        subject.close();
        subject.close();

        assertEquals(1,closeCount.get());
    }

    @Test
    public void
    testRepeatedCloseOfCountedOneHandleDoesNotReleaseOtherReferences() throws Exception
    {
        CountedCloseable<AutoCloseable> copy = new CountedCloseable<>(subject);

        subject.close();
        subject.close();
        subject.close();
        assertEquals(0,closeCount.get());

        copy.close();
        assertEquals(1,closeCount.get());
    }

    @Test
    public void
    testRepeatedCloseAfterResourceClosedIsNoOp() throws Exception
    {
        CountedCloseable<AutoCloseable> copy = new CountedCloseable<>(subject);

        subject.close();
        copy.close();
        subject.close();
        copy.close();

        assertEquals(1,closeCount.get());
    }

    @Test
    public void
    testRepeatedCloseAfterResourceExceptionDoesNotRethrow()
    {
        AtomicInteger                   attempts = new AtomicInteger(0);
        CountedCloseable<AutoCloseable> failing  =
            new CountedCloseable<>(
                () ->
                {
                    attempts.incrementAndGet();
                    throw new Exception("failure");
                });

        assertThrows(Exception.class,failing::close);
        assertDoesNotThrow(failing::close);
        assertEquals(1,attempts.get());
    }

    @Test
    public void
    testConcurrentRepeatedCloseOfCountedSameHandleReleasesOneReference() throws Exception
    {
        int                             threads = 16;
        ExecutorService                 executor = Executors.newFixedThreadPool(threads);
        CountDownLatch                  start = new CountDownLatch(1);
        List<Future<?>>                 futures = new ArrayList<>();
        CountedCloseable<AutoCloseable> copy = new CountedCloseable<>(subject);

        try
        {
            for (int i = 0; i < threads; i++)
            {
                futures.add(
                    executor.submit(
                        () ->
                        {
                            start.await();
                            subject.close();
                            return null;
                        }));
            }

            start.countDown();

            for (Future<?> future : futures)
                future.get();
        }
        finally
        {
            executor.shutdownNow();
        }

        assertEquals(0,closeCount.get());

        copy.close();

        assertEquals(1,closeCount.get());
    }

    @Test
    public void
    testCloseRethrowsResourceException()
    {
        Exception                       expected = new Exception("failure");
        CountedCloseable<AutoCloseable> failing  =
            new CountedCloseable<>(() -> { throw expected; });

        Exception actual = assertThrows(Exception.class,failing::close);

        assertSame(expected,actual);
    }

    @Test
    public void
    testCloseDoesNotThrowWhileReferencesRemain() throws Exception
    {
        CountedCloseable<AutoCloseable> failing =
            new CountedCloseable<>(() -> { throw new Exception("failure"); });
        CountedCloseable<AutoCloseable> copy = new CountedCloseable<>(failing);

        failing.close();

        assertThrows(Exception.class,copy::close);
    }

    @Test
    public void
    testTryWithResourcesReleasesReference() throws Exception
    {
        CountedCloseable<AutoCloseable> copy = new CountedCloseable<>(subject);

        try (CountedCloseable<AutoCloseable> c = copy)
        {
            assertSame(resource,c.get());
        }

        assertEquals(0,closeCount.get());

        try (CountedCloseable<AutoCloseable> c = subject)
        {
            assertSame(resource,c.get());
        }

        assertEquals(1,closeCount.get());
    }

    @Test
    public void
    testConcurrentCopyAndCloseClosesResourceExactlyOnce() throws Exception
    {
        int                                   threads = 16;
        int                                   perThread = 1000;
        ExecutorService                       executor = Executors.newFixedThreadPool(threads);
        CountDownLatch                        start = new CountDownLatch(1);
        List<Future<?>>                       futures = new ArrayList<>();

        try
        {
            for (int i = 0; i < threads; i++)
            {
                futures.add(
                    executor.submit(
                        () ->
                        {
                            start.await();

                            for (int j = 0; j < perThread; j++)
                                new CountedCloseable<>(subject).close();

                            return null;
                        }));
            }

            start.countDown();

            for (Future<?> future : futures)
                future.get();
        }
        finally
        {
            executor.shutdownNow();
        }

        assertEquals(0,closeCount.get());

        subject.close();

        assertEquals(1,closeCount.get());
    }

    @Test
    public void
    testConcurrentCloseOfCountedCopiesClosesResourceExactlyOnce() throws Exception
    {
        int                                   copies = 1000;
        ExecutorService                       executor = Executors.newFixedThreadPool(16);
        CountDownLatch                        start = new CountDownLatch(1);
        List<CountedCloseable<AutoCloseable>> refs = new ArrayList<>();
        List<Future<?>>                       futures = new ArrayList<>();

        refs.add(subject);

        for (int i = 1; i < copies; i++)
            refs.add(new CountedCloseable<>(subject));

        try
        {
            for (CountedCloseable<AutoCloseable> ref : refs)
            {
                futures.add(
                    executor.submit(
                        () ->
                        {
                            start.await();
                            ref.close();
                            return null;
                        }));
            }

            start.countDown();

            for (Future<?> future : futures)
                future.get();
        }
        finally
        {
            executor.shutdownNow();
        }

        assertEquals(1,closeCount.get());
    }

    @Test
    public void
    testOfUncountedRejectsNull()
    {
        assertThrows(NullPointerException.class,() -> CountedCloseable.ofUncounted(null));
    }

    @Test
    public void
    testIsCountedForCountedInstanceAndCopy()
    {
        assertTrue(subject.isCounted());
        assertTrue(subject.copy().isCounted());
    }

    @Test
    public void
    testIsCountedForUncountedInstanceAndCopy()
    {
        CountedCloseable<AutoCloseable> uncounted = CountedCloseable.ofUncounted(resource);

        assertFalse(uncounted.isCounted());
        assertTrue(uncounted.copy().isCounted());
    }

    @Test
    public void
    testIsReferenceClosedReflectsInstanceNotResource() throws Exception
    {
        CountedCloseable<AutoCloseable> copy = subject.copy();

        assertFalse(subject.isReferenceClosed());

        subject.close();

        assertTrue(subject.isReferenceClosed());
        assertFalse(copy.isReferenceClosed());
        assertEquals(0,closeCount.get());
    }

    @Test
    public void
    testIsReferenceClosedWhenResourceCloseThrows()
    {
        CountedCloseable<AutoCloseable> failing =
            CountedCloseable.ofCounted(() -> { throw new Exception("failure"); });

        assertThrows(Exception.class,failing::close);
        assertTrue(failing.isReferenceClosed());
    }

    @Test
    public void
    testIsResourceClosedForCountedInstanceAndCopy() throws Exception
    {
        CountedCloseable<AutoCloseable> copy = subject.copy();

        assertFalse(subject.isResourceClosed());
        assertFalse(copy.isResourceClosed());

        subject.close();

        assertFalse(subject.isResourceClosed());
        assertFalse(copy.isResourceClosed());

        copy.close();

        assertTrue(subject.isResourceClosed());
        assertTrue(copy.isResourceClosed());
        assertEquals(1,closeCount.get());
    }

    @Test
    public void
    testIsResourceClosedForUncountedWithoutCopies() throws Exception
    {
        CountedCloseable<AutoCloseable> uncounted = CountedCloseable.ofUncounted(resource);

        assertFalse(uncounted.isResourceClosed());

        uncounted.close();

        assertTrue(uncounted.isResourceClosed());
        assertEquals(1,closeCount.get());
    }

    @Test
    public void
    testIsResourceClosedForUncountedAfterLastCopyClosed() throws Exception
    {
        CountedCloseable<AutoCloseable> uncounted = CountedCloseable.ofUncounted(resource);
        CountedCloseable<AutoCloseable> copy = uncounted.copy();

        copy.close();

        assertFalse(uncounted.isReferenceClosed());
        assertTrue(uncounted.isResourceClosed());
        assertTrue(copy.isResourceClosed());
        assertEquals(1,closeCount.get());
    }

    @Test
    public void
    testIsResourceClosedWhileUncountedCopiesRemainOpen() throws Exception
    {
        CountedCloseable<AutoCloseable> uncounted = CountedCloseable.ofUncounted(resource);
        CountedCloseable<AutoCloseable> copy = uncounted.copy();

        uncounted.close();

        assertTrue(uncounted.isReferenceClosed());
        assertFalse(uncounted.isResourceClosed());
        assertFalse(copy.isResourceClosed());

        copy.close();

        assertTrue(uncounted.isResourceClosed());
    }

    @Test
    public void
    testIsResourceClosedWhenResourceCloseThrows()
    {
        CountedCloseable<AutoCloseable> failing =
            CountedCloseable.ofCounted(() -> { throw new Exception("failure"); });

        assertThrows(Exception.class,failing::close);
        assertTrue(failing.isResourceClosed());
    }

    @Test
    public void
    testCopyOfClosedInstanceThrowsWhileResourceOpen() throws Exception
    {
        CountedCloseable<AutoCloseable> copy = subject.copy();

        subject.close();

        assertThrows(IllegalStateException.class,subject::copy);
        assertThrows(IllegalStateException.class,() -> new CountedCloseable<>(subject));
        assertThrows(IllegalStateException.class,() -> CountedCloseable.ofCopy(subject));
        assertDoesNotThrow(() -> copy.copy().close());
        assertEquals(0,closeCount.get());
    }

    @Test
    public void
    testCopyOfCountedAfterResourceClosedThrows() throws Exception
    {
        subject.close();

        assertThrows(IllegalStateException.class,subject::copy);
        assertEquals(1,closeCount.get());
    }

    @Test
    public void
    testCloseUncountedWithoutCopiesClosesResource() throws Exception
    {
        CountedCloseable<AutoCloseable> uncounted = CountedCloseable.ofUncounted(resource);

        uncounted.close();

        assertEquals(1,closeCount.get());
    }

    @Test
    public void
    testCloseUncountedAfterCopiesClosedDoesNotCloseResourceAgain() throws Exception
    {
        CountedCloseable<AutoCloseable> uncounted = CountedCloseable.ofUncounted(resource);
        CountedCloseable<AutoCloseable> copy = uncounted.copy();

        copy.close();
        assertEquals(1,closeCount.get());

        uncounted.close();
        assertEquals(1,closeCount.get());
    }

    @Test
    public void
    testCloseUncountedWithOpenCopiesDefersToLastCopy() throws Exception
    {
        CountedCloseable<AutoCloseable> uncounted = CountedCloseable.ofUncounted(resource);
        CountedCloseable<AutoCloseable> copy1 = uncounted.copy();
        CountedCloseable<AutoCloseable> copy2 = uncounted.copy();

        uncounted.close();
        assertEquals(0,closeCount.get());

        copy1.close();
        assertEquals(0,closeCount.get());

        copy2.close();
        assertEquals(1,closeCount.get());
    }

    @Test
    public void
    testCopyOfUncountedAfterUncountedClosedThrows() throws Exception
    {
        CountedCloseable<AutoCloseable> uncounted = CountedCloseable.ofUncounted(resource);

        uncounted.close();

        assertThrows(IllegalStateException.class,uncounted::copy);
        assertEquals(1,closeCount.get());
    }

    @Test
    public void
    testCopyOfUncountedAfterResourceClosedThrows() throws Exception
    {
        CountedCloseable<AutoCloseable> uncounted = CountedCloseable.ofUncounted(resource);

        uncounted.copy().close();
        assertEquals(1,closeCount.get());

        assertThrows(IllegalStateException.class,uncounted::copy);
        assertThrows(IllegalStateException.class,() -> new CountedCloseable<>(uncounted));
        assertThrows(IllegalStateException.class,() -> CountedCloseable.ofCopy(uncounted));

        uncounted.close();
        assertEquals(1,closeCount.get());
    }

    @Test
    public void
    testCopyOfUncountedCopySharesSameCount() throws Exception
    {
        CountedCloseable<AutoCloseable> uncounted = CountedCloseable.ofUncounted(resource);
        CountedCloseable<AutoCloseable> copy1 = uncounted.copy();
        CountedCloseable<AutoCloseable> copy2 = copy1.copy();

        copy1.close();
        uncounted.close();
        assertEquals(0,closeCount.get());

        copy2.close();
        assertEquals(1,closeCount.get());
    }

    @Test
    public void
    testRepeatedCloseOfUncountedIsNoOp() throws Exception
    {
        CountedCloseable<AutoCloseable> uncounted = CountedCloseable.ofUncounted(resource);
        CountedCloseable<AutoCloseable> copy = uncounted.copy();

        uncounted.close();
        uncounted.close();
        uncounted.close();
        assertEquals(0,closeCount.get());

        copy.close();
        uncounted.close();
        assertEquals(1,closeCount.get());
    }

    @Test
    public void
    testConcurrentCloseOfUncountedAndLastCopyClosesResourceExactlyOnce() throws Exception
    {
        int             iterations = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(2);

        try
        {
            for (int i = 0; i < iterations; i++)
            {
                AtomicInteger                   counter = new AtomicInteger(0);
                CountedCloseable<AutoCloseable> uncounted = CountedCloseable.ofUncounted(counter::incrementAndGet);
                CountedCloseable<AutoCloseable> copy = uncounted.copy();

                runConcurrently(
                    executor,
                    List.of(
                        () -> { copy.close(); return null; },
                        () -> { uncounted.close(); return null; }));

                assertEquals(1,counter.get());
            }
        }
        finally
        {
            executor.shutdownNow();
        }
    }

    @Test
    public void
    testConcurrentCopyAndCloseOfUncountedClosesResourceExactlyOnce() throws Exception
    {
        int             iterations = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(2);

        try
        {
            for (int i = 0; i < iterations; i++)
            {
                AtomicInteger                   counter = new AtomicInteger(0);
                CountedCloseable<AutoCloseable> uncounted = CountedCloseable.ofUncounted(counter::incrementAndGet);

                runConcurrently(
                    executor,
                    List.of(
                        () -> { copyAndClose(uncounted); return null; },
                        () -> { uncounted.close(); return null; }));

                assertEquals(1,counter.get());
            }
        }
        finally
        {
            executor.shutdownNow();
        }
    }

    @Test
    public void
    testConcurrentCopyAndCloseOfCountedClosesResourceExactlyOnce() throws Exception
    {
        int             iterations = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(2);

        try
        {
            for (int i = 0; i < iterations; i++)
            {
                AtomicInteger                   counter = new AtomicInteger(0);
                CountedCloseable<AutoCloseable> counted = CountedCloseable.ofCounted(counter::incrementAndGet);

                runConcurrently(
                    executor,
                    List.of(
                        () -> { copyAndClose(counted); return null; },
                        () -> { counted.close(); return null; }));

                assertEquals(1,counter.get());
            }
        }
        finally
        {
            executor.shutdownNow();
        }
    }

    @Test
    public void
    testConcurrentCopyAndCloseFromUncountedClosesResourceExactlyOnce() throws Exception
    {
        int                             threads = 16;
        int                             perThread = 1000;
        ExecutorService                 executor = Executors.newFixedThreadPool(threads);
        CountedCloseable<AutoCloseable> uncounted = CountedCloseable.ofUncounted(resource);
        List<Callable<Void>>            tasks = new ArrayList<>();

        for (int i = 0; i < threads; i++)
        {
            tasks.add(
                () ->
                {
                    for (int j = 0; j < perThread; j++)
                        copyAndClose(uncounted);

                    return null;
                });
        }

        try
        {
            runConcurrently(executor,tasks);
        }
        finally
        {
            executor.shutdownNow();
        }

        uncounted.close();

        assertTrue(uncounted.isResourceClosed());
        assertEquals(1,closeCount.get());
    }

    private void
    copyAndClose(CountedCloseable<AutoCloseable> source) throws Exception
    {
        try
        {
            source.copy().close();
        }
        catch (IllegalStateException ignored)
        {
        }
    }

    private void
    runConcurrently(ExecutorService executor,List<Callable<Void>> tasks) throws Exception
    {
        CountDownLatch     start = new CountDownLatch(1);
        List<Future<Void>> futures = new ArrayList<>();

        for (Callable<Void> task : tasks)
        {
            futures.add(
                executor.submit(
                    () ->
                    {
                        start.await();
                        return task.call();
                    }));
        }

        start.countDown();

        for (Future<Void> future : futures)
            future.get();
    }
}

//////////////////////////////////////////////////////////////////////////////
