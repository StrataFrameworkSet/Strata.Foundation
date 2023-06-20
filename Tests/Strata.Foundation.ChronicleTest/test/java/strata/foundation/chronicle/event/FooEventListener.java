//////////////////////////////////////////////////////////////////////////////
// FooEventListener.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.chronicle.event;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public
class FooEventListener
    implements IFooEventListener
{
    private List<FooEvent> received;

    public
    FooEventListener(List<FooEvent> r)
    {
        received = r;
    }

    @Override
    public void
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
