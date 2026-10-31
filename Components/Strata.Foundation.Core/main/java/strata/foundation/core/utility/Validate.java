//////////////////////////////////////////////////////////////////////////////
// Validate.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.Objects;
import java.util.function.Supplier;

public
class Validate
{
    private Validate() {}

    public static <E extends Throwable> void
    require(boolean condition,Supplier<E> supplier)
        throws NullPointerException, E
    {
        Objects.requireNonNull(supplier,"supplier is null");

        if (!condition)
            throw supplier.get();
    }

    public static <T> T
    requireNonNull(T obj)
        throws NullPointerException
    {
        return Objects.requireNonNull(obj);
    }

    public static <T> T
    requireNonNull(T obj,String message)
        throws NullPointerException
    {
        return Objects.requireNonNull(obj,message);
    }

    public static <T,E extends Throwable> T
    requireNonNull(T obj,Supplier<E> supplier)
        throws NullPointerException, E
    {
        Objects.requireNonNull(supplier,"supplier is null");

        if (obj == null)
            throw supplier.get();

        return obj;
    }

    public static <T> T
    requireNonNullElse(T obj,T defaultObj)
        throws NullPointerException
    {
        return Objects.requireNonNullElse(obj,defaultObj);
    }

    public static <T,E extends Throwable> T
    requireNonNullElse(T obj,T defaultObj,Supplier<E> supplier)
        throws NullPointerException, E
    {
        Objects.requireNonNull(supplier,"supplier is null");

        if (obj != null)
            return obj;

        return requireNonNull(defaultObj,supplier);
    }

    public static <T> T
    requireNonNullElseGet(T obj,Supplier<? extends T> supplier)
        throws NullPointerException
    {
        Objects.requireNonNull(supplier,"supplier is null");

        return Objects.requireNonNullElseGet(obj,supplier);
    }

    public static <T,E extends Throwable> T
    requireNonNullElseGet(
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
            requireNonNull(
                defaultSupplier.get(),
                exceptionSupplier);
    }
}

//////////////////////////////////////////////////////////////////////////////
