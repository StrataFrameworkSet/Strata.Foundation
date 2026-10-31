//////////////////////////////////////////////////////////////////////////////
// FiscalYearMonthRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

public
class FiscalYearMonthRange
    extends CalendarPeriodRange<FiscalYearMonth>
{
    public
    FiscalYearMonthRange(FiscalYearMonth beginning,FiscalYearMonth end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static FiscalYearMonthRange
    of(FiscalYearMonth beginning,FiscalYearMonth end)
        throws NullPointerException, IllegalArgumentException
    {
        return new FiscalYearMonthRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
