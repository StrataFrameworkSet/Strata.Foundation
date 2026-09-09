//////////////////////////////////////////////////////////////////////////////
// ICombiner.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

/**
 * <p>
 * Combines two values of type {@code T} into a single value of the same
 * type, such as when merging or reducing a collection of values.
 * </p>
 * <h4>Type Parameter</h4>
 * {@code <T>} - the type of values being combined
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * ICombiner&lt;Integer&gt; sum = (x,y) -&gt; x + y;
 *
 * int total = sum.combine(2,3);
 * </pre>
 * </p>
 */
public
interface ICombiner<T>
{
    T
    combine(T x,T y);
}

//////////////////////////////////////////////////////////////////////////////