//////////////////////////////////////////////////////////////////////////////
// Conditional.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.Optional;
import java.util.function.Supplier;

public
class Conditional
    implements Supplier<Boolean>,Comparable<Conditional>
{
    public static final Conditional TRUE = Conditional.of(true);
    public static final Conditional FALSE = Conditional.of(false);

    private final Boolean condition;

    public Conditional(Boolean c)
    {
        condition = c;
    }

    @Override
    public int
    hashCode()
    {
        return 73*condition.hashCode();
    }

    @Override
    public boolean
    equals(Object other)
    {
        return
            other instanceof Conditional otherConditional
                ? condition.equals(otherConditional.condition)
                : false;
    }

    @Override
    public Boolean
    get()
    {
        return condition;
    }

    @Override
    public int
    compareTo(Conditional other)
    {
        return condition.compareTo(other.condition);
    }

    public <T> Optional<T>
    ifTrue(Supplier<T> supplier)
    {
        return
            condition
                ? Optional.ofNullable(supplier.get())
                : Optional.empty();
    }

    public <T> Optional<T>
    ifFalse(Supplier<T> supplier)
    {
        return
            !condition
                ? Optional.ofNullable(supplier.get())
                : Optional.empty();
    }

    public void
    ifTrue(Runnable action)
    {
        if (condition)
            action.run();
    }

    public void
    ifFalse(Runnable action)
    {
        if (!condition)
            action.run();
    }

    public <T> T
    ifTrueOrThrow(Supplier<T> supplier,Throwable exception)
        throws Throwable
    {
        if (condition)
            return supplier.get();

        throw exception;
    }

    public <T> T
    ifFalseOrThrow(Supplier<T> supplier,Throwable exception)
        throws Throwable
    {
        if (!condition)
            return supplier.get();

        throw exception;
    }

    public void
    ifTrueOrThrow(Runnable action,Throwable exception)
        throws Throwable
    {
        if (condition)
            action.run();
        else
            throw exception;
    }

    public void
    ifFalseOrThrow(Runnable action,Throwable exception)
        throws Throwable
    {
        if (!condition)
            action.run();
        else
            throw exception;
    }

    public <T> T
    ifTrueOrElse(Supplier<T> trueSupplier,Supplier<T> falseSupplier)
    {
        if (condition)
            return trueSupplier.get();

        return falseSupplier.get();
    }

    public void
    ifTrueOrElse(Runnable trueAction,Runnable falseAction)
    {
        if (condition)
            trueAction.run();
        else
            falseAction.run();
    }

    public Conditional
    and(Conditional other)
    {
        return Conditional.of(condition && other.condition);
    }

    public Conditional
    and(Boolean other)
    {
        return Conditional.of(condition && other);
    }

    public Conditional
    or(Conditional other)
    {
        return Conditional.of(condition || other.condition);
    }

    public Conditional
    or(Boolean other)
    {
        return Conditional.of(condition || other);
    }

    public Conditional
    xor(Conditional other)
    {
        return Conditional.of(condition ^ other.condition);
    }

    public Conditional
    xor(Boolean other)
    {
        return Conditional.of(condition ^ other);
    }

    public Conditional
    not()
    {
        return Conditional.of(!condition);
    }

    public static Conditional
    of(Boolean source)
    {
        return new Conditional(source);
    }

}

//////////////////////////////////////////////////////////////////////////////
