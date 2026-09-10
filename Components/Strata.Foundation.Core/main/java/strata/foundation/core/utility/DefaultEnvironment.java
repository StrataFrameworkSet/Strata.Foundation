//////////////////////////////////////////////////////////////////////////////
// DefaultEnvironment.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * <p>
 * Default {@link IEnvironment} implementation that resolves variables from
 * the operating system environment via {@link System#getenv(String)},
 * falling back to a configurable set of default values when a variable is
 * not defined in the environment.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * IEnvironment environment =
 *     new DefaultEnvironment(Map.of("PORT","8080"));
 *
 * String port = environment.get("PORT");
 * </pre>
 */
public
class DefaultEnvironment
    implements IEnvironment
{
    private Map<String,String> defaults;

    public
    DefaultEnvironment()
    {
        this(Map.of());
    }

    public
    DefaultEnvironment(Map<String,String> defaults)
    {
        this.defaults = new HashMap<>(defaults);
    }

    @Override
    public String
    get(String variableName)
        throws NoSuchElementException
    {
        String value = System.getenv(variableName);

        if (Objects.nonNull(value))
            return value;

        if (defaults.containsKey(variableName))
            return defaults.get(variableName);

        throw new NoSuchElementException(variableName);
    }

    public DefaultEnvironment
    setDefaults(Map<String,String> defaults)
    {
        this.defaults = new HashMap<>(defaults);
        return this;
    }

    public DefaultEnvironment
    setDefault(String variableName,String value)
    {
        defaults.put(variableName,value);
        return this;
    }

    public DefaultEnvironment
    clearDefault(String variableName)
    {
        defaults.remove(variableName);
        return this;
    }

    public DefaultEnvironment
    clearDefaults()
    {
        defaults.clear();
        return this;
    }

    public static DefaultEnvironment
    of(Map<String,String> defaults)
    {
        return new DefaultEnvironment(defaults);
    }
}

//////////////////////////////////////////////////////////////////////////////
