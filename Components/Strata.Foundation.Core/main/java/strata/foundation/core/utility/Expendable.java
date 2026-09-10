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

/**
 * <p>
 * {@link IOptionable} implementation that wraps a value which may be
 * retrieved only a limited number of times before it is automatically
 * treated as empty, useful for representing single-use or rate-limited
 * resources.
 * </p>
 * <br/>
 * <b>Type Parameter</b><br/>
 * {@code <T>} - the type of the wrapped value
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * Expendable&lt;String&gt; token = Expendable.of("one-time-token",1);
 *
 * token.ifPresent(t -&gt; authenticate(t));
 * </pre>
 */
public
class Expendable<T>
    implements IOptionable<T>
{
    private final int                      allowed;
    private Optional<ExpendableContext<T>> context;

    public
    Expendable()
    {
        this(null,0);
    }

    public
    Expendable(T value)
    {
        this(value,1);
    }

    public
    Expendable(T value,int allowed)
    {
        if (value != null)
        {
            this.allowed = Math.max(1,allowed);
            this.context = Optional.of(new ExpendableContext<>(value,this.allowed));
        }
        else
        {
            this.allowed = 0;
            this.context = Optional.empty();
        }
    }

    @Override
    public int
    hashCode()
    {
        return Objects.hash(allowed,context);
    }

    @Override
    public boolean
    equals(Object other)
    {
        return
            (other instanceof Expendable) &&
            Objects.equals(allowed,((Expendable<?>)other).allowed) &&
            Objects.equals(context,((Expendable<?>)other).context);
    }

    @Override
    public T
    get()
    {
        return
            context
                .map(c -> this.apply(c,value -> value))
                .orElseThrow();
    }

    @Override
    public void
    ifPresent(Consumer<T> consumer)
    {
        context.ifPresent(c -> accept(c,consumer));
    }

    @Override
    public <U> U
    ifPresentOrElse(Function<T,U> present,Supplier<U> notPresent)
    {
        return
            context
                .map(c -> this.apply(c,present))
                .orElseGet(notPresent);
    }

    @Override
    public <U,E extends RuntimeException> U
    ifPresentOrThrow(Function<T,U> present,E exception)
        throws E
    {
        return
            context
                .map(c -> this.apply(c,present))
                .orElseThrow(() -> exception);
    }

    @Override
    public void
    ifPresentOrElseNoReturn(Consumer<T> present,Runnable notPresent)
    {
        if (context.isPresent())
            context.ifPresent(c -> this.accept(c,present));
        else
            notPresent.run();
    }

    @Override
    public <E extends RuntimeException> void
    ifPresentOrThrowNoReturn(Consumer<T> present,E exception)
        throws E
    {
        if (context.isPresent())
            context.ifPresent(c -> this.accept(c,present));
        else
            throw exception;
    }

    @Override
    public void
    ifEmpty(Runnable notPresent)
    {
        if (context.isEmpty())
            notPresent.run();
    }

    @Override
    public Expendable<T>
    filter(Predicate<T> predicate)
    {
        return
            context
                .filter(c -> predicate.test(c.getValue()))
                .map(c -> new Expendable<>(c.getValue(),allowed))
                .orElseGet(() -> Expendable.empty());
    }

    @Override
    public <U> Expendable<U>
    map(Function<T,U> mapper)
    {
        return
            context
                .map(c -> new Expendable<>(this.apply(c,mapper),allowed))
                .orElseGet(() -> Expendable.empty());
    }

    @Override
    public <U,O extends IOptionable<U>> Expendable<U>
    flatMap(Function<T,O> mapper)
    {
        return
            context
                .map(c -> this.applyForFlatMap(c,mapper))
                .orElse(Expendable.empty());
    }

    @Override
    public <O extends IOptionable<T>> Expendable<T>
    or(Supplier<O> supplier)
    {
        Objects.requireNonNull(supplier, "Supplier must not be null");

        return
            context.isPresent()
                ? this
                : new Expendable<>(
                    supplier
                        .get()
                        .orElse(null),
                    allowed);
    }

    @Override
    public T
    orElse(T other)
    {
        return
            context
                .map(c -> this.apply(c,value -> value))
                .orElse(other);
    }

    @Override
    public T
    orElseGet(Supplier<T> other)
    {
        return
            context
                .map(c -> this.apply(c,value -> value))
                .orElseGet(other);
    }

    @Override
    public T
    orElseThrow() throws NoSuchElementException
    {
        return
            context
                .map(c -> this.apply(c,value -> value))
                .orElseThrow();
    }

    @Override
    public <E extends RuntimeException> T
    orElseThrow(Supplier<E> exceptionSupplier)
    {
        return
            context
                .map(c -> this.apply(c,value -> value))
                .orElseThrow(exceptionSupplier);
    }

    @Override
    public boolean
    isPresent()
    {
        return context.isPresent();
    }

    @Override
    public boolean
    isEmpty()
    {
        return context.isEmpty();
    }

    @Override
    public Optional<T> toOptional()
    {
        return Optional.empty();
    }

    public boolean
    isExpended()
    {
        return
            context
                .orElse(new ExpendableContext<>(null,0))
                .isExpended();
    }

    public int
    getAllowed() { return allowed; }

    public int
    getRemaining()
    {
        return
            context
                .map(ExpendableContext::getRemaining)
                .orElse(0);
    }

    private void
    writeObject(ObjectOutputStream out)
        throws IOException
    {
        if (context.isPresent())
        {
            out.writeBoolean(true);
            out.writeObject(context.get());
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
            context = Optional.ofNullable((ExpendableContext<T>)in.readObject());
        else
            context = Optional.empty();
    }

    public static <T> Expendable<T>
    of(T value)
    {
        return of(value,1);
    }

    public static <T> Expendable<T>
    of(T value,int allowed)
    {
        return new Expendable<>(value,allowed);
    }

    public static <T> Expendable<T>
    empty()
    {
        return of(null,0);
    }

    private void
    accept(ExpendableContext<T> context,Consumer<T> consumer)
    {
        consumer.accept(context.getValue());
        context.decrementRemaining();

        if (context.isExpended())
            this.context = Optional.empty();
    }


    private <U> U
    apply(ExpendableContext<T> context,Function<T,U> function)
    {
        U output = function.apply(context.getValue());

        context.decrementRemaining();

        if (context.isExpended())
            this.context = Optional.empty();

        return output;
    }

    private <U> Expendable<U>
    applyForFlatMap(
        ExpendableContext<T>               context,
        Function<T,? extends IOptionable<U>> function)
    {
        Expendable<U> output = (Expendable<U>)function.apply(context.getValue());

        context.decrementRemaining();

        if (context.isExpended())
            this.context = Optional.empty();

        return output;
    }

}

//////////////////////////////////////////////////////////////////////////////
