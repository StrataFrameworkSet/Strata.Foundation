//////////////////////////////////////////////////////////////////////////////
// FiscalYearRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

public
class FiscalYearRange
    extends CalendarPeriodRange<FiscalYear>
{
    public
    FiscalYearRange(FiscalYear beginning,FiscalYear end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static FiscalYearRange
    of(FiscalYear beginning,FiscalYear end)
        throws NullPointerException, IllegalArgumentException
    {
        return new FiscalYearRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
