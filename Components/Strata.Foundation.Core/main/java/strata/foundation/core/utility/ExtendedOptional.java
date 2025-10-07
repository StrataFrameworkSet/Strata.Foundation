//////////////////////////////////////////////////////////////////////////////
// Expendable.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public
class ExtendedOptional<T>
    implements IOptionable<T>
{
    private Optional<T> optional;

    public
    ExtendedOptional()
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
    public boolean
    equals(Object other)
    {
        return
            other instanceof ExtendedOptional eo &&
            Objects.equals(optional,eo.optional);
    }

    @Override
    public T
    get()
    {
        return optional.get();
    }

    @Override
    public void
    ifPresent(Consumer<T> consumer)
    {
        optional.ifPresent(consumer);
    }

    @Override
    public <U> U
    ifPresentOrElse(Function<T,U> present,Supplier<U> notPresent)
    {
        return
            optional
                .map(v -> present.apply(v))
                .orElseGet(notPresent);
    }

    @Override
    public <U,E extends RuntimeException> U
    ifPresentOrThrow(Function<T,U> present,E exception)
        throws E
    {
        return
            optional
                .map(v -> present.apply(v))
                .orElseThrow(() -> exception);
    }

    @Override
    public void
    ifPresentOrElseNoReturn(Consumer<T> present,Runnable notPresent)
    {
        optional.ifPresentOrElse(v -> present.accept(v), notPresent);
    }

    @Override
    public <E extends RuntimeException> void
    ifPresentOrThrowNoReturn(Consumer<T> present,E exception)
        throws E
    {
        optional.ifPresentOrElse(
            v -> present.accept(v),
            () -> { throw exception; });
    }

    @Override
    public void
    ifEmpty(Runnable notPresent)
    {
        if (optional.isEmpty())
            notPresent.run();
    }

    @Override
    public ExtendedOptional<T>
    filter(Predicate<T> predicate)
    {
        return
            optional
                .filter(v -> predicate.test(v))
                .map(v -> new ExtendedOptional<>(v))
                .orElseGet(() -> ExtendedOptional.empty());
    }

    @Override
    public <U> ExtendedOptional<U>
    map(Function<T,U> mapper)
    {
        return
            optional
                .map(v -> new ExtendedOptional<>(mapper.apply(v)))
                .orElseGet(() -> ExtendedOptional.empty());
    }

    @Override
    public <U,O extends IOptionable<U>> ExtendedOptional<U>
    flatMap(Function<T,O> mapper)
    {
        return
            new ExtendedOptional<>(
                optional.flatMap(v -> mapper.apply(v).toOptional()));
    }

    @Override
    public <O extends IOptionable<T>> ExtendedOptional<T>
    or(Supplier<O> supplier)
    {
        Objects.requireNonNull(supplier, "Supplier must not be null");

        return
            optional.isPresent()
                ? this
                : new ExtendedOptional<>(
                    supplier
                        .get()
                        .toOptional());
    }

    @Override
    public T
    orElse(T other)
    {
        return optional.orElse(other);
    }

    @Override
    public T
    orElseGet(Supplier<T> other)
    {
        return optional.orElseGet(other);
    }

    @Override
    public T
    orElseThrow() throws NoSuchElementException
    {
        return optional.orElseThrow();
    }

    @Override
    public <E extends RuntimeException> T
    orElseThrow(Supplier<E> exceptionSupplier)
    {
        return optional.orElseThrow(exceptionSupplier);
    }

    @Override
    public boolean
    isPresent()
    {
        return optional.isPresent();
    }

    @Override
    public boolean
    isEmpty()
    {
        return optional.isEmpty();
    }

    @Override
    public Optional<T>
    toOptional() { return optional;}

    private void
    writeObject(ObjectOutputStream out)
        throws IOException
    {
        if (optional.isPresent())
        {
            out.writeBoolean(true);
            out.writeObject(optional.get());
        }
        else
            out.writeBoolean(false);
    }

    private void
    readObject(ObjectInputStream in)
        throws IOException,ClassNotFoundException
    {
        boolean isPresent = in.readBoolean();

        if (isPresent)
            optional = Optional.ofNullable((T)in.readObject());
        else
            optional = Optional.empty();
    }

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
