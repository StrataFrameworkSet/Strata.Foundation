//////////////////////////////////////////////////////////////////////////////
// IFactory.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public
interface IFactory
{
    <T> T
    create(Class<T> type)
        throws InvocationTargetException, InstantiationException, IllegalAccessException;

    <T> T
    create(Class<T> type,Object... parameters)
        throws InvocationTargetException, InstantiationException, IllegalAccessException;

    <T> IFactory
    insertConstructor(Class<T> type,Constructor<? extends T> constructor)
        throws NoSuchMethodException;

    <T> IFactory
    insertDefaultConstructor(Class<T> type)
        throws NoSuchMethodException;

    <T> IFactory
    insertParameterizedConstructor(Class<T> type,Class<?>... parameterTypes)
        throws NoSuchMethodException;

    <T> boolean
    hasConstructorFor(Class<T> type);
}

//////////////////////////////////////////////////////////////////////////////
