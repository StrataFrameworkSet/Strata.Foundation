//////////////////////////////////////////////////////////////////////////////
// IOptional.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public
interface IOptional<T>
    extends Supplier<T>
{
    void
    ifPresent(Consumer<T> consumer);

    <U> U
    ifPresentOrElse(Function<T,U> present,Supplier<U> notPresent);

    <U,E extends RuntimeException> U
    ifPresentOrThrow(Function<T,U> present,E exception);

    void
    ifPresentOrElseNoReturn(Consumer<T> present,Runnable notPresent);

    <E extends RuntimeException> void
    ifPresentOrThrowNoReturn(Consumer<T> present,E exception) throws E;

    void
    ifNotPresent(Runnable notPresent);

    IOptional<T>
    filter(Predicate<T> predicate);

    <U> IOptional<U>
    map(Function<T,U> mapper);

    <U,O extends IOptional<U>> IOptional<U>
    flatMap(Function<T,O> mapper);

    <O extends IOptional<T>> IOptional<T>
    or(Supplier<O> supplier);

    T
    orElse(T other);

    T
    orElseGet(Supplier<T> other);

    T
    orElseThrow() throws NoSuchElementException;

    <E extends RuntimeException> T
    orElseThrow(Supplier<E> exceptionSupplier) throws E;

    boolean
    isPresent();

    boolean
    isEmpty();

    Optional<T>
    toOptional();

}

//////////////////////////////////////////////////////////////////////////////