// ##########################################################################
// # File Name:	NullSynchronizer.java
// ##########################################################################

package strata.foundation.core.concurrent;

/**
 * <p>
 * Null Object implementation of {@link ISynchronizer} that performs
 * no actual synchronization, suitable for single-threaded contexts
 * or testing.
 * See: <a href="https://en.wikipedia.org/wiki/Null_object_pattern">Null object pattern (Wikipedia)</a>
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Use when synchronization is not needed
 * ISynchronizer sync = new NullSynchronizer();
 * sync.lockForReading();   // no-op
 * sync.unlockFromReading(); // no-op
 * </pre>
 * </p>
 */
public
class NullSynchronizer
    implements ISynchronizer
{

    /************************************************************************
     * Creates a new {@code NullSynchronizer}. 
     *
     */
    public 
    NullSynchronizer() {}

    /************************************************************************
     * {@inheritDoc}
     * Null Object methods do nothing. 
     */
    @Override
    public void 
    lockForReading() {}

    /************************************************************************
     * {@inheritDoc} 
     * Null Object methods do nothing. 
     */
    @Override
    public void 
    lockForWriting() {}

    /************************************************************************
     * {@inheritDoc} 
     * Null Object methods do nothing. 
     */
    @Override
    public void 
    unlockFromReading() {}
    
    /************************************************************************
     * {@inheritDoc} 
     * Null Object methods do nothing. 
     */
    @Override
    public void 
    unlockFromWriting() {}
}

// ##########################################################################
