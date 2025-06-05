//////////////////////////////////////////////////////////////////////////////
// AbstractKafkaAvroEventReceiver.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.kafka.event;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import strata.foundation.core.event.IEventListener;

import java.util.Map;
import java.util.concurrent.ExecutorService;

public abstract
class AbstractKafkaAvroEventReceiver<E,L extends IEventListener<E>>
    extends AbstractKafkaEventReceiver<E,L>
{
    protected
    AbstractKafkaAvroEventReceiver(
        Map<String,Object> properties,
        Class<E>           type,
        String             topic)
    {
        super(initializeProperties(properties),type,topic);
    }

    protected
    AbstractKafkaAvroEventReceiver(
        Map<String,Object> properties,
        Class<E>           type,
        String             topic,
        ExecutorService    executor)
    {
        super(initializeProperties(properties),type,topic,executor);
    }

    private static Map<String,Object>
    initializeProperties(Map<String,Object> properties)
    {
        properties.put(
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            KafkaAvroDeserializer.class.getName());

        return properties;
    }

}

//////////////////////////////////////////////////////////////////////////////
