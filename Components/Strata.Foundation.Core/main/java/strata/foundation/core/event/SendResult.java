//////////////////////////////////////////////////////////////////////////////
// SendResult.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

import java.util.Optional;

public
class SendResult<E>
{
    private Optional<E>         sentEvent;
    private Optional<Throwable> exception;

    public
    SendResult(E event)
    {
        sentEvent = Optional.of(event);
        exception = Optional.empty();
    }

    public
    SendResult(Throwable e)
    {
        sentEvent = Optional.empty();
        exception = Optional.of(e);
    }

    public boolean
    isSuccess() { return sentEvent.isPresent(); }

    public Optional<E>
    getSentEvent() { return sentEvent; }

    public Optional<Throwable>
    getException() { return exception; }
}

//////////////////////////////////////////////////////////////////////////////
