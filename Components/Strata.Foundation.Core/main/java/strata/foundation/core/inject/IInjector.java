/// ///////////////////////////////////////////////////////////////////////////
// IInjector.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.inject;

import strata.foundation.core.reflect.TypeLiteral;

import java.lang.annotation.Annotation;

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