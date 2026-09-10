//////////////////////////////////////////////////////////////////////////////
// IFormatter.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

/**
 * <p>
 * A strategy for converting a value into a formatted representation of that
 * value, such as rendering a structured value object (for example, a person's
 * name or a postal address) into a human-readable {@link java.lang.String}.
 * Implementations encapsulate a single, reusable formatting rule so that the
 * rule can be swapped, composed, or configured independently of the value
 * being formatted.
 * </p>
 * <br/>
 * <b>Type Parameters</b><br/>
 * <ul>
 * <li>{@code <V>} - the type of the value being formatted</li>
 * <li>{@code <O>} - the type of the output produced by formatting</li>
 * </ul>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * IFormatter&lt;PersonName,String&gt; formatter =
 *     personName -&gt; personName.getFirstName() + " " + personName.getLastName();
 *
 * String formatted = formatter.format(PersonName.of("Jane","Doe"));
 * </pre>
 */
public
interface IFormatter<V,O>
{
    O
    format(V value);
}

//////////////////////////////////////////////////////////////////////////////
