//////////////////////////////////////////////////////////////////////////////
// IEventListener.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

import java.util.Collection;

public
interface IEventListener<E>
{
    default void
    onStart() throws StartException
    {}

    default void
    onStop() {}

    default void
    onEvents(Collection<E> events)
    {
        events.forEach(
            event ->
                {
                    try { onEvent(event); }
                    catch (Exception e) { onException(e); }
                });
    }

    void
    onEvent(E event);

    void
    onException(Exception exception);
}

//////////////////////////////////////////////////////////////////////////////