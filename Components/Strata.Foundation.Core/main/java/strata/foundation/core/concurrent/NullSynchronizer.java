// ##########################################################################
// # File Name:	NullSynchronizer.java
// ##########################################################################

package strata.foundation.core.concurrent;

/****************************************************************************
 * Implements the {@code ISynchronizer} interface as a <b>Null Object</b> 
 * that does not do anything.
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
