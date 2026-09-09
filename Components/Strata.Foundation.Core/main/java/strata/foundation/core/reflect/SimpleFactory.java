//////////////////////////////////////////////////////////////////////////////
// SimpleFactory.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.reflect;

import java.lang.reflect.Constructor;

/**
 * <p>
 * Concrete {@link AbstractFactory} that supports registering both default
 * and parameterized {@link Constructor Constructors} for a type.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * SimpleFactory factory = new SimpleFactory();
 * factory.insertDefaultConstructor(Widget.class);
 * Widget widget = factory.create(Widget.class);
 * </pre>
 * </p>
 */
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
