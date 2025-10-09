//////////////////////////////////////////////////////////////////////////////
// PostalAddress.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import strata.foundation.core.utility.ICopyable;

import java.io.Serializable;
import java.util.Objects;

public
class PostalAddress
    implements ICopyable,Serializable,Comparable<PostalAddress>
{
    private String address;
    private String street;
    private String city;
    private String state;
    private String countryCode;
    private String postalCode;

    @JsonCreator
    public
    PostalAddress(
        @JsonProperty("address")     final String address,
        @JsonProperty("street")      final String street,
        @JsonProperty("city")        final String city,
        @JsonProperty("state")       final String state,
        @JsonProperty("countryCode") final String countryCode,
        @JsonProperty("postalCode")  final String postalCode)
    {
        this.address = Objects.toString(address,"");
        this.street = Objects.toString(street,"");
        this.city = Objects.toString(city,"");
        this.state = Objects.toString(state,"");
        this.countryCode = Objects.toString(countryCode,"");
        this.postalCode = Objects.toString(postalCode,"");
    }

    public
    PostalAddress()
    {
        this(null,null,null,null,null,null);
    }

    public
    PostalAddress(PostalAddress other)
    {
        this(
            other.address,
            other.street,
            other.city,
            other.state,
            other.countryCode,
            other.postalCode);
    }

    @Override
    public PostalAddress
    copy()
    {
        return new PostalAddress( this );
    }

    @Override
    public int
    compareTo(PostalAddress other)
    {
        int result =
            Objects.compare(
                postalCode,
                other.postalCode,
                String::compareToIgnoreCase );

        if ( result != 0 )
            return result;

        result =
            Objects.compare(
                countryCode,
                other.countryCode,
                String::compareToIgnoreCase);

        if ( result != 0 )
            return result;

        result =
            Objects.compare(
                state,
                other.state,
                String::compareToIgnoreCase );

        if ( result != 0 )
            return result;

        result =
            Objects.compare(
                city,
                other.city,
                String::compareToIgnoreCase);

        if ( result != 0 )
            return result;

        result =
            Objects.compare(
                street,
                other.street,
                String::compareToIgnoreCase);

        if ( result != 0 )
            return result;

        result =
            Objects.compare(
                address,
                other.address,
                String::compareToIgnoreCase);

        return result;
    }

    @Override
    public boolean
    equals(Object other)
    {
        if ( other instanceof PostalAddress )
            return compareTo( (PostalAddress)other ) == 0;

        return false;
    }

    @Override
    public int
    hashCode()
    {
        int hash = 31;

        hash = 31 * hash + address.hashCode();
        hash = 31 * hash + street.hashCode();
        hash = 31 * hash + city.hashCode();
        hash = 31 * hash + state.hashCode();
        hash = 31 * hash + countryCode.hashCode();
        hash = 31 * hash + postalCode.hashCode();

        return hash;
    }

    @Override
    public String
    toString()
    {
        StringBuilder builder = new StringBuilder();

        builder
            .append(address)
            .append( ' ' )
            .append(street)
            .append(  '\n' )
            .append(city)
            .append( ' ' )
            .append(state)
            .append( ' ' )
            .append(countryCode)
            .append(  ' ' )
            .append(postalCode);

        return builder.toString();
    }

    public String
    getAddress()
    {
        return address;
    }

    public String
    getStreet()
    {
        return street;
    }

    public String
    getCity()
    {
        return city;
    }

    public String
    getState()
    {
        return state;
    }

    public String
    getCountryCode()
    {
        return countryCode;
    }

    public String
    getPostalCode()
    {
        return postalCode;
    }
}

//////////////////////////////////////////////////////////////////////////////
