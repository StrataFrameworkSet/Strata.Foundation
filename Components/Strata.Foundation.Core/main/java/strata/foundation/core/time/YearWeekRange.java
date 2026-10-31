//////////////////////////////////////////////////////////////////////////////
// YearWeekRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

public
class YearWeekRange
    extends CalendarPeriodRange<YearWeek>
{
    public
    YearWeekRange(YearWeek beginning,YearWeek end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static YearWeekRange
    of(YearWeek beginning,YearWeek end)
        throws NullPointerException, IllegalArgumentException
    {
        return new YearWeekRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
