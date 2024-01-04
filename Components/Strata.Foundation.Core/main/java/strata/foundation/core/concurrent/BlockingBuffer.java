//////////////////////////////////////////////////////////////////////////////
// BlockingBuffer.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;

public
class BlockingBuffer<T>
    implements IBlockingBuffer<T>
{
    private CountDownLatch latch;
    private Optional<T>    element;

    public
    BlockingBuffer()
    {
        latch   = new CountDownLatch(1);
        element = Optional.empty();
    }

    @Override
    public void
    accept(T e)
    {
        if (element.isPresent())
            throw new IllegalStateException("Buffer is already filled. Clear to accept new element.");

        element = Optional.ofNullable(e);

        if (element.isPresent())
            latch.countDown();
    }

    @Override
    public T
    get()
    {
        try
        {
            latch.await();
            return element.get();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void
    clear()
    {
        latch = new CountDownLatch(1);
        element = Optional.empty();
    }
}

//////////////////////////////////////////////////////////////////////////////
