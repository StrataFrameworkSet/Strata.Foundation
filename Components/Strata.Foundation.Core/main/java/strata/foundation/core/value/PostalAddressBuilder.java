//////////////////////////////////////////////////////////////////////////////
// PostalAddressBuilder.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

public
class PostalAddressBuilder
{
    private String itsAddress;
    private String itsStreet;
    private String itsCity;
    private String itsState;
    private String itsCountryCode;
    private String itsPostalCode;

    public
    PostalAddressBuilder()
    {
        itsAddress     = null;
        itsStreet      = null;
        itsCity        = null;
        itsState       = null;
        itsCountryCode = null;
        itsPostalCode  = null;
    }

    public PostalAddressBuilder
    setAddress(String address)
    {
        itsAddress = address;
        return this;
    }

    public PostalAddressBuilder
    setStreet(String street)
    {
        itsStreet = street;
        return this;
    }

    public PostalAddressBuilder
    setCity(String city)
    {
        itsCity = city;
        return this;
    }

    public PostalAddressBuilder
    setState(String state)
    {
        itsState = state;
        return this;
    }

    public PostalAddressBuilder
    setCountryCode(String countryCode)
    {
        itsCountryCode = countryCode;
        return this;
    }

    public PostalAddressBuilder
    setPostalCode(String postalCode)
    {
        itsPostalCode = postalCode;
        return this;
    }

    public PostalAddress
    build()
    {
        return
            new PostalAddress(
                itsAddress,
                itsStreet,
                itsCity,
                itsState,
                itsCountryCode,
                itsPostalCode );
    }
}

//////////////////////////////////////////////////////////////////////////////
