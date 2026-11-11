//////////////////////////////////////////////////////////////////////////////
// RetailYearMonthRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

/**
 * <p>
 * Inclusive {@link CalendarPeriodRange} of consecutive {@link RetailYearMonth}s.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * RetailYearMonthRange range =
 *     RetailYearMonthRange.of(
 *         RetailYearMonth.of(RetailCalendar.NRF,2026,1),
 *         RetailYearMonth.of(RetailCalendar.NRF,2026,6));
 *
 * // Usage
 * long months = range.lengthInPeriods();
 *
 * for (RetailYearMonth month : range)
 *     process(month);
 * </pre>
 */
public
class RetailYearMonthRange
    extends CalendarPeriodRange<RetailYearMonth>
{
    public
    RetailYearMonthRange(RetailYearMonth beginning,RetailYearMonth end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static RetailYearMonthRange
    of(RetailYearMonth beginning,RetailYearMonth end)
        throws NullPointerException, IllegalArgumentException
    {
        return new RetailYearMonthRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
