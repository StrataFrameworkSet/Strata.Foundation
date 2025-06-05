/// ///////////////////////////////////////////////////////////////////////////
// BlockingQueueSender.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import strata.foundation.core.event.SendResult;

public
class BlockingQueueSender<T>
    implements ISender<T>
{
    private final IBlockingQueue<T> queue;

    public
    BlockingQueueSender(IBlockingQueue<T> queue)
    {
        this.queue = queue;
    }

    @Override
    public SendResult<T>
    send(T element)
    {
        try
        {
            queue.put(element);
            return new SendResult<>(element);
        }
        catch (Exception e)
        {
            return new SendResult<>(e);
        }
    }
}

//////////////////////////////////////////////////////////////////////////////
