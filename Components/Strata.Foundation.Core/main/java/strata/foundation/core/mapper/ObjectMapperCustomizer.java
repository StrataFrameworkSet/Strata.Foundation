//////////////////////////////////////////////////////////////////////////////
// ObjectMapperCustomizer.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.avro.specific.SpecificRecordBase;

public
class ObjectMapperCustomizer
    implements IObjectMapperCustomizer
{
    @Override
    public ObjectMapper
    customize(ObjectMapper mapper)
    {
        return
            mapper
                .enable(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS)
                .enable(MapperFeature.ALLOW_EXPLICIT_PROPERTY_RENAMING)
                .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
                .enable(SerializationFeature.EAGER_SERIALIZER_FETCH)
                .enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(new SimpleModule())
                .registerModule(new JavaTimeModule())
                .registerModule(new Jdk8Module())
                .addMixIn(SpecificRecordBase.class,IExcludeAvroFieldsMixin.class);
    }
}

//////////////////////////////////////////////////////////////////////////////
