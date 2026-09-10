//////////////////////////////////////////////////////////////////////////////
// EnvironmentValueProvider.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.inject;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * <p>
 * Default implementation of {@link IEnvironmentValueProvider} that reads a
 * named environment variable and exposes its value as an
 * {@link java.util.Optional}, along with an exception to throw when the
 * variable is required but absent.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * IEnvironmentValueProvider provider =
 *     EnvironmentValueProvider.ofVariable("DEPLOY_ENV");
 *
 * String environment =
 *     provider.get().orElseThrow(provider.getException());
 * </pre>
 */
public
class EnvironmentValueProvider
    implements IEnvironmentValueProvider
{
    private final String variableName;

    public 
    EnvironmentValueProvider(String variableName)
    {
        this.variableName = variableName;
    }

    @Override
    public Optional<String>
    get()
    {
        return Optional.ofNullable(System.getenv(variableName));
    }
    
    @Override
    public Supplier<IllegalStateException>
    getException()
    {
        return
            () ->
                new IllegalStateException(
                    String.format(
                        "Environment variable '%s' is not set.",
                        variableName));
    }

    public static EnvironmentValueProvider
    ofVariable(String variableName)
    {
        return new EnvironmentValueProvider(variableName);
    }
}

//////////////////////////////////////////////////////////////////////////////
