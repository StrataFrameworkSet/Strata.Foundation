// ##########################################################################
// # File Name:	ReadLock.java
// ##########################################################################

package strata.foundation.core.concurrent;

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

// ##########################################################################
