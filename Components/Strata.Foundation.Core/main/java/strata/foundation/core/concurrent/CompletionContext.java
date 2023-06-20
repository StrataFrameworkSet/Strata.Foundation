//////////////////////////////////////////////////////////////////////////////
// CompletionContext.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import strata.foundation.core.exception.MultiCauseException;
import strata.foundation.core.utility.ICombiner;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletionException;
import java.util.function.Consumer;

public
class CompletionContext<T>
{
    private Optional<T>         itsResult;
    private Optional<Throwable> itsException;
    private Instant             itsStart;
    private Instant             itsStop;

    public
    CompletionContext(T result,Throwable exception)
    {
        itsResult    = Optional.ofNullable(result);
        itsException = Optional.ofNullable(exception);
        itsStart     = Instant.now();
        itsStop      = null;
    }

    public
    CompletionContext(T result)
    {
        this(result,null);
    }

    public
    CompletionContext(Throwable exception)
    {
        this(null,exception);
    }

    public CompletionContext<T>
    setResult(T result)
    {
        itsResult = Optional.of(result);
        return this;
    }

    public CompletionContext<T>
    clearResult()
    {
        itsResult = Optional.empty();
        return this;
    }

    public CompletionContext<T>
    setException(Throwable exception)
    {
        Throwable cause = exception;

        while (cause instanceof CompletionException && cause.getCause() != null)
            cause = cause.getCause();

        itsException = Optional.of(cause);
        return this;
    }

    public CompletionContext<T>
    clearException()
    {
        itsException = Optional.empty();
        return this;
    }

    public Optional<T>
    getResult()
    {
        return itsResult;
    }

    public T
    getResultIfPresent()
    {
        return hasResult() ? itsResult.get() : null;
    }

    public Optional<Throwable>
    getException()
    {
        return itsException;
    }

    public Throwable
    getExceptionIfPresent()
    {
        return hasException() ? itsException.get() : null;
    }

    public Duration
    getCompletionTime()
    {
        return Duration.between(itsStart,itsStop);
    }

    public boolean
    hasResult()
    {
        return itsResult.isPresent();
    }

    public boolean
    hasException()
    {
        return itsException.isPresent();
    }

    public CompletionContext<T>
    start()
    {
        itsStart = Instant.now();
        return this;
    }

    public CompletionContext<T>
    stop()
    {
        itsStop = Instant.now();
        return this;
    }

    public CompletionContext<T>
    combine(CompletionContext<T> other,ICombiner<T> combiner)
    {
        if (hasException() || other.hasException())
        {
            Throwable x = getExceptionIfPresent();
            Throwable y = other.getExceptionIfPresent();

            if (x != null && y != null)
                return
                    CompletionContext.of(
                        combiner.combine(
                            getResultIfPresent(),
                            other.getResultIfPresent()),
                        new MultiCauseException(x,y));
            else if (x != null)
                return
                    CompletionContext.of(
                        combiner.combine(
                            getResultIfPresent(),
                            other.getResultIfPresent()),
                        x);
            else
                return
                    CompletionContext.of(
                        combiner.combine(
                            getResultIfPresent(),
                            other.getResultIfPresent()),
                        y);
        }
        else
        {
            T x = getResultIfPresent();
            T y = other.getResultIfPresent();

            return of(combiner.combine(x,y));
        }
    }

    public CompletionContext<T>
    apply(Consumer<CompletionContext<T>> consumer)
    {
        consumer.accept(this);
        return this;
    }

    public static <T> CompletionContext<T>
    of(T result,Throwable exception)
    {
        return new CompletionContext<>(result,exception);
    }

    public static <T> CompletionContext<T>
    of(T result)
    {
        return new CompletionContext<>(result);
    }

    public static <T> CompletionContext<T>
    of(Throwable exception)
    {
        return new CompletionContext<>(exception);
    }
}

//////////////////////////////////////////////////////////////////////////////
