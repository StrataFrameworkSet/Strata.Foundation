/// ///////////////////////////////////////////////////////////////////////////
// IteratedEventListener.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

import java.util.Collection;

public abstract
class IteratedEventListener<E>
    implements IEventListener<E>
{
    @Override
    public void
    onStart() throws StartException {}

    @Override
    public void onStop() {}

    @Override
    public void
    onEvents(Collection<E> events)
    {
        events.forEach(this::doOnEvent);
    }

    protected abstract void
    onEvent(E event);

    @Override
    public abstract void
    onException(Exception exception);

    protected void
    doOnEvent(E event)
    {
        try
        {
            onEvent(event);
        }
        catch (Exception e)
        {
            onException(e);
        }
    }
}

//////////////////////////////////////////////////////////////////////////////
