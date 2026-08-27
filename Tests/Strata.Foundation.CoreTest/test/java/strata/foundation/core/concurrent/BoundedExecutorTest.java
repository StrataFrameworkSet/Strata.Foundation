//////////////////////////////////////////////////////////////////////////////
// BoundedExecutorTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@Tag("CommitStage")
public
class BoundedExecutorTest
{
    private static final int      MAX_CONCURRENCY = 3;
    private static final Duration TIMEOUT         = Duration.ofMillis(500);

    private BoundedExecutor subject;
    private ExecutorService threadPool;

    @BeforeEach
    public void
    setUp()
    {
        subject    = new BoundedExecutor(MAX_CONCURRENCY,TIMEOUT);
        threadPool = Executors.newFixedThreadPool(MAX_CONCURRENCY + 2);
    }

    @AfterEach
    public void
    tearDown() throws Exception
    {
        threadPool.shutdownNow();
        threadPool.awaitTermination(2,TimeUnit.SECONDS);
    }

    @Test
    public void
    testSuccessfulExecution()
    {
        AtomicBoolean executed = new AtomicBoolean(false);

        subject.execute(() -> executed.set(true));

        assertTrue(executed.get());
    }

    @Test
    public void
    testStaticFactory()
    {
        BoundedExecutor executor = BoundedExecutor.of(5,Duration.ofSeconds(1));

        assertNotNull(executor);
        assertEquals(5,executor.getAvailablePermits());
    }

    @Test
    public void
    testConstructorWithExecutor() throws Exception
    {
        ExecutorService impl      = Executors.newFixedThreadPool(MAX_CONCURRENCY + 2);
        CountDownLatch  completed = new CountDownLatch(1);
        AtomicBoolean   executed  = new AtomicBoolean(false);
        BoundedExecutor executor  = new BoundedExecutor(impl,MAX_CONCURRENCY,TIMEOUT);

        executor.execute(
            () ->
            {
                executed.set(true);
                completed.countDown();
            });

        assertTrue(completed.await(5,TimeUnit.SECONDS));
        assertTrue(executed.get());
        impl.shutdown();
    }

    @Test
    public void
    testStaticFactoryWithExecutor() throws Exception
    {
        ExecutorService impl      = Executors.newVirtualThreadPerTaskExecutor();
        CountDownLatch  completed = new CountDownLatch(1);
        AtomicBoolean   executed  = new AtomicBoolean(false);
        BoundedExecutor executor  = BoundedExecutor.of(impl,200,TIMEOUT);

        executor.execute(
            () ->
            {
                executed.set(true);
                completed.countDown();
            });

        assertTrue(completed.await(5,TimeUnit.SECONDS));
        assertTrue(executed.get());
        impl.shutdown();
    }

    @Test
    public void
    testAvailablePermitsAfterConstruction()
    {
        assertEquals(MAX_CONCURRENCY,subject.getAvailablePermits());
    }

    @Test
    public void
    testPermitsRestoredAfterExecution()
    {
        subject.execute(() -> {});

        assertEquals(MAX_CONCURRENCY,subject.getAvailablePermits());
    }

    @Test
    public void
    testPermitsRestoredAfterCommandException()
    {
        try
        {
            subject.execute(
                () ->
                {
                    throw new RuntimeException("command failed");
                });
        }
        catch (RuntimeException e) {}

        assertEquals(MAX_CONCURRENCY,subject.getAvailablePermits());
    }

    @Test
    public void
    testConcurrencyBound() throws Exception
    {
        CountDownLatch allSlotsOccupied = new CountDownLatch(MAX_CONCURRENCY);
        CountDownLatch releaseAll       = new CountDownLatch(1);
        AtomicInteger  peakConcurrency  = new AtomicInteger(0);
        AtomicInteger  activeTasks      = new AtomicInteger(0);

        for (int i = 0; i < MAX_CONCURRENCY; i++)
        {
            threadPool.submit(
                () ->
                {
                    subject.execute(
                        () ->
                        {
                            int current = activeTasks.incrementAndGet();
                            peakConcurrency.accumulateAndGet(
                                current,
                                Math::max);
                            allSlotsOccupied.countDown();

                            try
                            {
                                releaseAll.await(5,TimeUnit.SECONDS);
                            }
                            catch (InterruptedException e)
                            {
                                Thread.currentThread().interrupt();
                            }
                            finally
                            {
                                activeTasks.decrementAndGet();
                            }
                        });
                });
        }

        assertTrue(allSlotsOccupied.await(5,TimeUnit.SECONDS));
        assertEquals(0,subject.getAvailablePermits());
        assertEquals(MAX_CONCURRENCY,peakConcurrency.get());

        releaseAll.countDown();
    }

    @Test
    public void
    testTimeoutRejection() throws Exception
    {
        BoundedExecutor  executor         = BoundedExecutor.of(1,Duration.ofMillis(100));
        CountDownLatch   slotOccupied     = new CountDownLatch(1);
        CountDownLatch   releaseSlot      = new CountDownLatch(1);

        threadPool.submit(
            () ->
            {
                executor.execute(
                    () ->
                    {
                        slotOccupied.countDown();

                        try
                        {
                            releaseSlot.await(5,TimeUnit.SECONDS);
                        }
                        catch (InterruptedException e)
                        {
                            Thread.currentThread().interrupt();
                        }
                    });
            });

        assertTrue(slotOccupied.await(5,TimeUnit.SECONDS));

        try
        {
            executor.execute(() -> {});
            fail("Should have thrown RejectedExecutionException");
        }
        catch (RejectedExecutionException e)
        {
            assertTrue(e.getMessage().contains("Service capacity exceeded"));
        }
        finally
        {
            releaseSlot.countDown();
        }
    }

    @Test
    public void
    testInterruptedWhileWaiting() throws Exception
    {
        BoundedExecutor executor     = BoundedExecutor.of(1,Duration.ofSeconds(5));
        CountDownLatch  slotOccupied = new CountDownLatch(1);
        CountDownLatch  releaseSlot  = new CountDownLatch(1);

        threadPool.submit(
            () ->
            {
                executor.execute(
                    () ->
                    {
                        slotOccupied.countDown();

                        try
                        {
                            releaseSlot.await(10,TimeUnit.SECONDS);
                        }
                        catch (InterruptedException e)
                        {
                            Thread.currentThread().interrupt();
                        }
                    });
            });

        assertTrue(slotOccupied.await(5,TimeUnit.SECONDS));

        AtomicBoolean                    interruptFlagRestored = new AtomicBoolean(false);
        AtomicBoolean                    rejected              = new AtomicBoolean(false);
        CountDownLatch                   waiterStarted         = new CountDownLatch(1);

        Thread waiter = new Thread(
            () ->
            {
                waiterStarted.countDown();

                try
                {
                    executor.execute(() -> {});
                }
                catch (RejectedExecutionException e)
                {
                    rejected.set(true);
                    interruptFlagRestored.set(Thread.currentThread().isInterrupted());
                }
            });

        waiter.start();
        assertTrue(waiterStarted.await(5,TimeUnit.SECONDS));
        Thread.sleep(50);
        waiter.interrupt();
        waiter.join(5000);

        assertTrue(rejected.get());
        assertTrue(interruptFlagRestored.get());

        releaseSlot.countDown();
    }

    @Test
    public void
    testGetQueueLength() throws Exception
    {
        BoundedExecutor executor     = BoundedExecutor.of(1,Duration.ofSeconds(5));
        CountDownLatch  slotOccupied = new CountDownLatch(1);
        CountDownLatch  releaseSlot  = new CountDownLatch(1);
        CountDownLatch  waiterQueued = new CountDownLatch(1);

        threadPool.submit(
            () ->
            {
                executor.execute(
                    () ->
                    {
                        slotOccupied.countDown();

                        try
                        {
                            releaseSlot.await(10,TimeUnit.SECONDS);
                        }
                        catch (InterruptedException e)
                        {
                            Thread.currentThread().interrupt();
                        }
                    });
            });

        assertTrue(slotOccupied.await(5,TimeUnit.SECONDS));

        threadPool.submit(
            () ->
            {
                waiterQueued.countDown();
                executor.execute(() -> {});
            });

        assertTrue(waiterQueued.await(5,TimeUnit.SECONDS));
        Thread.sleep(50);
        assertTrue(executor.getQueueLength() >= 1);

        releaseSlot.countDown();
    }
}

//////////////////////////////////////////////////////////////////////////////
