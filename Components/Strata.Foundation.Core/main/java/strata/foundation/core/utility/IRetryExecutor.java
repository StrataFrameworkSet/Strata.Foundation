/// ///////////////////////////////////////////////////////////////////////////
// IRetryExecutor.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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