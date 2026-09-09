//////////////////////////////////////////////////////////////////////////////
// IPasswordGenerator.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

/**
 * <p>
 * Defines a generator that produces passwords on demand. Implementations
 * are free to apply whatever character sets, length constraints, or
 * randomness strategies are appropriate, exposing only a single method
 * for retrieving the next generated password.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * IPasswordGenerator generator = ...;
 * String password = generator.getNextPassword();
 * </pre>
 * </p>
 */
public
interface IPasswordGenerator
{
    String
    getNextPassword();
}

//////////////////////////////////////////////////////////////////////////////
