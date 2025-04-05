//////////////////////////////////////////////////////////////////////////////
// AscendingOrderSorter.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.function.Function;

public
class AscendingOrderSorter<T,P extends Comparable<P>>
    implements IPropertySorter<T,P>
{
    @Override
    public int
    compare(T a,T b,Function<T,P> selector)
    {
        return selector.apply(a).compareTo(selector.apply(b));
    }
}

//////////////////////////////////////////////////////////////////////////////
