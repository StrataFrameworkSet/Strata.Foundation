//////////////////////////////////////////////////////////////////////////////
// IEnvironment.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.NoSuchElementException;

/**
 * <p>
 * Provides access to named environment variables. Implementations may
 * source values from the operating system environment, system properties,
 * a configuration file, or any other name/value store, allowing callers to
 * remain agnostic of where the values actually come from.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * IEnvironment environment = ...;
 *
 * String path = environment.get("PATH");
 * </pre>
 */
public
interface IEnvironment
{
    String
    get(String variableName) throws NoSuchElementException;
}

//////////////////////////////////////////////////////////////////////////////
