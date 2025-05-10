//////////////////////////////////////////////////////////////////////////////
// KafkaAvroFooEventSender.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.kafka.event;

import jakarta.inject.Inject;
import strata.foundation.core.event.FooEvent;
import strata.foundation.core.event.IFooEventSender;

public
class KafkaAvroFooEventSender
    extends    AbstractKafkaEventSender<FooEvent>
    implements IFooEventSender
{
    @Inject
    public
    KafkaAvroFooEventSender(IKafkaConfigurationProvider provider)
    {
        super(
            provider.get(),
            e -> e.getSource().getId(),
            FooEvent.class,
            "strata.fooevent.avro");
    }

}

//////////////////////////////////////////////////////////////////////////////
