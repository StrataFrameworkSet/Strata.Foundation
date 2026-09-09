//////////////////////////////////////////////////////////////////////////////
// WriteLock.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

/**
 * <p>
 * An {@link AutoCloseable} wrapper that acquires a write lock from
 * an {@link ISynchronizer} on construction and releases it on close,
 * enabling try-with-resources write lock management.
 * See: <a href="https://en.wikipedia.org/wiki/Readers%E2%80%93writer_lock">Readers-writer lock (Wikipedia)</a>
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Try-with-resources write lock
 * ISynchronizer sync = new ReadWriteLockSynchronizer();
 *
 * try (WriteLock lock = new WriteLock(sync))
 * {
 *     // modify shared state safely
 * }
 * </pre>
 * </p>
 */
public
class WriteLock
    implements AutoCloseable
{
    private final ISynchronizer itsSynchronizer;
    
    /************************************************************************
     * Creates a new WriteLock. 
     *
     */
    public 
    WriteLock(ISynchronizer synchronizer)
    {
        itsSynchronizer = synchronizer;
        itsSynchronizer.lockForWriting();
    }

    /************************************************************************
     * {@inheritDoc} 
     */
    @Override
    public void 
    close()
    {
        itsSynchronizer.unlockFromWriting();
    }

}

//////////////////////////////////////////////////////////////////////////////
