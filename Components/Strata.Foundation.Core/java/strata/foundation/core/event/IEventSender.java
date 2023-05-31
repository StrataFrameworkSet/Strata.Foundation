//////////////////////////////////////////////////////////////////////////////
// IEventSender.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

import java.util.concurrent.CompletionStage;

public
interface IEventSender<E>
{
    IEventSender<E>
    open() throws Exception;

    IEventSender<E>
    close() throws Exception;

    CompletionStage<SendResult<E>>
    send(E event);

    boolean
    isOpen();

    boolean
    isClosed();
}


//////////////////////////////////////////////////////////////////////////////