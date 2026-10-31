//////////////////////////////////////////////////////////////////////////////
// YearMonthRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.YearMonth;

public
class YearMonthRange
    extends TemporalRange<YearMonth>
{
    public
    YearMonthRange(YearMonth beginning,YearMonth end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static YearMonthRange
    of(YearMonth beginning,YearMonth end)
        throws NullPointerException, IllegalArgumentException
    {
        return new YearMonthRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
