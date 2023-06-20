//////////////////////////////////////////////////////////////////////////////
// KafkaAvroFooEventSender.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.kafka.event;

import jakarta.inject.Inject;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import strata.foundation.core.action.IActionQueue;
import strata.foundation.core.event.FooEvent;
import strata.foundation.core.event.IFooEventSender;
import strata.foundation.kafka.mapper.KafkaJsonSerializer;

import java.util.HashMap;
import java.util.Map;

public
class KafkaJsonFooEventSender
    extends    AbstractKafkaEventSender<FooEvent>
    implements IFooEventSender
{
    @Inject
    public KafkaJsonFooEventSender(IActionQueue queue)
    {
        super(
            getProperties(),
            queue,
            e -> e.getSource().getId(),
            FooEvent.class,
            "foo.events.json");
    }

    private static Map<String,Object>
    getProperties()
    {
        return
            new HashMap<String,Object>()
            {{
                put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "dev-kafka.aws.hautelook.net:9093");
                put(ProducerConfig.ACKS_CONFIG, "1");
                put(ProducerConfig.RETRIES_CONFIG,"5");
                put(
                    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                    KafkaJsonSerializer.class.getName());
                put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG,"SSL");
            }};
    }
}

//////////////////////////////////////////////////////////////////////////////
