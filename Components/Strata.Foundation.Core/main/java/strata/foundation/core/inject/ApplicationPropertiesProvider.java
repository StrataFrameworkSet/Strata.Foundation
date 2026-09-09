//////////////////////////////////////////////////////////////////////////////
// ApplicationPropertiesProvider.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.inject;

import jakarta.inject.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

/**
 * <p>
 * A {@link jakarta.inject.Provider} of {@link java.util.Properties} that
 * loads a {@code &lt;environment&gt;.properties} resource selected by the
 * {@code DEPLOY_ENV} environment variable, falling back to
 * {@code "development"} when it is not set.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * Provider&lt;Properties&gt; provider = new ApplicationPropertiesProvider();
 *
 * Properties properties = provider.get();
 * </pre>
 * </p>
 */
public
class ApplicationPropertiesProvider
    implements Provider<Properties>
{
    private final Logger itsLogger =
        LogManager.getLogger(ApplicationPropertiesProvider.class);

    @Override
    public Properties
    get()
    {
        Properties properties = new Properties();
        String     environment = getEnvironment();

        itsLogger.debug("DEPLOY_ENV = " + environment);
        itsLogger.debug("Loading " + environment + ".properties");

        try
        {
            properties.load(
                ClassLoader.getSystemResourceAsStream(
                    getEnvironment() + ".properties"));

            return properties;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private static String
    getEnvironment()
    {
        return
            EnvironmentValueProvider
                .ofVariable("DEPLOY_ENV")
                .get()
                .orElse("development");
    }
}

//////////////////////////////////////////////////////////////////////////////
