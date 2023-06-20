//////////////////////////////////////////////////////////////////////////////
// KafkaJsonDeserializer.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.kafka.mapper;

import org.apache.kafka.common.serialization.Deserializer;
import strata.foundation.core.mapper.IObjectMapper;
import strata.foundation.core.mapper.JsonObjectByteArrayMapper;

import java.util.Map;

public
class KafkaJsonDeserializer<E>
    implements Deserializer<E>
{
    private final IObjectMapper<E,byte[]> itsMapper;
    private final Class<E>                itsType;

    public
    KafkaJsonDeserializer(Class<E> type)
    {
        itsMapper = new JsonObjectByteArrayMapper<>();
        itsType = type;
    }

    @Override
    public void
    configure(Map configs,boolean isKey) {}

    @Override
    public E
    deserialize(String topic,byte[] data)
    {
        return itsMapper.toObject(itsType,data);
    }

    @Override
    public void
    close() {}
}

//////////////////////////////////////////////////////////////////////////////
