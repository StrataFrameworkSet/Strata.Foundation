//////////////////////////////////////////////////////////////////////////////
// FooEventListener.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.chronicle.event;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.fail;

public
class NullFooEventListener
    implements IFooEventListener
{

    public
    NullFooEventListener() {}

    @Override
    public void
    onEvents(Collection<FooEvent> event) {}

    @Override
    public void
    onException(Exception exception)
    {
        fail(exception);
    }
}

//////////////////////////////////////////////////////////////////////////////
