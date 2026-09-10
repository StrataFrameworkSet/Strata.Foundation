// ##########################################################################
// # File Name:	ReadWriteLockSynchronizer.java
// ##########################################################################

package strata.foundation.core.concurrent;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <p>
 * Implementation of {@link ISynchronizer} that wraps a standard
 * {@link java.util.concurrent.locks.ReadWriteLock} for multiple
 * reader/single writer synchronization.
 * See: <a href="https://en.wikipedia.org/wiki/Readers%E2%80%93writer_lock">Readers-writer lock (Wikipedia)</a>
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Default construction (uses ReentrantReadWriteLock)
 * ISynchronizer sync = new ReadWriteLockSynchronizer();
 *
 * // Custom lock
 * ISynchronizer sync = new ReadWriteLockSynchronizer(myLock);
 *
 * // Usage with try-with-resources
 * try (ReadLock lock = new ReadLock(sync))
 * {
 *     // read shared state
 * }
 * </pre>
 */
public
class ReadWriteLockSynchronizer
	implements ISynchronizer
{
	private final ReadWriteLock itsLock;
	
	/************************************************************************
	 * Creates a new ReadWriteLockSynchronizer. 
	 *
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
