//////////////////////////////////////////////////////////////////////////////
// ScopeConfiguration.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.inject;

import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public
class ScopeConfiguration
{
    @Bean
    public static CustomScopeConfigurer
    strataFoundationScopeConfigurer()
    {
        CustomScopeConfigurer configurer = new CustomScopeConfigurer();

        configurer.addScope("thread", new ThreadScope());
        configurer.addScope("operation", new OperationScope());

        return configurer;
    }
}

//////////////////////////////////////////////////////////////////////////////
