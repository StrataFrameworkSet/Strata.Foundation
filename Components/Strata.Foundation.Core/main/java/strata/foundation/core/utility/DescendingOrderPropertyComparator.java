//////////////////////////////////////////////////////////////////////////////
// DescendingOrderPropertyComparator.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.function.Function;

/**
 * <p>
 * Compares two objects of type {@code T} by extracting a {@link Comparable}
 * property from each using a {@link Function} selector and returning the
 * result of comparing the properties in descending order.
 * </p>
 * <h4>Type Parameters</h4>
 * <ul>
 * <li>{@code <T>} - the type of object being compared</li>
 * <li>{@code <P>} - the type of the comparable property extracted from {@code T}</li>
 * </ul>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * IPropertyComparator&lt;Person,String&gt; byNameDesc =
 *     new DescendingOrderPropertyComparator&lt;&gt;(Person::getName);
 *
 * people.sort(byNameDesc);
 * </pre>
 * </p>
 */
public
class DescendingOrderPropertyComparator<T,P extends Comparable<P>>
    implements IPropertyComparator<T,P>
{
    private final Function<T,P> selector;

    public
    DescendingOrderPropertyComparator(Function<T,P> selector)
    {
        this.selector = selector;
    }

    @Override
    public int
    compare(T a,T b)
    {
        return -selector.apply(a).compareTo(selector.apply(b));
    }
}

//////////////////////////////////////////////////////////////////////////////
