//////////////////////////////////////////////////////////////////////////////
// FiscalYearRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

/**
 * <p>
 * Inclusive {@link CalendarPeriodRange} of consecutive {@link FiscalYear}s.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * FiscalYearRange range =
 *     FiscalYearRange.of(
 *         FiscalYear.of(FiscalCalendar.US_FEDERAL,2024),
 *         FiscalYear.of(FiscalCalendar.US_FEDERAL,2026));
 *
 * // Usage
 * long years = range.lengthInPeriods();
 * range.stream().forEach(year -&gt; process(year));
 * </pre>
 */
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
