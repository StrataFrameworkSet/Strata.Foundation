//////////////////////////////////////////////////////////////////////////////
// strataModelResolver.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import org.springframework.context.annotation.Configuration;
import strata.foundation.core.mapper.IObjectMapperCustomizer;
import strata.foundation.core.mapper.ObjectMapperCustomizer;

@Configuration
public
class StrataModelResolver
    extends ModelResolver
{
    private static IObjectMapperCustomizer customizer =
        new ObjectMapperCustomizer();

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public
    StrataModelResolver(ObjectMapper mapper)
    {
        super(mapper);
        customizer.customize(super.objectMapper());

    }
}

//////////////////////////////////////////////////////////////////////////////
