//////////////////////////////////////////////////////////////////////////////
// OptionalExtension.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <p>
 * A utility class providing extension-style helper methods for
 * {@link java.util.Optional} that are not part of its standard API, such
 * as branching on presence/absence with a return value, throwing a custom
 * exception when a value is absent, and comparing two {@code Optional}
 * values by their contained {@link Comparable} value.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * Optional&lt;String&gt; name = Optional.of("Ada");
 * String greeting = OptionalExtension.ifPresentOrElse(
 *     name,
 *     n -&gt; "Hello, " + n,
 *     () -&gt; "Hello, stranger");
 *
 * OptionalExtension.ifNotPresent(Optional.empty(), () -&gt; System.out.println("empty"));
 * </pre>
 * </p>
 */
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
