//////////////////////////////////////////////////////////////////////////////
// NullRetryExecutor.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <p>
 * A null-object implementation of {@link IRetryExecutor}, following the
 * <a href="https://en.wikipedia.org/wiki/Null_object_pattern">Null object pattern (Wikipedia)</a>.
 * Every configuration setter is a no-op that returns {@code this}, every
 * getter returns a default/zero value, and every {@code execute*} method
 * simply invokes the given action exactly once, immediately, with no
 * retrying, delay, or backoff applied. It is useful as a default or
 * "no retry" strategy wherever an {@link IRetryExecutor} is required but
 * retry behavior is not desired.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * IRetryExecutor executor = new NullRetryExecutor();
 * executor.executeRun(() -&gt; System.out.println("runs once, no retries"));
 * </pre>
 */
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
