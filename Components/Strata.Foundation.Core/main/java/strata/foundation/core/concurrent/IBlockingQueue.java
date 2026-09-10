//////////////////////////////////////////////////////////////////////////////
// IBlockingQueue.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Stoppable extension of {@link java.util.concurrent.BlockingQueue} that
 * supports graceful start and stop lifecycle for coordinated shutdown.
 * See: <a href="https://en.wikipedia.org/wiki/Blocking_(computing)">Blocking (Wikipedia)</a>
 * </p>
 * <br/>
 * <b>Type Parameter</b><br/>
 * {@code <T>} - element type
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation and lifecycle
 * IBlockingQueue&lt;String&gt; queue = new StoppableBlockingQueue&lt;&gt;();
 * queue.start();
 *
 * // Producer/consumer usage
 * queue.put("message");
 * String msg = queue.take();
 *
 * // Graceful shutdown
 * queue.stop();
 * </pre>
 */
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
