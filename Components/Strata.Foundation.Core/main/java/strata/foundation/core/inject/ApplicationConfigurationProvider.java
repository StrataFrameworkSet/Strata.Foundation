//////////////////////////////////////////////////////////////////////////////
// ApplicationPropertiesProvider.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import strata.foundation.core.configuration.IConfiguration;
import strata.foundation.core.configuration.SecureConfiguration;

import com.google.inject.Provider;

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
