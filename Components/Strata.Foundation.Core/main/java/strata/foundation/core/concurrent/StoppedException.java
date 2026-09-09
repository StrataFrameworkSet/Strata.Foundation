//////////////////////////////////////////////////////////////////////////////
// StoppedException.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

/**
 * <p>
 * Thrown when an operation is attempted on a stopped
 * {@link IBlockingQueue}.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Thrown after queue is stopped
 * IBlockingQueue&lt;String&gt; queue = new StoppableBlockingQueue&lt;&gt;();
 * queue.start();
 * queue.stop();
 *
 * try
 * {
 *     queue.take(); // throws StoppedException
 * }
 * catch (StoppedException e)
 * {
 *     // queue has been stopped
 * }
 * </pre>
 * </p>
 */
public
class StoppedException
    extends RuntimeException
{
    public
    StoppedException(String message)
    {
        super(message);
    }

    public
    StoppedException(String message,Throwable cause)
    {
        super(message,cause);
    }

    public
    StoppedException(Throwable cause)
    {
        super(cause);
    }
}

//////////////////////////////////////////////////////////////////////////////

