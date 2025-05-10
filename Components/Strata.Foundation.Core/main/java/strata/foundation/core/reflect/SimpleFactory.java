/// ///////////////////////////////////////////////////////////////////////////
// SimpleFactory.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.reflect;

import java.lang.reflect.Constructor;

public
class SimpleFactory
    extends AbstractFactory
{
    public
    SimpleFactory() { super(); }

    @Override
    public <T> SimpleFactory
    insertDefaultConstructor(Class<T> type)
        throws NoSuchMethodException
    {
        Constructor<T> constructor = type.getConstructor(new Class<?>[0]);

        insertConstructor(type, constructor);
        return this;
    }

    @Override
    public <T> SimpleFactory
    insertParameterizedConstructor(Class<T> type,Class<?>... parameterTypes)
        throws NoSuchMethodException
    {
        Constructor<T> constructor = type.getConstructor(parameterTypes);

        insertConstructor(type,constructor);
        return this;
    }
}

//////////////////////////////////////////////////////////////////////////////
