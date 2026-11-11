//////////////////////////////////////////////////////////////////////////////
// OwnedCloseableTest.java
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
class OwnedCloseableTest
{
    private AtomicInteger closeCount;
    private AutoCloseable resource;

    @BeforeEach
    public void
    setUp()
    {
        closeCount = new AtomicInteger(0);
        resource = closeCount::incrementAndGet;
    }

    @Test
    public void
    testConstructorOwnerClosesResource() throws Exception
    {
        OwnedCloseable<AutoCloseable> subject = new OwnedCloseable<>(resource,true);

        subject.close();

        assertEquals(1,closeCount.get());
    }

    @Test
    public void
    testConstructorNonOwnerDoesNotCloseResource() throws Exception
    {
        OwnedCloseable<AutoCloseable> subject = new OwnedCloseable<>(resource,false);

        subject.close();

        assertEquals(0,closeCount.get());
    }

    @Test
    public void
    testOfOwnedClosesResource() throws Exception
    {
        OwnedCloseable<AutoCloseable> subject = OwnedCloseable.ofOwned(resource);

        subject.close();

        assertEquals(1,closeCount.get());
    }

    @Test
    public void
    testOfBorrowedDoesNotCloseResource() throws Exception
    {
        OwnedCloseable<AutoCloseable> subject = OwnedCloseable.ofBorrowed(resource);

        subject.close();

        assertEquals(0,closeCount.get());
    }

    @Test
    public void
    testGetReturnsWrappedCloseableWhenOwned()
    {
        assertSame(resource,OwnedCloseable.ofOwned(resource).get());
    }

    @Test
    public void
    testGetReturnsWrappedCloseableWhenBorrowed()
    {
        assertSame(resource,OwnedCloseable.ofBorrowed(resource).get());
    }

    @Test
    public void
    testConstructorOwnerRejectsNull()
    {
        assertThrows(NullPointerException.class,() -> new OwnedCloseable<AutoCloseable>(null,true));
    }

    @Test
    public void
    testConstructorNonOwnerRejectsNull()
    {
        assertThrows(NullPointerException.class,() -> new OwnedCloseable<AutoCloseable>(null,false));
    }

    @Test
    public void
    testOfOwnedRejectsNull()
    {
        assertThrows(NullPointerException.class,() -> OwnedCloseable.ofOwned(null));
    }

    @Test
    public void
    testOfBorrowedRejectsNull()
    {
        assertThrows(NullPointerException.class,() -> OwnedCloseable.ofBorrowed(null));
    }

    @Test
    public void
    testCloseOwnedRethrowsResourceException()
    {
        Exception                     expected = new Exception("failure");
        OwnedCloseable<AutoCloseable> subject  =
            OwnedCloseable.ofOwned(() -> { throw expected; });

        Exception actual = assertThrows(Exception.class,subject::close);

        assertSame(expected,actual);
    }

    @Test
    public void
    testCloseBorrowedIgnoresFailingResource()
    {
        OwnedCloseable<AutoCloseable> subject =
            OwnedCloseable.ofBorrowed(() -> { throw new Exception("failure"); });

        assertDoesNotThrow(subject::close);
    }

    @Test
    public void
    testBorrowedRepeatedCloseNeverClosesResource() throws Exception
    {
        OwnedCloseable<AutoCloseable> subject = OwnedCloseable.ofBorrowed(resource);

        subject.close();
        subject.close();

        assertEquals(0,closeCount.get());
    }

    @Test
    public void
    testOwnedRepeatedCloseClosesResourceOnce() throws Exception
    {
        OwnedCloseable<AutoCloseable> subject = OwnedCloseable.ofOwned(resource);

        subject.close();
        subject.close();
        subject.close();

        assertEquals(1,closeCount.get());
    }

    @Test
    public void
    testOwnedRepeatedCloseAfterExceptionDoesNotRethrow()
    {
        AtomicInteger                 attempts = new AtomicInteger(0);
        OwnedCloseable<AutoCloseable> subject  =
            OwnedCloseable.ofOwned(
                () ->
                {
                    attempts.incrementAndGet();
                    throw new Exception("failure");
                });

        assertThrows(Exception.class,subject::close);
        assertDoesNotThrow(subject::close);
        assertEquals(1,attempts.get());
    }

    @Test
    public void
    testConcurrentRepeatedCloseOfOwnedClosesResourceOnce() throws Exception
    {
        int                           threads = 16;
        ExecutorService               executor = Executors.newFixedThreadPool(threads);
        CountDownLatch                start = new CountDownLatch(1);
        List<Future<?>>               futures = new ArrayList<>();
        OwnedCloseable<AutoCloseable> subject = OwnedCloseable.ofOwned(resource);

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

        assertEquals(1,closeCount.get());
    }

    @Test
    public void
    testTryWithResourcesOwned() throws Exception
    {
        try (OwnedCloseable<AutoCloseable> subject = OwnedCloseable.ofOwned(resource))
        {
            assertSame(resource,subject.get());
        }

        assertEquals(1,closeCount.get());
    }

    @Test
    public void
    testTryWithResourcesBorrowed() throws Exception
    {
        try (OwnedCloseable<AutoCloseable> subject = OwnedCloseable.ofBorrowed(resource))
        {
            assertSame(resource,subject.get());
        }

        assertEquals(0,closeCount.get());
    }
}

//////////////////////////////////////////////////////////////////////////////
