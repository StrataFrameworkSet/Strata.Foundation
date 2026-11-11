//////////////////////////////////////////////////////////////////////////////
// RetailYearQuarterRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

/**
 * <p>
 * Inclusive {@link CalendarPeriodRange} of consecutive {@link RetailYearQuarter}s.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * RetailYearQuarterRange range =
 *     RetailYearQuarterRange.of(
 *         RetailYearQuarter.of(RetailCalendar.NRF,2026,1),
 *         RetailYearQuarter.of(RetailCalendar.NRF,2026,4));
 *
 * // Usage
 * long quarters = range.lengthInPeriods();
 * boolean inRange = range.contains(LocalDate.of(2026,6,15));
 * </pre>
 */
public
class RetailYearQuarterRange
    extends CalendarPeriodRange<RetailYearQuarter>
{
    public
    RetailYearQuarterRange(RetailYearQuarter beginning,RetailYearQuarter end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static RetailYearQuarterRange
    of(RetailYearQuarter beginning,RetailYearQuarter end)
        throws NullPointerException, IllegalArgumentException
    {
        return new RetailYearQuarterRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
