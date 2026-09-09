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
 * <h4>Type Parameters</h4>
 * <ul>
 * <li>{@code <T>} - the type of the objects being compared</li>
 * <li>{@code <P>} - the comparable type of the property extracted from each object</li>
 * </ul>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * IPropertyComparator&lt;Person,Integer&gt; byAge =
 *     (a,b) -&gt; a.getAge().compareTo(b.getAge());
 * int result = byAge.compare(personA,personB);
 * </pre>
 * </p>
 */
public
interface IPropertyComparator<T,P extends Comparable<P>>
{
    int
    compare(T a,T b);
}

//////////////////////////////////////////////////////////////////////////////