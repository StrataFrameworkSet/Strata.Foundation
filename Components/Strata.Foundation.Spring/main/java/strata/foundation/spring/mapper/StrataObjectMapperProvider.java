//////////////////////////////////////////////////////////////////////////////
// strataObjectMapperProvider.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.avro.specific.SpecificRecordBase;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import strata.foundation.core.mapper.IExcludeAvroFieldsMixin;

public
class StrataObjectMapperProvider
    extends ObjectMapperProvider
{
    private ObjectMapper jsonObjectMapper;
    private ObjectMapper yamlObjectMapper;

    public
    StrataObjectMapperProvider(SpringDocConfigProperties springDocConfigProperties)
    {
        super(springDocConfigProperties);
        jsonObjectMapper = super.jsonMapper();
        yamlObjectMapper = super.yamlMapper();

        jsonObjectMapper
            .registerModule(new SimpleModule())
            .registerModule(new JavaTimeModule())
            .registerModule(new Jdk8Module())
            .addMixIn(SpecificRecordBase.class,IExcludeAvroFieldsMixin.class)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        yamlObjectMapper
            .registerModule(new SimpleModule())
            .registerModule(new JavaTimeModule())
            .registerModule(new Jdk8Module())
            .addMixIn(SpecificRecordBase.class,IExcludeAvroFieldsMixin.class)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
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
