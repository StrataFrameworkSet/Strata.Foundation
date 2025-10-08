/// ///////////////////////////////////////////////////////////////////////////
// BasicRetryAgent.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public
class BasicRetryAgent
    implements IRetryAgent
{
    private int             maxAttempts;
    private long            delayMillis;
    private BackoffStrategy backoffStrategy;
    private double          backoffFactor;

    public
    BasicRetryAgent()
    {
        this(5,50L,2.0);
    }

    public
    BasicRetryAgent(int maxAttempts,long delayMillis,double backoffFactor)
    {
        this.maxAttempts   = maxAttempts;
        this.delayMillis   = delayMillis;
        this.backoffStrategy = BackoffStrategy.EXPONENTIAL;
        this.backoffFactor = backoffFactor;
    }

    @Override
    public IRetryAgent
    setMaxAttempts(int maxAttempts)
    {
        this.maxAttempts = maxAttempts;
        return this;
    }

    @Override
    public IRetryAgent
    setRetryDelay(long delayMillis)
    {
        this.delayMillis = delayMillis;
        return this;
    }

    @Override
    public IRetryAgent
    setBackoffStrategy(BackoffStrategy strategy)
    {
        this.backoffStrategy = strategy;
        return this;
    }

    @Override
    public IRetryAgent
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
    runRetryable(Runnable runnable)
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
    callRetryable(Callable<O> callable)
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
    acceptRetryable(Consumer<I> consumer,I input)
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
    getRetryable(Supplier<O> supplier)
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
    applyRetryable(Function<I,O> function,I input)
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

            if (backoffStrategy == BackoffStrategy.EXPONENTIAL)
                calculatedDelay = (long)(delayMillis * Math.pow(backoffFactor,attempt-1));
            else
                calculatedDelay = (long)(delayMillis * backoffFactor * attempt);

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
