//////////////////////////////////////////////////////////////////////////////
// UsPostalAddressFormatter.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import strata.foundation.core.utility.Expendable;

/**
 * <p>
 * An {@link IPostalAddressFormatter} that renders a {@link PostalAddress}
 * according to <a href="https://en.wikipedia.org/wiki/Address#United_States">
 * United States postal addressing conventions</a>: an optional addressee
 * name on its own line, followed by the street address, and then the city,
 * state, and postal code on a final line. The addressee is optional and may
 * be set at construction time or later via {@link #setAddressee(String)}.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * PostalAddress address = new PostalAddress("123","Main St","Anytown","CA","US","90210");
 *
 * UsPostalAddressFormatter formatter = new UsPostalAddressFormatter("Jane Doe");
 * String formatted = formatter.format(address);
 * // "Jane Doe\n123 Main St\nAnytown, CA 90210"
 * </pre>
 */
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
