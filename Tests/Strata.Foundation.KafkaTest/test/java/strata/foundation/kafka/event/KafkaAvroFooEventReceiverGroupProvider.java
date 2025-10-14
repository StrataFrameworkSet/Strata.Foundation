//////////////////////////////////////////////////////////////////////////////
// KafkaAvroFooEventReceiverGroupProvider.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.kafka.event;

import jakarta.inject.Inject;
import strata.foundation.core.event.*;

import java.util.HashSet;
import java.util.Set;

public
class KafkaAvroFooEventReceiverGroupProvider
    implements IFooEventReceiverGroupProvider
{
    private final IKafkaConfigurationProvider provider;
    private final int                         size;

    @Inject
    public
    KafkaAvroFooEventReceiverGroupProvider(
        IKafkaConfigurationProvider provider)
    {
        this.provider = provider;
        this.size     = 4;
    }

    @Override
    public IFooEventReceiverGroup
    get()
    {
        Set<IFooEventReceiver> receivers = new HashSet<>();

        for (int i = 0; i < size; i++)
        {
            IFooEventReceiver receiver = new KafkaAvroFooEventReceiver(provider);

            receiver.setListener(new FooEventListener(receiver));
            receivers.add(receiver);
        }

        return new FooEventReceiverGroup(receivers);
    }
}

//////////////////////////////////////////////////////////////////////////////
