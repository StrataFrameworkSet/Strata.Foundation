// ##########################################################################
// # File Name:	WriteLock.java
// ##########################################################################

package strata.foundation.core.concurrent;

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

// ##########################################################################
