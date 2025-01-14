//////////////////////////////////////////////////////////////////////////////
// IEventListener.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

public
interface IEventListener<E>
{
    void
    onEvent(E event);

    void
    onException(Exception exception);
}

//////////////////////////////////////////////////////////////////////////////