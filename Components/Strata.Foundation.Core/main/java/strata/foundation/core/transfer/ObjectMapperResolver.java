//////////////////////////////////////////////////////////////////////////////
// ObjectMapperResolver.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.transfer;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;
import strata.foundation.core.mapper.ObjectMapperSupplier;

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
        itsMapper = new ObjectMapperSupplier().get();
    }

    @Override
    public ObjectMapper
    getContext(Class<?> aClass)
    {
        return itsMapper;
    }
}

//////////////////////////////////////////////////////////////////////////////
