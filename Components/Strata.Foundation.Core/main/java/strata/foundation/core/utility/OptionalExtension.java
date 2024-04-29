//////////////////////////////////////////////////////////////////////////////
// OptionalExtension.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public
class OptionalExtension
{
    public static <T,U> U
    ifPresentOrElse(
        Optional<T>   optional,
        Function<T,U> present,
        Supplier<U>   notPresent)
    {
        if (optional.isPresent())
            return present.apply(optional.get());

        return notPresent.get();
    }

    public static <T,U,E extends RuntimeException> U
    ifPresentOrThrow(
        Optional<T>   optional,
        Function<T,U> present,
        E             exception)
        throws E
    {
        if (optional.isPresent())
            return present.apply(optional.get());

        throw exception;
    }

    public static <T> void
    ifPresentOrElseNoReturn(
        Optional<T> optional,
        Consumer<T> present,
        Runnable    notPresent)
    {
        if (optional.isPresent())
            present.accept(optional.get());
        else
            notPresent.run();
    }

    public static <T,E extends RuntimeException> void
    ifPresentOrThrowNoReturn(
        Optional<T> optional,
        Consumer<T> present,
        E           exception)
        throws E
    {
        if (optional.isPresent())
            present.accept(optional.get());
        else
            throw exception;
    }

    public static <T> void
    ifNotPresent(Optional<T> optional,Runnable task)
    {
        if (!optional.isPresent())
            task.run();
    }

    public static <T extends Comparable<T>> int
    compare(Optional<T> x,Optional<T> y)
    {
        if (x.isPresent())
        {
            if (y.isPresent())
                return x.get().compareTo(y.get());

            return 1; // x > y
        }

        if (y.isEmpty())
            return 0; // x == y == empty

        return -1; // x < y
    }
}

//////////////////////////////////////////////////////////////////////////////
