//////////////////////////////////////////////////////////////////////////////
// strataModelResolver.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.core.jackson.ModelResolver;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.context.annotation.Configuration;
import strata.foundation.core.mapper.IExcludeAvroFieldsMixin;

@Configuration
public
class StrataModelResolver
    extends ModelResolver
{
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public
    StrataModelResolver(ObjectMapper mapper)
    {
        super(mapper);
        super
            .objectMapper()
            .registerModule(new SimpleModule())
            .registerModule(new JavaTimeModule())
            .registerModule(new Jdk8Module())
            .addMixIn(SpecificRecordBase.class,IExcludeAvroFieldsMixin.class)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }
}

//////////////////////////////////////////////////////////////////////////////
