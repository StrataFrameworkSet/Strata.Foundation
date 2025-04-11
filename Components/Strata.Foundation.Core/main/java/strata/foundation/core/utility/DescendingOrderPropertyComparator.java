//////////////////////////////////////////////////////////////////////////////
// AscendingOrderPropertyComparator.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.function.Function;

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
