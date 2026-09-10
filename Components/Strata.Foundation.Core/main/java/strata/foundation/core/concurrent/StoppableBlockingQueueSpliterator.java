//////////////////////////////////////////////////////////////////////////////
// StoppableBlockingQueueSpliterator.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * <p>
 * A {@link java.util.Spliterator} over a {@link StoppableBlockingQueue}
 * that supports sequential traversal of queued elements, terminating
 * when the queue is stopped.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation from a stoppable queue
 * StoppableBlockingQueue&lt;String&gt; queue = new StoppableBlockingQueue&lt;&gt;();
 * Spliterator&lt;String&gt; spliter =
 *     new StoppableBlockingQueueSpliterator&lt;&gt;(queue);
 *
 * // Sequential traversal
 * spliter.forEachRemaining(item -&gt; process(item));
 * </pre>
 *
 * @param <T> element type
 */
public
class StoppableBlockingQueueSpliterator<T>
    implements Spliterator<T>
{
    private final StoppableBlockingQueue<T> source;

    public
    StoppableBlockingQueueSpliterator(StoppableBlockingQueue<T> source)
    {
        this.source = source;
    }

    @Override
    public boolean
    tryAdvance(Consumer<? super T> action)
    {
        try
        {
            action.accept(source.take());
            return true;
        }
        catch (StoppedException e)
        {
            return false;
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public void
    forEachRemaining(Consumer<? super T> action)
    {
        while (tryAdvance(action)) {}
    }

    @Override
    public Spliterator<T>
    trySplit()
    {
        return null;
    }

    @Override
    public long
    estimateSize()
    {
        return Long.MAX_VALUE;
    }

    @Override
    public int
    characteristics()
    {
        return ORDERED|NONNULL;
    }
}

//////////////////////////////////////////////////////////////////////////////
