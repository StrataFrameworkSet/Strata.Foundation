/// ///////////////////////////////////////////////////////////////////////////
// BasicQualifier.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.inject;


import org.springframework.beans.factory.annotation.Qualifier;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Objects;

class BasicQualifier
    implements Qualifier, Serializable
{
    private final String value;

    public
    BasicQualifier(String value)
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
        if (other instanceof Qualifier qualifier)
            return Objects.equals(value,qualifier.value());

        return false;
    }

    @Override
    public String toString()
    {
        return value;
    }

    @Override
    public Class<? extends Annotation>
    annotationType()
    {
        return Qualifier.class;
    }

    @Override
    public String
    value()
    {
        return value;
    }
}

//////////////////////////////////////////////////////////////////////////////
