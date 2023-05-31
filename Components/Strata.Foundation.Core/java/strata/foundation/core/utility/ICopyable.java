// ##########################################################################
// # File Name:	ICopyable.java
// ##########################################################################

package strata.foundation.core.utility;

/**
 * A more natural alternative to Cloneable and Object.clone().
 * Also takes advantage of Java 6's covariant return feature:
 * extending interfaces and implementing classes can extend
 * the return class of the {@code copy()} method to types 
 * that extend or implement ICopyable.
 */
public 
interface ICopyable
{
	/************************************************************************
	 * Returns a copy of the object. An alternative to {@code Object.clone()}. 
	 *
	 * @return copy of object
	 */
	ICopyable
	copy();
}


// ##########################################################################
