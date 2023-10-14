//////////////////////////////////////////////////////////////////////////////
// PostalAddress.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import strata.foundation.core.utility.ICopyable;

import java.io.Serializable;

public
class PostalAddress
    implements ICopyable,Serializable,Comparable<PostalAddress>
{
    private String itsAddress;
    private String itsStreet;
    private String itsCity;
    private String itsState;
    private String itsCountryCode;
    private String itsPostalCode;

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
        itsAddress     = address;
        itsStreet      = street;
        itsCity        = city;
        itsState       = state;
        itsCountryCode = countryCode;
        itsPostalCode  = postalCode;
    }

    public
    PostalAddress(PostalAddress other)
    {
        this(
            other.itsAddress,
            other.itsStreet,
            other.itsCity,
            other.itsState,
            other.itsCountryCode,
            other.itsPostalCode );
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
            itsPostalCode.compareToIgnoreCase( other.itsPostalCode );

        if ( result != 0 )
            return result;

        result =
            itsCountryCode.compareToIgnoreCase( other.itsCountryCode );

        if ( result != 0 )
            return result;

        result =
            itsState.compareToIgnoreCase( other.itsState );

        if ( result != 0 )
            return result;

        result =
            itsCity.compareToIgnoreCase( other.itsCity );

        if ( result != 0 )
            return result;

        result =
            itsStreet.compareToIgnoreCase( other.itsStreet );

        if ( result != 0 )
            return result;

        result =
            itsAddress.compareToIgnoreCase( other.itsAddress );

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

        hash = 31 * hash + itsAddress.hashCode();
        hash = 31 * hash + itsStreet.hashCode();
        hash = 31 * hash + itsCity.hashCode();
        hash = 31 * hash + itsState.hashCode();
        hash = 31 * hash + itsCountryCode.hashCode();
        hash = 31 * hash + itsPostalCode.hashCode();

        return hash;
    }

    @Override
    public String
    toString()
    {
        StringBuilder builder = new StringBuilder();

        builder
            .append(  itsAddress )
            .append( ' ' )
            .append( itsStreet )
            .append(  '\n' )
            .append(  itsCity )
            .append( ' ' )
            .append( itsState )
            .append( ' ' )
            .append( itsCountryCode )
            .append(  ' ' )
            .append( itsPostalCode );

        return builder.toString();
    }

    public String
    getAddress()
    {
        return itsAddress;
    }

    public String
    getStreet()
    {
        return itsStreet;
    }

    public String
    getCity()
    {
        return itsCity;
    }

    public String
    getState()
    {
        return itsState;
    }

    public String
    getCountryCode()
    {
        return itsCountryCode;
    }

    public String
    getPostalCode()
    {
        return itsPostalCode;
    }
}

//////////////////////////////////////////////////////////////////////////////
