//////////////////////////////////////////////////////////////////////////////
// ActionQueueEventSender.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

import jakarta.inject.Inject;
import strata.foundation.core.action.IActionQueue;
import strata.foundation.core.concurrent.BlockingBuffer;
import strata.foundation.core.concurrent.IBlockingBuffer;

public
class ActionQueueEventSender<E>
    implements IEventSender<E>
{
    private IEventSender<E> implementation;
    private IActionQueue    queue;

    @Inject
    public
    ActionQueueEventSender(IEventSender<E> imp,IActionQueue q)
    {
        implementation = imp;
        queue          = q;
    }

    @Override
    public ActionQueueEventSender<E>
    open() throws Exception
    {
        queue.insert(() -> implementation.open());
        return this;
    }

    @Override
    public ActionQueueEventSender<E>
    close() throws Exception
    {
        queue.insert(() -> implementation.close());
        return this;
    }

    @Override
    public ICompletableSendResult<E>
    send(E event)
    {
        IBlockingBuffer<ICompletableSendResult<E>> buffer = new BlockingBuffer<>();

        queue.insert(() -> buffer.accept(implementation.send(event)));
        return new BlockingBufferBasedCompletableSendResult<>(buffer);
    }

    @Override
    public boolean
    isOpen()
    {
        return implementation.isOpen();
    }

    @Override
    public boolean
    isClosed()
    {
        return implementation.isClosed();
    }
}

//////////////////////////////////////////////////////////////////////////////
