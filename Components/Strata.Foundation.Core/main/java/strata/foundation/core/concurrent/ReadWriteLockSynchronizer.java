// ##########################################################################
// # File Name:	ReadWriteLockSynchronizer.java
// ##########################################################################

package strata.foundation.core.concurrent;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/****************************************************************************
 * Implements the {@code ISynchronizer} interface by wrapping
 * the standard {@code ReadWriteLock}.
 */
public 
class ReadWriteLockSynchronizer 
	implements ISynchronizer
{
	private final ReadWriteLock itsLock;
	
	/************************************************************************
	 * Creates a new ReadWriteLockSynchronizer. 
	 *
	 * @param lock
	 */
	public 
	ReadWriteLockSynchronizer()
	{
		super();
		itsLock             = new ReentrantReadWriteLock();
	}
	
	/************************************************************************
	 * Creates a new {@code ReadWriteLockSynchronizer}. 
	 *
	 * @param lock
	 */
	public 
	ReadWriteLockSynchronizer(ReadWriteLock lock)
	{
		super();
		itsLock = lock;
	}

	/************************************************************************
	 * {@inheritDoc} 
	 */
	@Override
	public synchronized void 
	lockForReading()
	{
		itsLock.readLock().lock();
	}

	/************************************************************************
	 * {@inheritDoc} 
	 */
	@Override
	public void 
	lockForWriting()
	{
		itsLock.writeLock().lock();
	}

	/************************************************************************
	 * {@inheritDoc} 
	 */
	@Override
	public void 
	unlockFromReading()
	{
		itsLock.readLock().unlock();
	}

	/************************************************************************
	 * {@inheritDoc} 
	 */
	@Override
	public void 
	unlockFromWriting()
	{
		itsLock.writeLock().unlock();
	}

}


// ##########################################################################
