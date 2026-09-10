// ##########################################################################
// # File Name:	ISynchronizer.java
// ##########################################################################

package strata.foundation.core.concurrent;

/**
 * <p>
 * Provides a simplified interface for read and write lock
 * synchronization using a multiple reader/single writer access pattern.
 * See: <a href="https://en.wikipedia.org/wiki/Readers%E2%80%93writer_lock">Readers-writer lock (Wikipedia)</a>
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Using with ReadLock/WriteLock (try-with-resources)
 * ISynchronizer sync = new ReadWriteLockSynchronizer();
 *
 * try (ReadLock lock = new ReadLock(sync))
 * {
 *     // read shared state
 * }
 *
 * try (WriteLock lock = new WriteLock(sync))
 * {
 *     // modify shared state
 * }
 * </pre>
 */
public
interface ISynchronizer
{
	/************************************************************************
	 * Acquires a <b>read lock</b> used in synchronizing concurrent access
	 * to objects. A <b>read lock</b> assumes that objects are only read
	 * and <b>not</b> modified. This enables multiple readers to access
	 * the same object(s) simultaneously without interfering with each
	 * other.
	 */
	void
	lockForReading();
	
	/************************************************************************
	 * Acquires a <b>write lock</b> used in synchronizing concurrent access
	 * to objects. A <b>write lock</b> assumes that objects are modified 
	 * and only allows a single writer to be active--no other writers or
	 * readers can be active at the same time.
	 */
	void
	lockForWriting();
	
	/************************************************************************
	 * Releases a <b>read lock</b>.
	 */
	void
	unlockFromReading();
	
	/************************************************************************
	 * Releases a <b>write lock</b>.
	 */
	void
	unlockFromWriting();
}


// ##########################################################################
