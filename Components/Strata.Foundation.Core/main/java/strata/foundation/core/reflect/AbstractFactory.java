/// ///////////////////////////////////////////////////////////////////////////
// AbstractFactory.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public
class AbstractFactory
    implements IFactory
{
    private final Map<Class<?>,Constructor<?>> constructors;

    protected
    AbstractFactory()
    {
        constructors = new HashMap<>();
    }

    @Override
    public <T> T
    create(Class<T> type)
        throws InvocationTargetException,InstantiationException,IllegalAccessException
    {
        return create(type,new Object[0]);
    }

    @Override
    public <T> T
    create(Class<T> type,Object... parameters)
        throws InvocationTargetException,InstantiationException,IllegalAccessException
    {
        if (hasConstructorFor(type))
        {
            Constructor<?> constructor = constructors.get(type);

            return type.cast(constructor.newInstance(parameters));
        }

        throw new InstantiationException("No suitable constructor found for " + type);
    }

    @Override
    public <T> boolean
    hasConstructorFor(Class<T> type)
    {
        return constructors.containsKey(type);
    }

    protected <T> void
    insertConstructorFor(Class<T> type,Constructor<? extends T> constructor)
    {
        Class<? extends T> declaringClass = constructor.getDeclaringClass();

        if (type.isAssignableFrom(declaringClass))
            constructors.put(type,constructor);
        else
            throw
                new ClassCastException(
                    declaringClass.getName() + " is not assignable to " + type.getName());
    }
}

//////////////////////////////////////////////////////////////////////////////
