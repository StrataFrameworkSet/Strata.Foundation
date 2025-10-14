//////////////////////////////////////////////////////////////////////////////
// FooEventReceiverGroup.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

import java.util.Set;

public
class FooEventReceiverGroup
    extends
        AbstractEventReceiverGroup<
            FooEvent,
            IFooEventListener,
            IFooEventReceiver>
    implements IFooEventReceiverGroup
{
    public
    FooEventReceiverGroup() {}

    public
    FooEventReceiverGroup(Set<IFooEventReceiver> receivers)
    {
        super(receivers);
    }
}

//////////////////////////////////////////////////////////////////////////////
