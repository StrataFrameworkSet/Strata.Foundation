//////////////////////////////////////////////////////////////////////////////
// CurrentThreadExecutorServiceTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@Tag("CommitStage")
public
class CurrentThreadExecutorServiceTest
{
    private CurrentThreadExecutorService subject;

    @BeforeEach
    public void
    setUp()
    {
        subject = new CurrentThreadExecutorService();
    }

    @Test
    public void
    testExecuteRunsOnCallingThread()
    {
        AtomicReference<Thread> runner = new AtomicReference<>();

        subject.execute(() -> runner.set(Thread.currentThread()));

        assertSame(Thread.currentThread(),runner.get());
    }

    @Test
    public void
    testSubmitReturnsCompletedFuture() throws Exception
    {
        Future<String> result = subject.submit(() -> "done");

        assertTrue(result.isDone());
        assertEquals("done",result.get());
    }

    @Test
    public void
    testInvokeAll() throws Exception
    {
        List<Callable<Integer>> tasks   = List.of(() -> 1,() -> 2,() -> 3);
        List<Future<Integer>>   results = subject.invokeAll(tasks);

        assertEquals(3,results.size());
        assertEquals(1,results.get(0).get());
        assertEquals(2,results.get(1).get());
        assertEquals(3,results.get(2).get());
    }

    @Test
    public void
    testExecuteAfterShutdownRejected()
    {
        subject.shutdown();

        assertThrows(
            RejectedExecutionException.class,
            () -> subject.execute(() -> {}));
    }

    @Test
    public void
    testShutdownState()
    {
        assertFalse(subject.isShutdown());
        assertFalse(subject.isTerminated());

        subject.shutdown();

        assertTrue(subject.isShutdown());
        assertTrue(subject.isTerminated());
    }

    @Test
    public void
    testShutdownNowReturnsEmptyList()
    {
        assertTrue(subject.shutdownNow().isEmpty());
        assertTrue(subject.isShutdown());
    }

    @Test
    public void
    testAwaitTerminationTimesOutWhenNotShutdown() throws Exception
    {
        assertFalse(subject.awaitTermination(50,TimeUnit.MILLISECONDS));
    }

    @Test
    public void
    testAwaitTerminationWaitsForRunningTask() throws Exception
    {
        CountDownLatch taskStarted = new CountDownLatch(1);
        CountDownLatch releaseTask = new CountDownLatch(1);

        Thread worker = new Thread(
            () ->
                subject.execute(
                    () ->
                    {
                        taskStarted.countDown();

                        try
                        {
                            releaseTask.await(5,TimeUnit.SECONDS);
                        }
                        catch (InterruptedException e)
                        {
                            Thread.currentThread().interrupt();
                        }
                    }));

        worker.start();
        assertTrue(taskStarted.await(5,TimeUnit.SECONDS));

        subject.shutdown();

        assertFalse(subject.isTerminated());
        assertFalse(subject.awaitTermination(50,TimeUnit.MILLISECONDS));

        releaseTask.countDown();

        assertTrue(subject.awaitTermination(5,TimeUnit.SECONDS));
        assertTrue(subject.isTerminated());
        worker.join(5000);
    }
}

//////////////////////////////////////////////////////////////////////////////
