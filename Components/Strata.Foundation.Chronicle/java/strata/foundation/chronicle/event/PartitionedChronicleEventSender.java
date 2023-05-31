//////////////////////////////////////////////////////////////////////////////
// PartitionedChronicleEventSender.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.chronicle.event;

import strata.foundation.core.event.IEventKeySelector;
import strata.foundation.core.event.IEventSender;
import strata.foundation.core.event.SendResult;

import java.util.Set;
import java.util.concurrent.CompletionStage;

public
class PartitionedChronicleEventSender<E,S extends IEventSender<E>>
    extends CompositeChronicleEventSender<E,S>
{
    private final IEventKeySelector<String,E> keySelector;

    public
    PartitionedChronicleEventSender(Set<S> senders,IEventKeySelector<String,E> k)
    {
        super(senders);
        keySelector = k;
    }

    @Override
    public CompletionStage<SendResult<E>>
    send(E event)
    {
        return getSenderForEvent(event).send(event);
    }

    private S
    getSenderForEvent(E event)
    {
        return getSenders().get(getPartition(event));
    }

    private int
    getPartition(E event)
    {
        int hashcode =
            keySelector
                .get(event)
                .hashCode();

        int partition = hashcode % getSenders().size();

        return partition >= 0 ? partition : -partition;
    }
}

//////////////////////////////////////////////////////////////////////////////
