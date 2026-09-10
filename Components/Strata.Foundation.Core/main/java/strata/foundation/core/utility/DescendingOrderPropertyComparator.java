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
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * IPropertyComparator&lt;Person,String&gt; byNameDesc =
 *     new DescendingOrderPropertyComparator&lt;&gt;(Person::getName);
 *
 * people.sort(byNameDesc);
 * </pre>
 *
 * @param <T> the type of object being compared
 * @param <P> the type of the comparable property extracted from {@code T}
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
