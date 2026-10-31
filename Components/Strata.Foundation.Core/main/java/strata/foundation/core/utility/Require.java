//////////////////////////////////////////////////////////////////////////////
// Require.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.Objects;
import java.util.function.Supplier;

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
