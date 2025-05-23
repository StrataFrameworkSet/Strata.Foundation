//////////////////////////////////////////////////////////////////////////////
// FooEventListener.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.chronicle.event;

import strata.foundation.core.event.IteratedEventListener;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public
class FooEventListener
    extends IteratedEventListener<FooEvent>
    implements IFooEventListener
{
    private List<FooEvent> received;

    public
    FooEventListener(List<FooEvent> r)
    {
        received = r;
    }

    @Override
    protected void
    onEvent(FooEvent event)
    {
        received.add(event);
    }

    @Override
    public void
    onException(Exception exception)
    {
        fail(exception);
    }
}

//////////////////////////////////////////////////////////////////////////////
