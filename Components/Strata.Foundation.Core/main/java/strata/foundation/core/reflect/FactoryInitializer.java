/// ///////////////////////////////////////////////////////////////////////////
// FactoryInitializer.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.reflect;

import java.lang.reflect.Constructor;

public
class FactoryInitializer<T>
{
    public
    FactoryInitializer(Class<T> type,Constructor<? extends T> constructor)
    {
        IFactory factory = new SingletonFactorySupplier().get();

        try
        {
            System.out.println("Initializing " + type);
            factory.insertConstructor(type,constructor);
        }
        catch (NoSuchMethodException e)
        {
            throw new RuntimeException(e);
        }
    }
}

//////////////////////////////////////////////////////////////////////////////
