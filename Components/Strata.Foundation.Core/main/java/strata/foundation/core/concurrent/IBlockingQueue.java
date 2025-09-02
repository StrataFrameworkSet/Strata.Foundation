/// ///////////////////////////////////////////////////////////////////////////
// IBlockingQueue.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public
interface IBlockingQueue<T>
    extends BlockingQueue<T>
{
    @Override
    T
    remove() throws StoppedException;

    @Override
    T
    poll() throws StoppedException;

    @Override
    T
    element() throws StoppedException;

    @Override
    T
    peek() throws StoppedException;

    @Override
    T
    poll(long timeout,TimeUnit unit)
        throws InterruptedException,StoppedException;

    @Override
    T take()
        throws InterruptedException,StoppedException;

    @Override
    int
    drainTo(Collection<? super T> c)
        throws PartiallyDrainedException;

    @Override
    int
    drainTo(Collection<? super T> c,int maxElements)
        throws PartiallyDrainedException;

    IBlockingQueue<T>
    start();

    IBlockingQueue<T>
    stop() throws InterruptedException;

    boolean
    isStarted();

    boolean
    isStopped();
}

//////////////////////////////////////////////////////////////////////////////
