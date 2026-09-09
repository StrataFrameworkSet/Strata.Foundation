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

/**
 * <p>
 * An immutable value type representing a validated postal code (also known
 * as a ZIP code). See
 * <a href="https://en.wikipedia.org/wiki/Postal_code">Postal code</a> for
 * background on the format variations this type must accommodate across
 * countries. On construction, the supplied value is checked against an
 * {@link strata.foundation.core.mapper.IPostalCodeToCountryCodeMapper},
 * rejecting formats that cannot be mapped to a known country code.
 * </p>
 * <p>
 * Equality, hashing, and ordering are all case-insensitive over the
 * underlying string value. Copies are produced via {@link
 * ICopyable#copy()}, and instances implement {@link java.io.Serializable}
 * and {@link java.lang.Comparable}.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * PostalCode postalCode = PostalCode.of("62704");
 *
 * PostalCode copy = postalCode.copy();
 *
 * boolean same = postalCode.equals(copy);
 * int order = postalCode.compareTo(PostalCode.of("90210"));
 * </pre>
 * </p>
 */
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
