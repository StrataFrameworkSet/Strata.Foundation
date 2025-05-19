//////////////////////////////////////////////////////////////////////////////
// KafkaAvroFooEventReceiver.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.kafka.event;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import jakarta.inject.Inject;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import strata.foundation.core.event.FooEvent;
import strata.foundation.core.event.IFooEventListener;
import strata.foundation.core.event.IFooEventReceiver;

import java.util.HashMap;
import java.util.Map;

public
class KafkaAvroFooEventReceiver
    extends    AbstractKafkaEventReceiver<FooEvent,IFooEventListener>
    implements IFooEventReceiver
{
    @Inject
    public
    KafkaAvroFooEventReceiver(IKafkaConfigurationProvider provider)
    {
        super(provider.get(),FooEvent.class,"strata.fooevent.avro");
    }

}

//////////////////////////////////////////////////////////////////////////////
