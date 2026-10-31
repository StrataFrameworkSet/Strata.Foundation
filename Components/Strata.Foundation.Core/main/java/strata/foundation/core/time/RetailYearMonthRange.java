//////////////////////////////////////////////////////////////////////////////
// RetailYearMonthRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

public
class RetailYearMonthRange
    extends CalendarPeriodRange<RetailYearMonth>
{
    public
    RetailYearMonthRange(RetailYearMonth beginning,RetailYearMonth end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static RetailYearMonthRange
    of(RetailYearMonth beginning,RetailYearMonth end)
        throws NullPointerException, IllegalArgumentException
    {
        return new RetailYearMonthRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
