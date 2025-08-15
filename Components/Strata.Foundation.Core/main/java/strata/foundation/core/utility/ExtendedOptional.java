//////////////////////////////////////////////////////////////////////////////
// Expendable.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public
class ExtendedOptional<T>
    implements Supplier<T>
{
    private Optional<T> optional;

    public ExtendedOptional()
    {
        this(Optional.empty());
    }

    public
    ExtendedOptional(T value)
    {
        this(Optional.ofNullable(value));
    }

    public
    ExtendedOptional(Optional<T> source)
    {
        optional = source != null ? source : Optional.empty();
    }

    @Override
    public int
    hashCode()
    {
        return Objects.hash(optional);
    }

    @Override
    public T
    get()
    {
        return optional.get();
    }

    public void
    ifPresent(Consumer<T> consumer)
    {
        optional.ifPresent(consumer);
    }

    public <U> U
    ifPresentOrElse(Function<T,U> present,Supplier<U> notPresent)
    {
        return
            optional
                .map(v -> present.apply(v))
                .orElseGet(notPresent);
    }

    public <U,E extends RuntimeException> U
    ifPresentOrThrow(Function<T,U> present,E exception)
        throws E
    {
        return
            optional
                .map(v -> present.apply(v))
                .orElseThrow(() -> exception);
    }

    public void
    ifPresentOrElseNoReturn(Consumer<T> present,Runnable notPresent)
    {
        if (optional.isPresent())
            optional.ifPresent(v -> present.accept(v));
        else
            notPresent.run();
    }

    public <E extends RuntimeException> void
    ifPresentOrThrowNoReturn(Consumer<T> present,E exception)
        throws E
    {
        if (optional.isPresent())
            optional.ifPresent(v -> present.accept(v));
        else
            throw exception;
    }

    public void
    ifNotPresent(Runnable notPresent)
    {
        if (!optional.isPresent())
            notPresent.run();
    }

    public ExtendedOptional<T>
    filter(Predicate<T> predicate)
    {
        return
            optional
                .filter(v -> predicate.test(v))
                .map(v -> new ExtendedOptional<>(v))
                .orElseGet(() -> ExtendedOptional.empty());
    }

    public <U> ExtendedOptional<U>
    map(Function<T,U> mapper)
    {
        return
            optional
                .map(v -> new ExtendedOptional<>(mapper.apply(v)))
                .orElseGet(() -> ExtendedOptional.empty());
    }

    public <U> ExtendedOptional<U>
    flatMap(Function<T,ExtendedOptional<U>> mapper)
    {
        return
            new ExtendedOptional<>(
                optional.flatMap(v -> mapper.apply(v).toOptional()));
    }

    public T
    orElse(T other)
    {
        return optional.orElse(other);
    }

    public T
    orElseGet(Supplier<T> other)
    {
        return optional.orElseGet(other);
    }

    public T
    orElseThrow(Supplier<? extends RuntimeException> exceptionSupplier)
    {
        return optional.orElseThrow(exceptionSupplier);
    }

    public boolean
    isPresent()
    {
        return optional.isPresent();
    }

    public boolean
    isEmpty()
    {
        return optional.isEmpty();
    }

    public Optional<T>
    toOptional() { return optional;}

    public static <T> ExtendedOptional<T>
    of(T value)
    {
        return new ExtendedOptional<>(value);
    }

    public static <T> ExtendedOptional<T>
    of(Optional<T> extendee)
    {
        return new ExtendedOptional<>(extendee);
    }

    public static <T> ExtendedOptional<T>
    empty()
    {
        return of(Optional.empty());
    }

}

//////////////////////////////////////////////////////////////////////////////
