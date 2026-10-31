//////////////////////////////////////////////////////////////////////////////
// RetailYearWeekRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

public
class RetailYearWeekRange
    extends CalendarPeriodRange<RetailYearWeek>
{
    public
    RetailYearWeekRange(RetailYearWeek beginning,RetailYearWeek end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static RetailYearWeekRange
    of(RetailYearWeek beginning,RetailYearWeek end)
        throws NullPointerException, IllegalArgumentException
    {
        return new RetailYearWeekRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
