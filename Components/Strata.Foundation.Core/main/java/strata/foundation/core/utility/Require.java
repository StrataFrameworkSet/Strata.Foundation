//////////////////////////////////////////////////////////////////////////////
// Require.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * <p>
 * Static precondition checks for validating arguments and state. Each
 * check either returns the validated value or throws an exception, which
 * is either a {@link NullPointerException} or an exception produced by a
 * caller-supplied {@link java.util.function.Supplier}.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Null checks
 * String name = Require.notNull(input,"input is null");
 * String value = Require.notNull(input,() -&gt; new IllegalStateException("missing"));
 *
 * // Defaults
 * String label = Require.notNullElse(input,"default");
 * String computed = Require.notNullElseGet(input,() -&gt; compute());
 *
 * // Conditions
 * Require.isTrue(count &gt; 0,() -&gt; new IllegalArgumentException("count must be positive"));
 * Require.isFalse(list.isEmpty(),() -&gt; new IllegalArgumentException("list is empty"));
 * </pre>
 */
public
class Require
{
    private Require() {}

    public static <E extends Throwable> void
    isTrue(boolean condition,Supplier<E> supplier)
        throws NullPointerException, E
    {
        Objects.requireNonNull(supplier,"supplier is null");

        if (!condition)
            throw supplier.get();
    }

    public static <E extends Throwable> void
    isFalse(boolean condition,Supplier<E> supplier)
        throws NullPointerException, E
    {
        Objects.requireNonNull(supplier,"supplier is null");

        if (condition)
            throw supplier.get();
    }

    public static <T> T
    notNull(T obj)
        throws NullPointerException
    {
        return Objects.requireNonNull(obj);
    }

    public static <T> T
    notNull(T obj,String message)
        throws NullPointerException
    {
        return Objects.requireNonNull(obj,message);
    }

    public static <T,E extends Throwable> T
    notNull(T obj,Supplier<E> supplier)
        throws NullPointerException, E
    {
        Objects.requireNonNull(supplier,"supplier is null");

        if (obj == null)
            throw supplier.get();

        return obj;
    }

    public static <T> T
    notNullElse(T obj,T defaultObj)
        throws NullPointerException
    {
        return Objects.requireNonNullElse(obj,defaultObj);
    }

    public static <T,E extends Throwable> T
    notNullElse(T obj,T defaultObj,Supplier<E> supplier)
        throws NullPointerException, E
    {
        Objects.requireNonNull(supplier,"supplier is null");

        if (obj != null)
            return obj;

        return notNull(defaultObj,supplier);
    }

    public static <T> T
    notNullElseGet(T obj,Supplier<? extends T> supplier)
        throws NullPointerException
    {
        Objects.requireNonNull(supplier,"supplier is null");

        return Objects.requireNonNullElseGet(obj,supplier);
    }

    public static <T,E extends Throwable> T
    notNullElseGet(
        T                     obj,
        Supplier<? extends T> defaultSupplier,
        Supplier<E>           exceptionSupplier)
        throws NullPointerException, E
    {
        Objects.requireNonNull(defaultSupplier,"default supplier is null");
        Objects.requireNonNull(exceptionSupplier,"exception supplier is null");

        if (obj != null)
            return obj;

        return
            notNull(
                defaultSupplier.get(),
                exceptionSupplier);
    }
}

//////////////////////////////////////////////////////////////////////////////
