//////////////////////////////////////////////////////////////////////////////
// FiscalYearWeekRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

public
class FiscalYearWeekRange
    extends CalendarPeriodRange<FiscalYearWeek>
{
    public
    FiscalYearWeekRange(FiscalYearWeek beginning,FiscalYearWeek end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static FiscalYearWeekRange
    of(FiscalYearWeek beginning,FiscalYearWeek end)
        throws NullPointerException, IllegalArgumentException
    {
        return new FiscalYearWeekRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
