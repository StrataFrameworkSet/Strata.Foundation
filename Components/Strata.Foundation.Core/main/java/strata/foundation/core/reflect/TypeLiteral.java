//////////////////////////////////////////////////////////////////////////////
// TypeLiteral.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.reflect;

import java.io.Serializable;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * <p>
 * Captures a fully reified generic type at runtime, working around
 * <a href="https://en.wikipedia.org/wiki/Type_erasure">type erasure</a> by
 * requiring subclasses to specify the type parameter {@code T} in an
 * anonymous subclass declaration.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * TypeLiteral&lt;List&lt;String&gt;&gt; literal = new TypeLiteral&lt;List&lt;String&gt;&gt;() {};
 * Type type = literal.getType();
 * </pre>
 *
 * @param <T> the type captured by this literal
 */
public abstract
class TypeLiteral<T>
    implements Serializable
{
    private final Type subjectType;

    protected
    TypeLiteral()
    {
        subjectType = getSubjectType(getClass());
    }

    @Override
    public boolean
    equals(Object other)
    {
        if (other instanceof TypeLiteral<?> literal)
            return this.getType().equals(literal.getType());

        return false;
    }

    @Override
    public int
    hashCode()
    {
        return getType().hashCode();
    }

    @Override
    public String
    toString()
    {
        return getType().getTypeName();
    }

    public Type
    getType()
    {
        return subjectType;
    }

    @SuppressWarnings("unchecked")
    public final Class<T>
    getRawType()
    {
        Type type = getType();

        if (type instanceof Class)
            return (Class<T>)type;
        else if (type instanceof ParameterizedType)
            return (Class<T>)((ParameterizedType)type).getRawType();
        else if (type instanceof GenericArrayType)
            return (Class<T>)Object[].class;
        else
            throw new RuntimeException("Illegal type");

    }

    private static Type
    getSubjectType(Class<?> type)
    {
        Type     subjectType = null;
        Class<?> typeLiteralSubclass = getTypeLiteralSubclass(type);

        if (typeLiteralSubclass == null)
            throw new RuntimeException(type + " is not a subclass of TypeLiteral");

        subjectType = getTypeParameter(typeLiteralSubclass);

        if (subjectType == null)
            throw new RuntimeException(type + " does not specify the type parameter T of TypeLiteral<T>");

        return subjectType;
    }

    private static Class<?>
    getTypeLiteralSubclass(Class<?> type)
    {
        Class<?> superclass = type.getSuperclass();

        if (superclass.equals(TypeLiteral.class))
            return type;
        else if (superclass.equals(Object.class))
            return null;
        else
            return getTypeLiteralSubclass(superclass);
    }

    private static Type
    getTypeParameter(Class<?> superclass)
    {
        Type type = superclass.getGenericSuperclass();

        if (type instanceof ParameterizedType parameterizedType)
            if (parameterizedType.getActualTypeArguments().length == 1)
                return parameterizedType.getActualTypeArguments()[0];

        return null;
    }
}

//////////////////////////////////////////////////////////////////////////////
