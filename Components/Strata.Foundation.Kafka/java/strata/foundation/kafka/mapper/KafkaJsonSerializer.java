//////////////////////////////////////////////////////////////////////////////
// KafkaJsonSerializer.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.kafka.mapper;

import org.apache.kafka.common.serialization.Serializer;
import strata.foundation.core.mapper.IObjectMapper;
import strata.foundation.core.mapper.JsonObjectByteArrayMapper;

import java.util.Map;

public
class KafkaJsonSerializer
    implements Serializer<Object>
{
    private final IObjectMapper<Object,byte[]> itsMapper;

    public
    KafkaJsonSerializer()
    {
        itsMapper = new JsonObjectByteArrayMapper<>();
    }

    @Override
    public void
    configure(Map<String,?> configs,boolean isKey) {}

    @Override
    public byte[]
    serialize(String topic,Object data)
    {
        return itsMapper.toPayload(data);
    }

    @Override
    public void
    close() {}
}

//////////////////////////////////////////////////////////////////////////////
