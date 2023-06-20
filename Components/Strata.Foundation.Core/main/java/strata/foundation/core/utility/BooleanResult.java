//////////////////////////////////////////////////////////////////////////////
// BooleanResult.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.Optional;
import java.util.function.Supplier;

public
class BooleanResult
    implements Supplier<Boolean>
{
    private final Boolean result;

    public BooleanResult(Boolean r)
    {
        result = r;
    }

    @Override
    public Boolean
    get() { return result; }

    public <T> Optional<T>
    ifTrue(Supplier<T> supplier)
    {
        return
            result
                ? Optional.ofNullable(supplier.get())
                : Optional.empty();
    }

    public <T> Optional<T>
    ifFalse(Supplier<T> supplier)
    {
        return
            !result
                ? Optional.ofNullable(supplier.get())
                : Optional.empty();
    }

    public void
    ifTrue(Runnable action)
    {
        if (result)
            action.run();
    }

    public void
    ifFalse(Runnable action)
    {
        if (!result)
            action.run();
    }

    public void
    ifTrueOrThrow(Runnable action,Throwable exception)
        throws Throwable
    {
        if (result)
            action.run();
        else
            throw exception;
    }

    public void
    ifFalseOrThrow(Runnable action,Throwable exception)
        throws Throwable
    {
        if (!result)
            action.run();
        else
            throw exception;
    }

    public <T> T
    ifTrueOrElse(Supplier<T> trueSupplier,Supplier<T> falseSupplier)
    {
        if (result)
            return trueSupplier.get();

        return falseSupplier.get();
    }

    public void
    ifTrueOrElse(Runnable trueAction,Runnable falseAction)
    {
        if (result)
            trueAction.run();
        else
            falseAction.run();
    }

    public Boolean
    orElse(Supplier<Boolean> falseSupplier)
    {
        if (result)
            return true;

        return falseSupplier.get();
    }

    public BooleanResult
    and(BooleanResult other) {return BooleanResult.of(result && other.result);}

    public BooleanResult
    or(BooleanResult other) {return BooleanResult.of(result || other.result);}

    public BooleanResult
    xor(BooleanResult other) {return BooleanResult.of(result ^ other.result);}

    public BooleanResult
    not() {return BooleanResult.of(!result);}

    public static BooleanResult
    of(Boolean source)
    {
        return new BooleanResult(source);
    }
}

//////////////////////////////////////////////////////////////////////////////
