//////////////////////////////////////////////////////////////////////////////
// RetailYearQuarterRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

public
class RetailYearQuarterRange
    extends CalendarPeriodRange<RetailYearQuarter>
{
    public
    RetailYearQuarterRange(RetailYearQuarter beginning,RetailYearQuarter end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static RetailYearQuarterRange
    of(RetailYearQuarter beginning,RetailYearQuarter end)
        throws NullPointerException, IllegalArgumentException
    {
        return new RetailYearQuarterRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
