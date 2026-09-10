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

/**
 * <p>
 * A JAX-RS {@link jakarta.ws.rs.ext.ContextResolver} that supplies a single,
 * shared {@link com.fasterxml.jackson.databind.ObjectMapper} instance,
 * configured by {@link strata.foundation.core.mapper.ObjectMapperSupplier},
 * for JSON ({@link jakarta.ws.rs.core.MediaType#APPLICATION_JSON}) request
 * and response bodies. Registered as a JAX-RS {@link
 * jakarta.ws.rs.ext.Provider} so the JAX-RS runtime discovers it and uses
 * the resolved {@code ObjectMapper} in place of a default one when
 * (de)serializing entities.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Registered automatically by JAX-RS via @Provider, or explicitly:
 * ResourceConfig config = new ResourceConfig();
 * config.register(new ObjectMapperResolver());
 * </pre>
 */
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
