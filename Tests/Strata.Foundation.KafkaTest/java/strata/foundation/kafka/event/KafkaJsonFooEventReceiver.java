//////////////////////////////////////////////////////////////////////////////
// KafkaJsonFooEventReceiver.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.kafka.event;

import jakarta.inject.Inject;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import strata.foundation.core.event.FooEvent;
import strata.foundation.core.event.IFooEventListener;
import strata.foundation.core.event.IFooEventReceiver;
import strata.foundation.kafka.mapper.KafkaJsonFooEventDeserializer;

import java.util.HashMap;
import java.util.Map;

public
class KafkaJsonFooEventReceiver
    extends    AbstractKafkaEventReceiver<FooEvent,IFooEventListener>
    implements IFooEventReceiver
{
    @Inject
    public KafkaJsonFooEventReceiver()
    {
        super(getProperties(),FooEvent.class,"foo.events.json");
    }


    private static Map<String,Object>
    getProperties()
    {
        return
            new HashMap<String,Object>()
            {{
                put(
                    ConsumerConfig.GROUP_ID_CONFIG,
                    "foo.events.json.consumer-group-1");
                put(
                    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                    "dev-kafka.aws.hautelook.net:9093");
                put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG,"SSL");
                put(
                    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                    KafkaJsonFooEventDeserializer.class.getName());
            }};
    }

}

//////////////////////////////////////////////////////////////////////////////
