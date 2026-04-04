/// ///////////////////////////////////////////////////////////////////////////
// EnvironmentValueProvider.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.inject;

import java.util.Optional;
import java.util.function.Supplier;

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
