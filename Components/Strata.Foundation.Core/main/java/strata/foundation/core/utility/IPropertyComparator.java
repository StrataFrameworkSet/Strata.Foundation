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
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * IPropertyComparator&lt;Person,Integer&gt; byAge =
 *     (a,b) -&gt; a.getAge().compareTo(b.getAge());
 * int result = byAge.compare(personA,personB);
 * </pre>
 *
 * @param <T> the type of the objects being compared
 * @param <P> the comparable type of the property extracted from each object
 */
public
interface IPropertyComparator<T,P extends Comparable<P>>
{
    int
    compare(T a,T b);
}

//////////////////////////////////////////////////////////////////////////////
