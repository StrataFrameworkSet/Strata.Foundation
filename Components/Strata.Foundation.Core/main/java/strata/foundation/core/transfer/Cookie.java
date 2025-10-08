//////////////////////////////////////////////////////////////////////////////
// Cookie.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.transfer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

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
