//////////////////////////////////////////////////////////////////////////////
// IEventReceiver.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

import java.util.Optional;

public
interface IEventReceiver<E,L extends IEventListener<E>>
{
    IEventReceiver<E,L>
    setListener(L listener);

    Optional<L>
    getListener();

    boolean
    hasListener();

    void
    startListening(L listener);

    void
    startListening();

    void
    stopListening();

    boolean
    isListening();
}

//////////////////////////////////////////////////////////////////////////////