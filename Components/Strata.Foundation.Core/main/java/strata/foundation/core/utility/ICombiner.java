//////////////////////////////////////////////////////////////////////////////
// ICombiner.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

/**
 * <p>
 * Combines two values of type {@code T} into a single value of the same
 * type, such as when merging or reducing a collection of values.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * ICombiner&lt;Integer&gt; sum = (x,y) -&gt; x + y;
 *
 * int total = sum.combine(2,3);
 * </pre>
 *
 * @param <T> the type of values being combined
 */
public
interface ICombiner<T>
{
    T
    combine(T x,T y);
}

//////////////////////////////////////////////////////////////////////////////
