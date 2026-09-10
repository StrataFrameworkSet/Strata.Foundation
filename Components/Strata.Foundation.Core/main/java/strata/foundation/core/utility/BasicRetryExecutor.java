//////////////////////////////////////////////////////////////////////////////
// BasicRetryExecutor.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <p>
 * Default {@link IRetryExecutor} implementation that retries a failed
 * operation up to a configured maximum number of attempts, pausing between
 * attempts according to a configurable {@link BackoffStrategy} and backoff
 * factor.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * IRetryExecutor executor =
 *     new BasicRetryExecutor(5,50L,2.0)
 *         .setBackoffStrategy(BackoffStrategy.EXPONENTIAL);
 *
 * executor.executeRun(() -&gt; connect());
 * </pre>
 */
public
class BasicRetryExecutor
    implements IRetryExecutor
{
    private int             maxAttempts;
    private long            delayMillis;
    private BackoffStrategy backoffStrategy;
    private double          backoffFactor;

    public BasicRetryExecutor()
    {
        this(5,50L,2.0);
    }

    public BasicRetryExecutor(int maxAttempts,long delayMillis,double backoffFactor)
    {
        this.maxAttempts   = maxAttempts;
        this.delayMillis   = delayMillis;
        this.backoffStrategy = BackoffStrategy.EXPONENTIAL;
        this.backoffFactor = backoffFactor;
    }

    @Override
    public IRetryExecutor
    setMaxAttempts(int maxAttempts)
    {
        this.maxAttempts = maxAttempts;
        return this;
    }

    @Override
    public IRetryExecutor
    setRetryDelay(long delayMillis)
    {
        this.delayMillis = delayMillis;
        return this;
    }

    @Override
    public IRetryExecutor
    setBackoffStrategy(BackoffStrategy strategy)
    {
        this.backoffStrategy = strategy;
        return this;
    }

    @Override
    public IRetryExecutor
    setBackoffFactor(double backoffFactor)
    {
        this.backoffFactor = backoffFactor;
        return this;
    }

    @Override
    public int
    getMaxAttempts()
    {
        return maxAttempts;
    }

    @Override
    public long
    getRetryDelay()
    {
        return delayMillis;
    }

    @Override
    public BackoffStrategy
    getBackoffStrategy()
    {
        return backoffStrategy;
    }

    @Override
    public double
    getBackoffFactor()
    {
        return backoffFactor;
    }

    @Override
    public void
    executeRun(Runnable runnable)
    {
        for (int attempt = 1; attempt <= maxAttempts; attempt++)
        {
            try
            {
                runnable.run();
                return;
            }
            catch (Exception e)
            {
                if (attempt == maxAttempts)
                    throw e;

                pauseBetweenAttempts(attempt);
            }
        }
    }

    @Override
    public <O> O
    executeCall(Callable<O> callable)
    {
        for (int attempt = 1; attempt <= maxAttempts; attempt++)
        {
            try
            {
                return callable.call();
            }
            catch (Exception e)
            {
                if (attempt == maxAttempts)
                    throw new RuntimeException(e);

                pauseBetweenAttempts(attempt);
            }
        }

        throw new IllegalStateException("Unreachable code");
    }

    @Override
    public <I> void
    executeAccept(Consumer<I> consumer,I input)
    {
        for (int attempt = 1; attempt <= maxAttempts; attempt++)
        {
            try
            {
                consumer.accept(input);
                return;
            }
            catch (Exception e)
            {
                if (attempt == maxAttempts)
                    throw e;

                pauseBetweenAttempts(attempt);
            }
        }
    }

    @Override
    public <O> O
    executeGet(Supplier<O> supplier)
    {
        for (int attempt = 1; attempt <= maxAttempts; attempt++)
        {
            try
            {
                return supplier.get();
            }
            catch (Exception e)
            {
                if (attempt == maxAttempts)
                    throw e;

                pauseBetweenAttempts(attempt);
            }
        }

        throw new IllegalStateException("Unreachable code");
    }

    @Override
    public <I,O> O
    executeApply(Function<I,O> function,I input)
    {
        for (int attempt = 1; attempt <= maxAttempts; attempt++)
        {
            try
            {
                return function.apply(input);
            }
            catch (Exception e)
            {
                if (attempt == maxAttempts)
                    throw e;

                pauseBetweenAttempts(attempt);
            }
        }

        throw new IllegalStateException("Unreachable code");
    }


    private void
    pauseBetweenAttempts(int attempt)
    {
        try
        {
            long calculatedDelay = 0L;

            switch (backoffStrategy)
            {
                case NONE:
                    calculatedDelay = delayMillis * attempt;
                    break;

                case EXPONENTIAL:
                    calculatedDelay = (long)(delayMillis * Math.pow(backoffFactor,attempt-1));
                    break;

                case LINEAR:
                    calculatedDelay = (long)(delayMillis * backoffFactor * attempt);
                    break;
            }

            Thread.sleep(calculatedDelay);
        }
        catch (InterruptedException ie)
        {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Retry interrupted",ie);
        }
    }

}

//////////////////////////////////////////////////////////////////////////////
