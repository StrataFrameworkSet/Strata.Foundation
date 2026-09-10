//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

/**
 * <p>
 * A more natural, type-safe alternative to {@link Cloneable} and
 * {@code Object.clone()}. Implementations of the
 * <a href="https://en.wikipedia.org/wiki/Prototype_pattern">Prototype pattern (Wikipedia)</a>
 * expose a {@code copy()} method that returns a new instance holding an
 * equivalent copy of the object's state. Because Java supports covariant
 * return types, extending interfaces and implementing classes may narrow the
 * return type of {@code copy()} to a more specific type that itself extends
 * or implements {@code ICopyable}.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * class Point implements ICopyable
 * {
 *     private final int x, y;
 *
 *     Point(int x, int y)
 *     {
 *         this.x = x;
 *         this.y = y;
 *     }
 *
 *     public Point copy()
 *     {
 *         return new Point(x, y);
 *     }
 * }
 *
 * Point original = new Point(1, 2);
 * Point clone    = original.copy();
 * </pre>
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


//////////////////////////////////////////////////////////////////////////////
