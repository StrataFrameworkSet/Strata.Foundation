//////////////////////////////////////////////////////////////////////////////
// FooEventReceiver.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.chronicle.event;

import net.openhft.chronicle.queue.ChronicleQueue;

public
class FooEventReceiver
    extends ChronicleEventReceiver<FooEvent,IFooEventListener>
    implements IFooEventReceiver
{
    public
    FooEventReceiver(String name,ChronicleQueue queue)
    {
        super(FooEvent.class,name,queue);
    }
}

//////////////////////////////////////////////////////////////////////////////
