//////////////////////////////////////////////////////////////////////////////
// IOptionable.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * <p>
 * A {@link java.io.Serializable}, {@link java.util.function.Supplier}-based
 * alternative to {@link java.util.Optional} that may or may not hold a
 * present value. Provides operations for consuming, transforming, and
 * falling back on the contained value in a functional style, and can be
 * converted to a standard {@code Optional} when interoperability is needed.
 * </p>
 * <h4>Type Parameter</h4>
 * {@code <T>} - the type of value potentially held by this instance
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * IOptionable&lt;String&gt; optionable = ...;
 *
 * optionable.ifPresent(value -&gt; System.out.println(value));
 *
 * String result = optionable
 *     .map(String::toUpperCase)
 *     .orElse("DEFAULT");
 * </pre>
 * </p>
 */
public
interface IOptionable<T>
    extends Supplier<T>, Serializable
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
    ifEmpty(Runnable notPresent);

    IOptionable<T>
    filter(Predicate<T> predicate);

    <U> IOptionable<U>
    map(Function<T,U> mapper);

    <U,O extends IOptionable<U>> IOptionable<U>
    flatMap(Function<T,O> mapper);

    <O extends IOptionable<T>> IOptionable<T>
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