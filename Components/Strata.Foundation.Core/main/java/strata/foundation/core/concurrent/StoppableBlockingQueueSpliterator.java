/// ///////////////////////////////////////////////////////////////////////////
// StoppableBlockingQueueSpliterator.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.util.Spliterator;
import java.util.function.Consumer;

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
