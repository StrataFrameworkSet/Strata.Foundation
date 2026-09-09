//////////////////////////////////////////////////////////////////////////////
// ObjectMapperSupplier.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

/**
 * <p>
 * Default implementation of {@link IObjectMapperSupplier} that builds a
 * new Jackson {@link ObjectMapper} using a {@link JsonMapper} builder
 * and applies this framework's standard configuration via a shared
 * {@link ObjectMapperCustomizer}. A new {@link ObjectMapper} instance
 * is returned on every call to {@link #get()}.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * IObjectMapperSupplier supplier = new ObjectMapperSupplier();
 *
 * ObjectMapper mapper = supplier.get();
 * </pre>
 * </p>
 */
public
class ObjectMapperSupplier
    implements IObjectMapperSupplier
{
    private static IObjectMapperCustomizer customizer =
        new ObjectMapperCustomizer();

    @Override
    public ObjectMapper
    get()
    {
        return
            customizer
                .customize(
                    JsonMapper
                        .builder()
                        .build());

    }
}

//////////////////////////////////////////////////////////////////////////////
