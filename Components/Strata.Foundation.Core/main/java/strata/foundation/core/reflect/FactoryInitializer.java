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
 * <h4>Type Parameter</h4>
 * {@code <T>} - the type whose constructor is being registered
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * new FactoryInitializer&lt;&gt;(Widget.class, Widget.class.getConstructor());
 * </pre>
 * </p>
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
