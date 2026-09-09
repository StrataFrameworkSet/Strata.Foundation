//////////////////////////////////////////////////////////////////////////////
// Cookie.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.transfer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * <p>
 * An immutable name/value pair representing an HTTP cookie carried on a
 * service {@link AbstractServiceRequest request} or {@link
 * AbstractServiceReply reply}. Equality and {@link #hashCode()} are based on
 * both the {@link #getName() name} and the {@link #getValue() value}.
 * Instances are constructed either directly or, for JSON deserialization,
 * via the {@link com.fasterxml.jackson.annotation.JsonCreator}-annotated
 * constructor, and implement {@link Serializable} so cookies can cross
 * process or transport boundaries.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * Cookie sessionCookie = Cookie.of("SESSIONID","abc123");
 * request.addCookie(sessionCookie);
 * </pre>
 * </p>
 */
public
class Cookie
    implements Serializable
{
    private final String name;
    private final String value;

    @JsonCreator
    public
    Cookie(
        @JsonProperty("name")  String name,
        @JsonProperty("value") String value)
    {
        this.name  = name;
        this.value = value;
    }

    @Override
    public boolean
    equals(Object other)
    {
        return
            other instanceof Cookie that &&
            Objects.equals(getName(),that.getName()) &&
            Objects.equals(getValue(),that.getValue());
    }

    @Override
    public int
    hashCode()
    {
        return Objects.hash(getName(),getValue());
    }

    public String
    getName() { return name; }

    public String
    getValue() { return value; }

    public static Cookie
    of(String name,String value)
    {
        return new Cookie(name,value);
    }
}

//////////////////////////////////////////////////////////////////////////////
