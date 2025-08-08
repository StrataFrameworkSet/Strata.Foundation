/// ///////////////////////////////////////////////////////////////////////////
// UsPostalAddressFormatter.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import strata.foundation.core.utility.Expendable;

public
class UsPostalAddressFormatter
    implements IPostalAddressFormatter
{
    private Expendable<String> addressee;

    public UsPostalAddressFormatter()
    {
        this(null);
    }

    public
    UsPostalAddressFormatter(String addressee)
    {
        this.addressee = Expendable.of(addressee);
    }

    @Override
    public String
    format(PostalAddress value)
    {
        return
            addressee
                .ifPresentOrElse(
                    addressee ->
                        String.format(
                            "%s\n%s\n%s, %s %s",
                            addressee,
                            getStreetAddress(value.getAddress(),value.getStreet()),
                            value.getCity(),
                            value.getState(),
                            value.getPostalCode()),
                    () ->
                    String.format(
                            "%s\n%s, %s %s",
                            getStreetAddress(value.getAddress(),value.getStreet()),
                            value.getCity(),
                            value.getState(),
                            value.getPostalCode()));
    }

    public UsPostalAddressFormatter
    setAddressee(String addressee)
    {
        this.addressee = Expendable.of(addressee);
        return this;
    }

    private String
    getStreetAddress(String address, String street)
    {
        StringBuilder builder = new StringBuilder();

        if (address != null && !address.isEmpty())
        {
            builder.append(address);

            if (street != null && !street.isEmpty())
                builder
                    .append(" ")
                    .append(street);

            return builder.toString();
        }

        return street;
    }
}

//////////////////////////////////////////////////////////////////////////////
