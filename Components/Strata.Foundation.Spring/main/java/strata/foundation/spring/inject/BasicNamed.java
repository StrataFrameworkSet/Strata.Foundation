/// ///////////////////////////////////////////////////////////////////////////
// BasicNamed.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.inject;

import jakarta.inject.Named;

import java.lang.annotation.Annotation;
import java.util.Objects;

public
class BasicNamed
    implements Named
{
    private final String value;

    public
    BasicNamed(String value)
    {
        Objects.requireNonNull(value,"Value cannot be null");
        this.value = value;
    }

    @Override
    public int
    hashCode()
    {
        return (127 * "value".hashCode()) ^ value.hashCode();
    }

    @Override
    public boolean
    equals(Object other)
    {
        if (other instanceof Named named)
            return Objects.equals(value,named.value());

        return false;
    }

    @Override
    public String
    toString()
    {
        return value;
    }

    @Override
    public Class<? extends Annotation>
    annotationType()
    {
        return Named.class;
    }

    @Override
    public String
    value()
    {
        return value;
    }
}

//////////////////////////////////////////////////////////////////////////////
