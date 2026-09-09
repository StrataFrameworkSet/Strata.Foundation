//////////////////////////////////////////////////////////////////////////////
// BoundedExecutor.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.time.Duration;
import java.util.concurrent.*;

/**
 * <p>
 * An {@link java.util.concurrent.Executor} decorator that limits
 * concurrency using a {@link java.util.concurrent.Semaphore},
 * blocking submission when the maximum number of concurrent
 * tasks is reached.
 * See: <a href="https://en.wikipedia.org/wiki/Semaphore_(programming)">Semaphore (Wikipedia)</a>
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Limit to 5 concurrent tasks with 30s timeout
 * BoundedExecutor executor =
 *     BoundedExecutor.of(5,Duration.ofSeconds(30));
 *
 * executor.execute(() -&gt; doWork());
 *
 * // Check available capacity
 * int available = executor.getAvailablePermits();
 * </pre>
 * </p>
 */
public
class BoundedExecutor
    implements Executor
{
    private final Executor  executor;
    private final Semaphore semaphore;
    private final Duration  timeout;

    public
    BoundedExecutor(Executor executor,int maxConcurrency,Duration timeout)
    {
        this.executor = executor;
        this.semaphore = new Semaphore(maxConcurrency,true);
        this.timeout   = timeout;
    }

    public
    BoundedExecutor(int maxConcurrency,Duration timeout)
    {
        this(new CurrentThreadExecutor(),maxConcurrency,timeout);
    }

    @Override
    public void
    execute(Runnable command)
    {
        boolean acquired = false;

        try
        {
            acquired =
                semaphore.tryAcquire(timeout.toMillis(),TimeUnit.MILLISECONDS);

            if (!acquired)
                throw new RejectedExecutionException(
                    "Service capacity exceeded: " +
                        semaphore.getQueueLength() + " requests waiting");

            executor.execute(command);
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
            throw new RejectedExecutionException(
                "Interrupted waiting for service capacity",e);
        }
        finally
        {
            if (acquired)
                semaphore.release();
        }
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

    public static BoundedExecutor
    of(Executor executor,int maxConcurrency,Duration timeout)
    {
        return new BoundedExecutor(executor,maxConcurrency,timeout);
    }

    public static BoundedExecutor
    of(int maxConcurrency,Duration timeout)
    {
        return new BoundedExecutor(maxConcurrency,timeout);
    }
}

//////////////////////////////////////////////////////////////////////////////
