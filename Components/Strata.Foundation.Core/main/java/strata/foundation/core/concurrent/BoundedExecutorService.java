//////////////////////////////////////////////////////////////////////////////
// BoundedExecutorService.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>
 * An {@link java.util.concurrent.ExecutorService} decorator that limits
 * the number of concurrently running tasks using a
 * {@link java.util.concurrent.Semaphore}, blocking submission when the
 * maximum number of running tasks is reached. A permit is held until
 * the task completes, so the bound applies to both same-thread and
 * pooled delegate executor services.
 * See: <a href="https://en.wikipedia.org/wiki/Semaphore_(programming)">Semaphore (Wikipedia)</a>
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Limit a virtual thread executor to 5 running tasks with 30s timeout
 * BoundedExecutorService executor =
 *     BoundedExecutorService.of(
 *         Executors.newVirtualThreadPerTaskExecutor(),
 *         5,
 *         Duration.ofSeconds(30));
 *
 * Future&lt;String&gt; result = executor.submit(() -&gt; doWork());
 *
 * // Check available capacity
 * int available = executor.getAvailablePermits();
 *
 * executor.shutdown();
 * </pre>
 */
public
class BoundedExecutorService
    extends AbstractExecutorService
{
    private final ExecutorService executorService;
    private final Semaphore       semaphore;
    private final Duration        timeout;

    public
    BoundedExecutorService(
        ExecutorService executorService,
        int             maxConcurrency,
        Duration        timeout)
    {
        this.executorService = executorService;
        this.semaphore       = new Semaphore(maxConcurrency,true);
        this.timeout         = timeout;
    }

    public
    BoundedExecutorService(int maxConcurrency,Duration timeout)
    {
        this(new CurrentThreadExecutorService(),maxConcurrency,timeout);
    }

    @Override
    public void
    execute(Runnable command)
    {
        AtomicBoolean released = new AtomicBoolean(false);

        acquire();

        try
        {
            executorService.execute(
                () ->
                {
                    try
                    {
                        command.run();
                    }
                    finally
                    {
                        release(released);
                    }
                });
        }
        catch (RuntimeException e)
        {
            release(released);
            throw e;
        }
    }

    @Override
    public void
    shutdown()
    {
        executorService.shutdown();
    }

    @Override
    public List<Runnable>
    shutdownNow()
    {
        return executorService.shutdownNow();
    }

    @Override
    public boolean
    isShutdown()
    {
        return executorService.isShutdown();
    }

    @Override
    public boolean
    isTerminated()
    {
        return executorService.isTerminated();
    }

    @Override
    public boolean
    awaitTermination(long timeout,TimeUnit unit)
        throws InterruptedException
    {
        return executorService.awaitTermination(timeout,unit);
    }

    public int
    getAvailablePermits()
    {
        return semaphore.availablePermits();
    }

    public int
    getQueueLength()
    {
        return semaphore.getQueueLength();
    }

    public static BoundedExecutorService
    of(ExecutorService executorService,int maxConcurrency,Duration timeout)
    {
        return new BoundedExecutorService(executorService,maxConcurrency,timeout);
    }

    public static BoundedExecutorService
    of(int maxConcurrency,Duration timeout)
    {
        return new BoundedExecutorService(maxConcurrency,timeout);
    }

    private void
    acquire()
    {
        try
        {
            if (!semaphore.tryAcquire(timeout.toMillis(),TimeUnit.MILLISECONDS))
                throw new RejectedExecutionException(
                    "Service capacity exceeded: " +
                        semaphore.getQueueLength() + " requests waiting");
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
            throw new RejectedExecutionException(
                "Interrupted waiting for service capacity",e);
        }
    }

    private void
    release(AtomicBoolean released)
    {
        if (released.compareAndSet(false,true))
            semaphore.release();
    }
}

//////////////////////////////////////////////////////////////////////////////
