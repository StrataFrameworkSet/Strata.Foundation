/// ///////////////////////////////////////////////////////////////////////////
// BlockingQueueReceiver.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public
class BlockingQueueReceiver<T,C extends Consumer<T>>
    extends AbstractReceiver<T,C>
{
    private IBlockingQueue<T>     queue;
    private final ExecutorService executor;
    private final AtomicBoolean   consuming;

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
                throw new ConsumeFailedException("Failed to consume.", e);
            }
        }
    }
}

//////////////////////////////////////////////////////////////////////////////
