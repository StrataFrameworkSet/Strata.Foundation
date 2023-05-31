//////////////////////////////////////////////////////////////////////////////
// ObjectMapperResolver.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.transfer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public
class ObjectMapperResolver
    implements ContextResolver<ObjectMapper>
{
    private final ObjectMapper itsMapper;

    public
    ObjectMapperResolver()
    {
        itsMapper = new ObjectMapper();
        itsMapper
            .enable( MapperFeature.REQUIRE_SETTERS_FOR_GETTERS )
            .enable( MapperFeature.ALLOW_EXPLICIT_PROPERTY_RENAMING )
            .enable( MapperFeature.SORT_PROPERTIES_ALPHABETICALLY )
            .enable( DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES )
            .enable( SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS )
            .registerModule( new SimpleModule())
            .registerModule(new JavaTimeModule())
            .registerModule( new Jdk8Module())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);;
    }

    @Override
    public ObjectMapper
    getContext(Class<?> aClass)
    {
        return itsMapper;
    }
}

//////////////////////////////////////////////////////////////////////////////
