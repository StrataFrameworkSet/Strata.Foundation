//////////////////////////////////////////////////////////////////////////////
// ApplicationConfigurationProvider.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.inject;

import jakarta.inject.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import strata.foundation.core.configuration.IConfiguration;
import strata.foundation.core.configuration.SecureConfiguration;


/**
 * <p>
 * A {@link jakarta.inject.Provider} of {@link strata.foundation.core.configuration.IConfiguration}
 * that loads a {@code &lt;environment&gt;.properties} resource selected by the
 * {@code DEPLOY_ENV} environment variable, falling back to a configured
 * default environment when it is not set.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * Provider&lt;IConfiguration&gt; provider =
 *     new ApplicationConfigurationProvider("development");
 *
 * IConfiguration configuration = provider.get();
 * </pre>
 * </p>
 */
public
class ApplicationConfigurationProvider
    implements Provider<IConfiguration>
{
    private final Logger itsLogger =
        LogManager.getLogger(ApplicationConfigurationProvider.class);

    private final String defaultEnvironment;

    public
    ApplicationConfigurationProvider()
    {
        this("development");
    }

    public
    ApplicationConfigurationProvider(String defaultEnv)
    {
        defaultEnvironment = defaultEnv;
    }

    @Override
    public IConfiguration
    get()
    {
        String environment = getEnvironment();

        itsLogger.debug("DEPLOY_ENV = " + environment);
        itsLogger.debug("Loading " + environment + ".properties");

        try
        {
            return
                new SecureConfiguration(
                    ClassLoader.getSystemResourceAsStream(
                        getEnvironment() + ".properties"));
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private String
    getEnvironment()
    {
        String deployEnv = System.getenv("DEPLOY_ENV");

        return
            deployEnv != null
                ? deployEnv
                : defaultEnvironment;
    }
}

//////////////////////////////////////////////////////////////////////////////
