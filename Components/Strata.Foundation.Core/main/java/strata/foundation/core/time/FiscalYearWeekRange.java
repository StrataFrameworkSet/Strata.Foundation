//////////////////////////////////////////////////////////////////////////////
// FiscalYearWeekRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

/**
 * <p>
 * Inclusive {@link CalendarPeriodRange} of consecutive {@link FiscalYearWeek}s.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * FiscalYearWeekRange range =
 *     FiscalYearWeekRange.of(
 *         FiscalYearWeek.of(FiscalCalendar.US_FEDERAL,2026,1),
 *         FiscalYearWeek.of(FiscalCalendar.US_FEDERAL,2026,13));
 *
 * // Usage
 * long weeks = range.lengthInPeriods();
 * LocalDate last = range.getLastDay();
 * </pre>
 */
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
