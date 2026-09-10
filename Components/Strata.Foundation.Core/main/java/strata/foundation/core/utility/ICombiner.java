//////////////////////////////////////////////////////////////////////////////
// ICombiner.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

/**
 * <p>
 * Combines two values of type {@code T} into a single value of the same
 * type, such as when merging or reducing a collection of values.
 * </p>
 * <br/>
 * <b>Type Parameter</b><br/>
 * {@code <T>} - the type of values being combined
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * ICombiner&lt;Integer&gt; sum = (x,y) -&gt; x + y;
 *
 * int total = sum.combine(2,3);
 * </pre>
 */
public
interface ICombiner<T>
{
    T
    combine(T x,T y);
}

//////////////////////////////////////////////////////////////////////////////
