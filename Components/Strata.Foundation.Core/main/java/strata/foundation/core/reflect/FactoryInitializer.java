//////////////////////////////////////////////////////////////////////////////
// FactoryInitializer.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.reflect;

import java.lang.reflect.Constructor;

/**
 * <p>
 * Registers a {@link Constructor} for a given type with the process-wide
 * {@link IFactory} obtained from {@link SingletonFactorySupplier}, typically
 * invoked for its side effect during static initialization.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * new FactoryInitializer&lt;&gt;(Widget.class, Widget.class.getConstructor());
 * </pre>
 *
 * @param <T> the type whose constructor is being registered
 */
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
