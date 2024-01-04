//////////////////////////////////////////////////////////////////////////////
// KafkaAvroFooEventSender.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.kafka.event;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import jakarta.inject.Inject;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import strata.foundation.core.event.FooEvent;
import strata.foundation.core.event.IFooEventSender;

import java.util.HashMap;
import java.util.Map;

public
class KafkaAvroFooEventSender
    extends    AbstractKafkaEventSender<FooEvent>
    implements IFooEventSender
{
    @Inject
    public
    KafkaAvroFooEventSender()
    {
        super(
            getProperties(),
            e -> e.getSource().getId(),
            FooEvent.class,
            "foo.events.avro");
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
                    KafkaAvroSerializer.class.getName());
                put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG,"SSL");
                put(
                    KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG,
                   "https://dev-kafka-schema.aws.hautelook.net:8081");
            }};
    }
}

//////////////////////////////////////////////////////////////////////////////
