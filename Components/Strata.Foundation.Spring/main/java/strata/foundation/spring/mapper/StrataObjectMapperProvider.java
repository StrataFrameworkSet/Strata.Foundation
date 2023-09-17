//////////////////////////////////////////////////////////////////////////////
// strataObjectMapperProvider.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import strata.foundation.core.mapper.IObjectMapperCustomizer;
import strata.foundation.core.mapper.ObjectMapperCustomizer;

public
class StrataObjectMapperProvider
    extends ObjectMapperProvider
{
    private ObjectMapper jsonObjectMapper;
    private ObjectMapper yamlObjectMapper;

    private static IObjectMapperCustomizer customizer =
        new ObjectMapperCustomizer();

    public
    StrataObjectMapperProvider(SpringDocConfigProperties springDocConfigProperties)
    {
        super(springDocConfigProperties);

        jsonObjectMapper = customizer.customize(super.jsonMapper());
        yamlObjectMapper = customizer.customize(super.yamlMapper());
     }

    @Override
    public ObjectMapper
    jsonMapper()
    {
        return jsonObjectMapper;
    }

    @Override
    public ObjectMapper
    yamlMapper()
    {
        return yamlObjectMapper;
    }
}

//////////////////////////////////////////////////////////////////////////////
