//////////////////////////////////////////////////////////////////////////////
// RetailYearRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

public
class RetailYearRange
    extends CalendarPeriodRange<RetailYear>
{
    public
    RetailYearRange(RetailYear beginning,RetailYear end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static RetailYearRange
    of(RetailYear beginning,RetailYear end)
        throws NullPointerException, IllegalArgumentException
    {
        return new RetailYearRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
