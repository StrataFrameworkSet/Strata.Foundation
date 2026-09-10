//////////////////////////////////////////////////////////////////////////////
// ISecurityCodeGenerator.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

/**
 * <p>
 * Defines a generator that produces security codes on demand, such as
 * one-time passcodes used for verification or multi-factor authentication
 * flows. Implementations are free to choose the code format, length, and
 * randomness strategy, exposing only a single method for retrieving the
 * next generated code.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * ISecurityCodeGenerator generator = ...;
 * String code = generator.getNextSecurityCode();
 * </pre>
 */
public
interface ISecurityCodeGenerator
{
    String
    getNextSecurityCode();
}

//////////////////////////////////////////////////////////////////////////////
