//////////////////////////////////////////////////////////////////////////////
// PropertiesModule.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.inject;

import com.google.inject.Scopes;
import strata.foundation.core.configuration.IConfiguration;

import java.util.Properties;

public
class PropertiesModule
    extends AbstractModule
{
    @Override
    protected void
    configure()
    {
        bind(Properties.class)
            .toProvider(ApplicationPropertiesProvider.class)
            .in(Scopes.SINGLETON);

        bind(IConfiguration.class)
            .toProvider(ApplicationConfigurationProvider.class)
            .in(Scopes.SINGLETON);
    }
}

//////////////////////////////////////////////////////////////////////////////
