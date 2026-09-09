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
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * ISecurityCodeGenerator generator = ...;
 * String code = generator.getNextSecurityCode();
 * </pre>
 * </p>
 */
public
interface ISecurityCodeGenerator
{
    String
    getNextSecurityCode();
}

//////////////////////////////////////////////////////////////////////////////