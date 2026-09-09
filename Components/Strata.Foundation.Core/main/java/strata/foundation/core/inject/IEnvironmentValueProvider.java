//////////////////////////////////////////////////////////////////////////////
// IEnvironmentValueProvider.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.inject;

import jakarta.inject.Provider;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * <p>
 * A {@link jakarta.inject.Provider} of {@link java.util.Optional} that
 * resolves a value which may or may not be present, and supplies an
 * exception to throw when a caller requires the value but it is absent.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * IEnvironmentValueProvider provider =
 *     EnvironmentValueProvider.ofVariable("DEPLOY_ENV");
 *
 * String environment =
 *     provider.get().orElseThrow(provider.getException());
 * </pre>
 * </p>
 */
public
interface IEnvironmentValueProvider
    extends Provider<Optional<String>> 
{
    Supplier<? extends RuntimeException>
    getException();
}

//////////////////////////////////////////////////////////////////////////////