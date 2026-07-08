//////////////////////////////////////////////////////////////////////////////
// ApplicationPropertiesProvider.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.inject;

import jakarta.inject.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import strata.foundation.core.configuration.IConfiguration;
import strata.foundation.core.configuration.SecureConfiguration;

import java.io.InputStream;
import java.util.Objects;


public
class SpringConfigurationProvider
    implements Provider<IConfiguration>
{
    private final String defaultProfile;
    private final boolean requireProfileProperties;
    private final Logger logger;

    public
    SpringConfigurationProvider()
    {
        this("development",true);
    }

    public
    SpringConfigurationProvider(String defaultProfile,boolean requireProfileProperties)
    {
        this.defaultProfile = defaultProfile;
        this.requireProfileProperties = requireProfileProperties;
        logger = LogManager.getLogger(SpringConfigurationProvider.class);
    }

    @Override
    public IConfiguration
    get()
    {
        String      profile = getProfile();
        String      profileFileName = "application-" + profile + ".properties";
        InputStream base =
            ClassLoader.getSystemResourceAsStream("application.properties");
        InputStream profileSpecific =
            ClassLoader.getSystemResourceAsStream(profileFileName);

        if (Objects.isNull(base))
            throw new RuntimeException("application.properties not found on classpath");

        logger.debug("DEPLOY_ENV = {}",profile);
        logger.debug("Loading application.properties");

        if (Objects.nonNull(profileSpecific))
            logger.debug("Loading {}",profileFileName);
        else if (requireProfileProperties)
            throw new RuntimeException(profileFileName + " not found on classpath");
        else
            logger.debug("{} not found, skipping",profileFileName);

        try
        {
            return new SecureConfiguration(base,profileSpecific);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private String
    getProfile()
    {
        String deployProfile = System.getenv("DEPLOY_ENV");

        return
            deployProfile != null
                ? deployProfile
                : defaultProfile;
    }
}

//////////////////////////////////////////////////////////////////////////////
