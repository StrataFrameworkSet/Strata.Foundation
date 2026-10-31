//////////////////////////////////////////////////////////////////////////////
// FiscalYearQuarterRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

public
class FiscalYearQuarterRange
    extends CalendarPeriodRange<FiscalYearQuarter>
{
    public
    FiscalYearQuarterRange(FiscalYearQuarter beginning,FiscalYearQuarter end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static FiscalYearQuarterRange
    of(FiscalYearQuarter beginning,FiscalYearQuarter end)
        throws NullPointerException, IllegalArgumentException
    {
        return new FiscalYearQuarterRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
