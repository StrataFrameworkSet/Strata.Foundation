//////////////////////////////////////////////////////////////////////////////
// FiscalYearMonthRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

/**
 * <p>
 * Inclusive {@link CalendarPeriodRange} of consecutive {@link FiscalYearMonth}s.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * FiscalYearMonthRange range =
 *     FiscalYearMonthRange.of(
 *         FiscalYearMonth.of(FiscalCalendar.US_FEDERAL,2026,1),
 *         FiscalYearMonth.of(FiscalCalendar.US_FEDERAL,2026,6));
 *
 * // Usage
 * long months = range.lengthInPeriods();
 *
 * for (FiscalYearMonth month : range)
 *     process(month);
 * </pre>
 */
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
