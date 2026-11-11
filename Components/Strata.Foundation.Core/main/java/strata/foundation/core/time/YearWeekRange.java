//////////////////////////////////////////////////////////////////////////////
// YearWeekRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

/**
 * <p>
 * Inclusive {@link CalendarPeriodRange} of consecutive ISO {@link YearWeek}s.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * YearWeekRange range =
 *     YearWeekRange.of(
 *         YearWeek.of(2026,1),
 *         YearWeek.of(2026,13));
 *
 * // Usage
 * long weeks = range.lengthInPeriods();
 *
 * for (YearWeek week : range)
 *     process(week);
 * </pre>
 */
public
class YearWeekRange
    extends CalendarPeriodRange<YearWeek>
{
    public
    YearWeekRange(YearWeek beginning,YearWeek end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static YearWeekRange
    of(YearWeek beginning,YearWeek end)
        throws NullPointerException, IllegalArgumentException
    {
        return new YearWeekRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
