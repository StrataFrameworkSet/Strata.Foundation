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
class Expendable<T>
    implements IOptional<T>
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
    ifNotPresent(Runnable notPresent)
    {
        if (!context.isPresent())
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
    public <U,O extends IOptional<U>> Expendable<U>
    flatMap(Function<T,O> mapper)
    {
        return
            context
                .map(c -> this.applyForFlatMap(c,mapper))
                .orElse(Expendable.empty());
    }

    @Override
    public Expendable<T>
    or(Supplier<? extends IOptional<T>> supplier)
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
    orElseThrow(Supplier<? extends RuntimeException> exceptionSupplier)
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
        Function<T,? extends IOptional<U>> function)
    {
        Expendable<U> output = (Expendable<U>)function.apply(context.getValue());

        context.decrementRemaining();

        if (context.isExpended())
            this.context = Optional.empty();

        return output;
    }

}

//////////////////////////////////////////////////////////////////////////////
