/// ///////////////////////////////////////////////////////////////////////////
// NullRetryExecutor.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public
class NullRetryExecutor
    implements IRetryExecutor
{
    @Override
    public IRetryExecutor
    setMaxAttempts(int maxAttempts) { return this; }

    @Override
    public IRetryExecutor
    setRetryDelay(long delayMillis) { return this; }

    @Override
    public IRetryExecutor
    setBackoffStrategy(BackoffStrategy strategy) { return this; }

    @Override
    public IRetryExecutor
    setBackoffFactor(double backoffFactor) { return this; }

    @Override
    public int
    getMaxAttempts() { return 0; }

    @Override
    public long
    getRetryDelay() { return 0; }

    @Override
    public BackoffStrategy
    getBackoffStrategy() { return BackoffStrategy.NONE; }

    @Override
    public double
    getBackoffFactor() { return 0; }

    @Override
    public void
    executeRun(Runnable runnable) { runnable.run(); }

    @Override
    public <O> O
    executeCall(Callable<O> callable)
    {
        try
        {
            return callable.call();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <I> void
    executeAccept(Consumer<I> consumer,I input) { consumer.accept(input); }

    @Override
    public <O> O
    executeGet(Supplier<O> supplier) { return supplier.get(); }

    @Override
    public <I,O> O
    executeApply(Function<I,O> function,I input) { return function.apply(input); }
}

//////////////////////////////////////////////////////////////////////////////
