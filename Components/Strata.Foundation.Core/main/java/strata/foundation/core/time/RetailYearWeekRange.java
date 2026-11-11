//////////////////////////////////////////////////////////////////////////////
// RetailYearWeekRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

/**
 * <p>
 * Inclusive {@link CalendarPeriodRange} of consecutive {@link RetailYearWeek}s.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * RetailYearWeekRange range =
 *     RetailYearWeekRange.of(
 *         RetailYearWeek.of(RetailCalendar.NRF,2026,1),
 *         RetailYearWeek.of(RetailCalendar.NRF,2026,13));
 *
 * // Usage
 * long weeks = range.lengthInPeriods();
 * LocalDate last = range.getLastDay();
 * </pre>
 */
public
class RetailYearWeekRange
    extends CalendarPeriodRange<RetailYearWeek>
{
    public
    RetailYearWeekRange(RetailYearWeek beginning,RetailYearWeek end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static RetailYearWeekRange
    of(RetailYearWeek beginning,RetailYearWeek end)
        throws NullPointerException, IllegalArgumentException
    {
        return new RetailYearWeekRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
