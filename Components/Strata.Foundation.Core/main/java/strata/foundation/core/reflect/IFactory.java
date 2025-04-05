//////////////////////////////////////////////////////////////////////////////
// IFactory.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.reflect;

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

    <T> boolean
    hasConstructorFor(Class<T> type);
}

//////////////////////////////////////////////////////////////////////////////
