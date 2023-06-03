//////////////////////////////////////////////////////////////////////////////
// MulticastedFooEventSender.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.chronicle.event;

import java.util.Set;

public
class PartitionedFooEventSender
    extends PartitionedChronicleEventSender<FooEvent,IFooEventSender>
    implements IFooEventSender
{
    public
    PartitionedFooEventSender(Set<IFooEventSender> senders)
    {
        super(senders,event -> event.getX());
    }
}

//////////////////////////////////////////////////////////////////////////////
