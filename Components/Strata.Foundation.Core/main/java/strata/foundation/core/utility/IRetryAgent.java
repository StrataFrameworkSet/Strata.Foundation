/// ///////////////////////////////////////////////////////////////////////////
// IRetryAgent.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public
interface IRetryAgent
{
    IRetryAgent
    setMaxAttempts(int maxAttempts);

    IRetryAgent
    setRetryDelay(long delayMillis);

    IRetryAgent
    setBackoffStrategy(BackoffStrategy strategy);

    IRetryAgent
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
    runRetryable(Runnable runnable);

    <O> O
    callRetryable(Callable<O> callable);

    <I> void
    acceptRetryable(Consumer<I> consumer,I input);

    <O> O
    getRetryable(Supplier<O> supplier);

    <I,O> O
    applyRetryable(Function<I,O> function,I input);
}

//////////////////////////////////////////////////////////////////////////////