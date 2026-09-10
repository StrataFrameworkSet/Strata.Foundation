//////////////////////////////////////////////////////////////////////////////
// IPropertyComparator.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

/**
 * <p>
 * Defines a comparator that compares two instances of a type based on a
 * particular property, where that property's value is {@link Comparable}.
 * Implementations extract the property from each instance and compare
 * the resulting values, allowing callers to sort or order objects by an
 * arbitrary comparable attribute.
 * </p>
 * <br/>
 * <b>Type Parameters</b><br/>
 * <ul>
 * <li>{@code <T>} - the type of the objects being compared</li>
 * <li>{@code <P>} - the comparable type of the property extracted from each object</li>
 * </ul>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * IPropertyComparator&lt;Person,Integer&gt; byAge =
 *     (a,b) -&gt; a.getAge().compareTo(b.getAge());
 * int result = byAge.compare(personA,personB);
 * </pre>
 */
public
interface IPropertyComparator<T,P extends Comparable<P>>
{
    int
    compare(T a,T b);
}

//////////////////////////////////////////////////////////////////////////////
