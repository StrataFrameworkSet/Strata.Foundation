// ##########################################################################
// # File Name:	ISynchronizer.java
// #
// # Copyright:	2011, Sapientia Systems, LLC. All Rights Reserved.
// #
// # License:	This file is part of the StrataCommon Framework.
// #
// #   			The StrataCommon Framework is free software: you 
// #			can redistribute it and/or modify it under the terms of 
// #			the GNU Lesser General Public License as published by
// #    		the Free Software Foundation, either version 3 of the 
// #			License, or (at your option) any later version.
// #
// #    		The StrataCommon Framework is distributed in the 
// #			hope that it will be useful, but WITHOUT ANY WARRANTY; 
// #			without even the implied warranty of MERCHANTABILITY or 
// #			FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser 
// #			General Public License for more details.
// #
// #    		You should have received a copy of the GNU Lesser 
// #			General Public License along with the StrataCommon
// #			Framework. If not, see http://www.gnu.org/licenses/.
// ##########################################################################

package strata.foundation.core.utility;

/****************************************************************************
 * Provides a simplified interface for doing read and write lock
 * synchronization using a multiple reader/single writer access
 * pattern.
 * 
 * @author 		
 *     Sapientia Systems
 * @conventions	
 *     <a href="{@docRoot}/NamingConventions.html">Naming Conventions</a>
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


// ##########################################################################
