//////////////////////////////////////////////////////////////////////////////
// PostalCode.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import strata.foundation.core.mapper.BasicPostalCodeToCountryCodeMapper;
import strata.foundation.core.mapper.IPostalCodeToCountryCodeMapper;
import strata.foundation.core.utility.ICopyable;

import java.io.Serializable;
import java.util.Objects;

public
class PostalCode
    implements ICopyable, Serializable, Comparable<PostalCode>
{
    private final String value;

    private static final IPostalCodeToCountryCodeMapper mapper =
        new BasicPostalCodeToCountryCodeMapper();

    @JsonCreator
    public
    PostalCode(@JsonProperty("postalCode") String value)
        throws NullPointerException, IllegalArgumentException
    {
        Objects.requireNonNull(value,"postalCode cannot be null");
        validate(value);
        this.value = value;
    }

    @Override
    public int
    hashCode()
    {
        return Objects.hashCode(value.toUpperCase());
    }

    @Override
    public boolean
    equals(Object other)
    {
        return
            other instanceof PostalCode postalCode &&
            value.equalsIgnoreCase(postalCode.value);
    }

    @Override
    protected Object
    clone()
    {
        return copy();
    }

    @Override
    public PostalCode
    copy()
    {
        return new PostalCode(value);
    }

    @Override
    public int
    compareTo(PostalCode other)
    {
        return value.compareToIgnoreCase(other.value);
    }

    @Override
    @JsonProperty("postalCode")
    public String
    toString()
    {
        return value;
    }

    public static PostalCode
    of(String postalCode)
        throws NullPointerException,IllegalArgumentException
    {
        return new PostalCode(postalCode);
    }

    private static void
    validate(String postalCode)
        throws IllegalArgumentException
    {
        if (mapper.map(postalCode).isEmpty())
            throw
                new IllegalArgumentException(
                    "Unrecognized postal code format: " + postalCode);
    }

}

//////////////////////////////////////////////////////////////////////////////
