//////////////////////////////////////////////////////////////////////////////
// IPostalAddressFormatter.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

/**
 * <p>
 * An {@link IFormatter} specialized for rendering a {@link PostalAddress} as
 * a {@link java.lang.String}. Implementations decide how the individual
 * address components (street address, city, state, postal code, and so on)
 * are combined and laid out into the final formatted text, typically
 * following the conventions of a particular country's
 * <a href="https://en.wikipedia.org/wiki/Address">postal addressing
 * format</a>.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * IPostalAddressFormatter formatter = new UsPostalAddressFormatter("Jane Doe");
 * String formatted = formatter.format(address);
 * </pre>
 * </p>
 */
public
interface IPostalAddressFormatter
    extends IFormatter<PostalAddress,String> {}

//////////////////////////////////////////////////////////////////////////////