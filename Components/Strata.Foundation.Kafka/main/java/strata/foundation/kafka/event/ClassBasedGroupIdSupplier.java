/// ///////////////////////////////////////////////////////////////////////////
// ClassBasedGroupIdSupplier.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.kafka.event;

import java.util.Map;
import java.util.Optional;

public
class ClassBasedGroupIdSupplier<T>
    implements IGroupIdSupplier
{
    private final Class<T>           type;
    private final Map<String,Object> properties;

    public
    ClassBasedGroupIdSupplier(
        Class<T>           type,
        Map<String,Object> properties)
    {
        this.type = type;
        this.properties = properties;
    }
    @Override
    public Optional<String>
    get()
    {
        String groupIdKey =
            KafkaConfigurationProvider.GROUP_ID_PREFIX_KEY +
            type.getSimpleName();

        return
            properties.containsKey(groupIdKey)
                ? Optional.ofNullable(properties.get(groupIdKey).toString())
                : Optional.empty();
    }
}

//////////////////////////////////////////////////////////////////////////////
