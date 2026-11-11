//////////////////////////////////////////////////////////////////////////////
// RetailYearRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

/**
 * <p>
 * Inclusive {@link CalendarPeriodRange} of consecutive {@link RetailYear}s.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * RetailYearRange range =
 *     RetailYearRange.of(
 *         RetailYear.of(RetailCalendar.NRF,2024),
 *         RetailYear.of(RetailCalendar.NRF,2026));
 *
 * // Usage
 * long years = range.lengthInPeriods();
 * range.stream().forEach(year -&gt; process(year));
 * </pre>
 */
public
class RetailYearRange
    extends CalendarPeriodRange<RetailYear>
{
    public
    RetailYearRange(RetailYear beginning,RetailYear end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static RetailYearRange
    of(RetailYear beginning,RetailYear end)
        throws NullPointerException, IllegalArgumentException
    {
        return new RetailYearRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
