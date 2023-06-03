//////////////////////////////////////////////////////////////////////////////
// MulticastedFooEventSender.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.chronicle.event;

import java.util.Set;

public
class MulticastedFooEventSender
    extends MulticastedChronicleEventSender<FooEvent,IFooEventSender>
    implements IFooEventSender
{
    public
    MulticastedFooEventSender(Set<IFooEventSender> senders)
    {
        super(senders);
    }
}

//////////////////////////////////////////////////////////////////////////////
