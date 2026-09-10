//////////////////////////////////////////////////////////////////////////////
// IPersonNameFormatter.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

/**
 * <p>
 * An {@link IFormatter} specialized for rendering a {@link PersonName} as a
 * {@link java.lang.String}. Implementations decide how the individual parts
 * of a name (title, first, middle, last, suffix) are combined and ordered
 * into the final formatted text.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * IPersonNameFormatter formatter = new BasicPersonNameFormatter();
 * String formatted = formatter.format(PersonName.of("Jane","Q.","Doe"));
 * </pre>
 */
public
interface IPersonNameFormatter
    extends IFormatter<PersonName,String> {}

//////////////////////////////////////////////////////////////////////////////
