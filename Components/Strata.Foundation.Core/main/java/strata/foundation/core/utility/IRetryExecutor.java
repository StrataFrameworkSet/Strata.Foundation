//////////////////////////////////////////////////////////////////////////////
// IRetryExecutor.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <p>
 * Defines an executor that retries a unit of work when it fails, according
 * to a configurable maximum number of attempts, a delay between attempts,
 * and a {@link BackoffStrategy} used to grow that delay across successive
 * retries. Work can be supplied as a {@link Runnable}, a {@link Callable},
 * a {@link Consumer}, a {@link Supplier}, or a {@link Function}, letting
 * callers retry actions that consume input, produce output, or both.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * IRetryExecutor executor = ...;
 * executor.setMaxAttempts(3)
 *         .setRetryDelay(100)
 *         .setBackoffStrategy(BackoffStrategy.EXPONENTIAL)
 *         .setBackoffFactor(2.0);
 *
 * String result = executor.executeCall(() -&gt; callRemoteService());
 * </pre>
 */
public
interface IRetryExecutor
{
    IRetryExecutor
    setMaxAttempts(int maxAttempts);

    IRetryExecutor
    setRetryDelay(long delayMillis);

    IRetryExecutor
    setBackoffStrategy(BackoffStrategy strategy);

    IRetryExecutor
    setBackoffFactor(double backoffFactor);

    int
    getMaxAttempts();

    long
    getRetryDelay();

    BackoffStrategy
    getBackoffStrategy();

    double
    getBackoffFactor();

    void
    executeRun(Runnable runnable);

    <O> O
    executeCall(Callable<O> callable);

    <I> void
    executeAccept(Consumer<I> consumer,I input);

    <O> O
    executeGet(Supplier<O> supplier);

    <I,O> O
    executeApply(Function<I,O> function,I input);
}

//////////////////////////////////////////////////////////////////////////////
