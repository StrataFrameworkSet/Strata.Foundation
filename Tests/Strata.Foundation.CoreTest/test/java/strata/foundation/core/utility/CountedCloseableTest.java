//////////////////////////////////////////////////////////////////////////////
// CountedCloseableTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
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
    testCopyOfCopySharesSameCount() throws Exception
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
    testConcurrentCloseOfCopiesClosesResourceExactlyOnce() throws Exception
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
}

//////////////////////////////////////////////////////////////////////////////
