//////////////////////////////////////////////////////////////////////////////
// BlockingQueueReceiver.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * <p>
 * Implementation of {@link IReceiver} that consumes messages from
 * an {@link IBlockingQueue} on a background thread using an
 * {@link java.util.concurrent.ExecutorService}.
 * </p>
 * <br/>
 * <b>Type Parameters</b><br/>
 * <ul>
 * <li>{@code <T>} - message type</li>
 * <li>{@code <C>} - consumer type</li>
 * </ul>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation and consumption
 * IBlockingQueue&lt;String&gt; queue = new StoppableBlockingQueue&lt;&gt;();
 * BlockingQueueReceiver&lt;String,Consumer&lt;String&gt;&gt; receiver =
 *     new BlockingQueueReceiver&lt;&gt;(queue);
 *
 * receiver.startConsuming(msg -&gt; System.out.println(msg));
 * // ... later ...
 * receiver.stopConsuming();
 * </pre>
 */
public
class BlockingQueueReceiver<T,C extends Consumer<T>>
    extends AbstractReceiver<T,C>
{
    private IBlockingQueue<T>     queue;
    private final ExecutorService executor;
    private final AtomicBoolean   consuming;
    private final AtomicReference<Throwable> exception;

    public
    BlockingQueueReceiver(IBlockingQueue<T> queue)
    {
        this(queue, Executors.newSingleThreadExecutor());
    }

    public
    BlockingQueueReceiver(IBlockingQueue<T> queue,ExecutorService executor)
    {
        super();
        this.queue = queue;
        this.executor = executor;
        this.consuming = new AtomicBoolean(false);
        this.exception = new AtomicReference<>(null);
    }

    @Override
    public void
    startConsuming()
        throws StartFailedException
    {
        if (isConsuming())
            return;

        if (!hasConsumer())
            throw new StartFailedException("No consumer set for receiver.");

        try
        {
            consuming.set(true);
            executor.execute(this::runConsumeLoop);
        }
        catch (Exception e)
        {
            throw new StartFailedException("Failed to start consuming.", e);
        }

        if (exception.get() != null)
            throw
                new StartFailedException(
                    "Failed to start consuming.",
                    exception.getAndSet(null));
    }

    @Override
    public void
    stopConsuming()
    {
        try
        {
            queue.stop();
        }
        catch (InterruptedException e) {}
    }

    @Override
    public boolean
    isConsuming()
    {
        return consuming.get();
    }

    protected void
    runConsumeLoop() throws ConsumeFailedException
    {
        while (consuming.get())
        {
            try
            {
                T element = queue.take();

                getConsumer()
                    .orElseThrow()
                    .accept(element);
            }
            catch (StoppedException e)
            {
                consuming.set(false);
            }
            catch (Exception e)
            {
                exception.set(
                    new ConsumeFailedException("Failed to consume.", e));
                consuming.set(false);
            }
        }
    }
}

//////////////////////////////////////////////////////////////////////////////
