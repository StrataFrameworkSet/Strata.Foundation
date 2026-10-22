//////////////////////////////////////////////////////////////////////////////
// CurrentThreadExecutorService.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * An {@link java.util.concurrent.ExecutorService} that runs tasks
 * synchronously on the calling thread using a
 * {@link CurrentThreadExecutor}, useful for testing or
 * single-threaded execution contexts that require the full
 * executor service lifecycle.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Synchronous execution on current thread
 * ExecutorService executor = new CurrentThreadExecutorService();
 * Future&lt;String&gt; result = executor.submit(() -&gt; "done");
 *
 * result.isDone(); // true
 *
 * executor.shutdown();
 * executor.awaitTermination(1,TimeUnit.SECONDS);
 * </pre>
 */
public
class CurrentThreadExecutorService
    extends AbstractExecutorService
{
    private final Executor      executor;
    private final ReentrantLock lock;
    private final Condition     terminated;
    private boolean             shutdown;
    private int                 runningTasks;

    public
    CurrentThreadExecutorService()
    {
        executor     = new CurrentThreadExecutor();
        lock         = new ReentrantLock();
        terminated   = lock.newCondition();
        shutdown     = false;
        runningTasks = 0;
    }

    @Override
    public void
    execute(Runnable command)
    {
        startTask();

        try
        {
            executor.execute(command);
        }
        finally
        {
            endTask();
        }
    }

    @Override
    public void
    shutdown()
    {
        lock.lock();

        try
        {
            shutdown = true;

            if (runningTasks == 0)
                terminated.signalAll();
        }
        finally
        {
            lock.unlock();
        }
    }

    @Override
    public List<Runnable>
    shutdownNow()
    {
        shutdown();
        return Collections.emptyList();
    }

    @Override
    public boolean
    isShutdown()
    {
        lock.lock();

        try
        {
            return shutdown;
        }
        finally
        {
            lock.unlock();
        }
    }

    @Override
    public boolean
    isTerminated()
    {
        lock.lock();

        try
        {
            return shutdown && runningTasks == 0;
        }
        finally
        {
            lock.unlock();
        }
    }

    @Override
    public boolean
    awaitTermination(long timeout,TimeUnit unit)
        throws InterruptedException
    {
        long nanos = unit.toNanos(timeout);

        lock.lock();

        try
        {
            while (!(shutdown && runningTasks == 0))
            {
                if (nanos <= 0)
                    return false;

                nanos = terminated.awaitNanos(nanos);
            }

            return true;
        }
        finally
        {
            lock.unlock();
        }
    }

    private void
    startTask()
    {
        lock.lock();

        try
        {
            if (shutdown)
                throw new RejectedExecutionException("Executor has been shut down");

            runningTasks++;
        }
        finally
        {
            lock.unlock();
        }
    }

    private void
    endTask()
    {
        lock.lock();

        try
        {
            runningTasks--;

            if (shutdown && runningTasks == 0)
                terminated.signalAll();
        }
        finally
        {
            lock.unlock();
        }
    }
}

//////////////////////////////////////////////////////////////////////////////
