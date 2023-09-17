//////////////////////////////////////////////////////////////////////////////
// ObjectMapperSupplier.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

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
