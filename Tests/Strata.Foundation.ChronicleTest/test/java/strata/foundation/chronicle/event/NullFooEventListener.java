//////////////////////////////////////////////////////////////////////////////
// FooEventListener.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.chronicle.event;

import static org.junit.jupiter.api.Assertions.fail;

public
class NullFooEventListener
    implements IFooEventListener
{

    public
    NullFooEventListener() {}

    @Override
    public void
    onEvent(FooEvent event) {}

    @Override
    public void
    onException(Exception exception)
    {
        fail(exception);
    }
}

//////////////////////////////////////////////////////////////////////////////
