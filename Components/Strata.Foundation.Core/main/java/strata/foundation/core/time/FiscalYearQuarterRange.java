//////////////////////////////////////////////////////////////////////////////
// FiscalYearQuarterRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

/**
 * <p>
 * Inclusive {@link CalendarPeriodRange} of consecutive {@link FiscalYearQuarter}s.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * FiscalYearQuarterRange range =
 *     FiscalYearQuarterRange.of(
 *         FiscalYearQuarter.of(FiscalCalendar.US_FEDERAL,2026,1),
 *         FiscalYearQuarter.of(FiscalCalendar.US_FEDERAL,2026,4));
 *
 * // Usage
 * long quarters = range.lengthInPeriods();
 * boolean inRange = range.contains(LocalDate.of(2026,3,15));
 * </pre>
 */
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
