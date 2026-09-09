//////////////////////////////////////////////////////////////////////////////
// IInjector.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.inject;

import strata.foundation.core.reflect.TypeLiteral;

import java.lang.annotation.Annotation;

/**
 * <p>
 * Abstraction over a
 * <a href="https://en.wikipedia.org/wiki/Dependency_injection">Dependency
 * injection</a> container, allowing instances to be resolved by type,
 * optionally qualified by an annotation, an annotation type, or a name.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * IInjector injector = ...;
 *
 * IConfiguration configuration = injector.getInstance(IConfiguration.class);
 * IConfiguration named =
 *     injector.getInstance(IConfiguration.class,"secondary");
 * </pre>
 * </p>
 */
public
interface IInjector
{
    <T>
    T
    getInstance(Class<T> type);

    <T>
    T
    getInstance(TypeLiteral<T> type);

    <T>
    T
    getInstance(Class<T> type,Class<? extends Annotation> annotationType);

    <T>
    T
    getInstance(TypeLiteral<T> type,Class<? extends Annotation> annotationType);

    <T>
    T
    getInstance(Class<T> type,Annotation annotation);

    <T>
    T
    getInstance(TypeLiteral<T> type,Annotation annotation);

    <T>
    T
    getInstance(Class<T> type,String name);

    <T>
    T
    getInstance(TypeLiteral<T> type,String name);
}

//////////////////////////////////////////////////////////////////////////////