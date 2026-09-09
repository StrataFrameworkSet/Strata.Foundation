//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

/**
 * <p>
 * Provides a simplified interface for performing read and write lock
 * synchronization using a multiple-reader/single-writer access pattern.
 * A <b>read lock</b> allows multiple concurrent readers to access shared
 * objects without interfering with one another, while a <b>write lock</b>
 * allows only a single writer to be active at a time, excluding all other
 * readers and writers. See the
 * <a href="https://en.wikipedia.org/wiki/Readers%E2%80%93writer_lock">readers-writer
 * lock</a> pattern.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * ISynchronizer synchronizer = ...;
 *
 * synchronizer.lockForReading();
 * try
 * {
 *     // read shared state
 * }
 * finally
 * {
 *     synchronizer.unlockFromReading();
 * }
 * </pre>
 * </p>
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
	 * 
	 * @see ISynchronizer.unlockFromReading()
	 *
	 */
	void
	lockForReading();
	
	/************************************************************************
	 * Acquires a <b>write lock</b> used in synchronizing concurrent access
	 * to objects. A <b>write lock</b> assumes that objects are modified 
	 * and only allows a single writer to be active--no other writers or
	 * readers can be active at the same time.
	 * 
	 * @see ISynchronizer.unlockFromWriting()
	 *
	 */
	void
	lockForWriting();
	
	/************************************************************************
	 * Releases a <b>read lock</b>.
	 * 
	 * @see ISynchronizer.lockForReading()
	 *
	 */
	void
	unlockFromReading();
	
	/************************************************************************
	 * Releases a <b>write lock</b>.
	 * 
	 * @see ISynchronizer.lockForWriting()
	 *
	 */
	void
	unlockFromWriting();
}


//////////////////////////////////////////////////////////////////////////////
