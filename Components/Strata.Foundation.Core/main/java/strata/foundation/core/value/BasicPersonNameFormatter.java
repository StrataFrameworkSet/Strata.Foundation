//////////////////////////////////////////////////////////////////////////////
// BasicPersonNameFormatter.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

/**
 * <p>
 * A default {@link IPersonNameFormatter} implementation that simply defers
 * to {@link PersonName#toString()}, producing a formatted name such as
 * {@code "Dr. Jane Q. Doe, Jr."} depending on which parts of the name are
 * present.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * IPersonNameFormatter formatter = new BasicPersonNameFormatter();
 * String formatted = formatter.format(PersonName.of("Jane","Doe"));
 * // formatted == "Jane Doe"
 * </pre>
 */
public
class BasicPersonNameFormatter
    implements IPersonNameFormatter
{
    public
    BasicPersonNameFormatter() {}

    @Override
    public String
    format(PersonName personName)
    {
        return personName.toString();
    }
}

//////////////////////////////////////////////////////////////////////////////
