//////////////////////////////////////////////////////////////////////////////
// BlockingQueueSender.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import strata.foundation.core.event.SendResult;

/**
 * <p>
 * Implementation of {@link ISender} that sends messages to an
 * {@link IBlockingQueue} for consumption by an {@link IReceiver}.
 * </p>
 * <p>
 * <h4>Type Parameter</h4>
 * {@code <T>} - message type
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Creation and sending
 * IBlockingQueue&lt;String&gt; queue = new StoppableBlockingQueue&lt;&gt;();
 * ISender&lt;String&gt; sender = new BlockingQueueSender&lt;&gt;(queue);
 *
 * SendResult&lt;String&gt; result = sender.send("hello");
 * </pre>
 * </p>
 */
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
