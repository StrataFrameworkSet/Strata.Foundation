//////////////////////////////////////////////////////////////////////////////
// ReadLock.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

/**
 * <p>
 * An {@link AutoCloseable} wrapper that acquires a read lock from
 * an {@link ISynchronizer} on construction and releases it on close,
 * enabling try-with-resources read lock management.
 * See: <a href="https://en.wikipedia.org/wiki/Readers%E2%80%93writer_lock">Readers-writer lock (Wikipedia)</a>
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Try-with-resources read lock
 * ISynchronizer sync = new ReadWriteLockSynchronizer();
 *
 * try (ReadLock lock = new ReadLock(sync))
 * {
 *     // read shared state safely
 * }
 * </pre>
 * </p>
 */
public
class ReadLock
    implements AutoCloseable
{
    private final ISynchronizer itsSynchronizer;
    
    /************************************************************************
     * Creates a new ReadLock. 
     *
     */
    public 
    ReadLock(ISynchronizer synchronizer)
    {
        itsSynchronizer = synchronizer;
        itsSynchronizer.lockForReading();
    }

    /************************************************************************
     * {@inheritDoc} 
     */
    @Override
    public void 
    close()
    {
        itsSynchronizer.unlockFromReading();
    }

}

//////////////////////////////////////////////////////////////////////////////
