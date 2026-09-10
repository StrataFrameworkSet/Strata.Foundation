//////////////////////////////////////////////////////////////////////////////
// CompletedResult.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import strata.foundation.core.exception.MultiCauseException;
import strata.foundation.core.utility.ICombiner;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <p>
 * Result container that holds either a successful value or an exception,
 * implementing {@link java.util.function.Supplier} for value access.
 * Supports monadic composition via {@code map} and {@code flatMap}.
 * See: <a href="https://en.wikipedia.org/wiki/Result_type">Result type (Wikipedia)</a>
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Success
 * CompletedResult&lt;String&gt; success = CompletedResult.of("hello");
 * String value = success.get();
 *
 * // Failure
 * CompletedResult&lt;String&gt; failure = CompletedResult.of(new RuntimeException("oops"));
 * failure.ifExceptionPresent(ex -&gt; ex.printStackTrace());
 *
 * // Monadic mapping
 * CompletedResult&lt;Integer&gt; mapped = success.map(String::length);
 * </pre>
 *
 * @param <T> result value type
 */
public
class CompletedResult<T>
    implements Supplier<T>
{
    private Optional<T>         value;
    private Optional<Throwable> exception;

    public
    CompletedResult(T value,Throwable exception)
    {
        this.value     = Optional.ofNullable(value);
        this.exception = Optional.ofNullable(exception);
    }

    public
    CompletedResult(T value)
    {
        this(value,null);
    }

    public
    CompletedResult(Throwable exception)
    {
        this(null,exception);
    }

    @Override
    public T
    get()
        throws CompletionException
    {
        return getValue().orElse(null);
    }

    public Optional<T>
    getValue()
        throws CompletionException
    {
        throwIfExceptionPresent();
        return value;
    }

    public Optional<Throwable>
    getException()
    {
        return exception;
    }

    public void
    ifValuePresent(Consumer<T> consumer)
    {
        if (isValuePresent())
            consumer.accept(value.get());
    }

    public void
    ifExceptionPresent(Consumer<Throwable> consumer)
    {
        if (isExceptionPresent())
            consumer.accept(exception.get());
    }

    public void
    throwIfExceptionPresent()
        throws CompletionException
    {
        if (isExceptionPresent())
            throw new CompletionException(exception.get());
    }

    public boolean
    isValuePresent() { return value.isPresent(); }

    public boolean
    isExceptionPresent() { return exception.isPresent(); }

    public CompletedResult<T>
    combine(CompletedResult<T> other,ICombiner<T> combiner)
    {
        if (isExceptionPresent() || other.isExceptionPresent())
        {
            Throwable x = exception.orElse(null);
            Throwable y = other.exception.orElse(null);

            if (x != null && y != null)
                return CompletedResult.of(new MultiCauseException(x,y));
            else if (x != null)
                return CompletedResult.of(x);
            else
                return CompletedResult.of(y);
        }
        else
            return of(combiner.combine(get(),other.get()));
    }

    public <R> CompletedResult<R>
    map(Function<T,R> mapper)
    {
        if (isValuePresent())
            return CompletedResult.of(mapper.apply(get()));
        else
            return CompletedResult.of(getException().orElse(null));
    }

    public <R> CompletedResult<R>
    flatMap(Function<T,CompletedResult<R>> mapper)
    {
        if (isValuePresent())
            return mapper.apply(get());
        else
            return CompletedResult.of(getException().orElse(null));
    }

    public static <T> CompletedResult<T>
    of(T value)
    {
        return new CompletedResult<>(value);
    }

    public static <T> CompletedResult<T>
    of(Throwable exception)
    {
        return new CompletedResult<>(exception);
    }
}

//////////////////////////////////////////////////////////////////////////////
