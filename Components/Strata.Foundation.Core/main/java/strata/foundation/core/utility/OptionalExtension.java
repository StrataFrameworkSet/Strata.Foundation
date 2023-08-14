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

    public static <T> void
    ifNotPresent(Optional<T> optional,Runnable task)
    {
        if (!optional.isPresent())
            task.run();
    }
}

//////////////////////////////////////////////////////////////////////////////
