//////////////////////////////////////////////////////////////////////////////
// RetailYearEndRule.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

/**
 * <p>
 * Rule for choosing the last day of a retail year: the configured day of
 * week nearest the end of the year-end month ({@code NEAREST_END_OF_MONTH}),
 * or the last occurrence of that day of week in the year-end month
 * ({@code LAST_OF_MONTH}).
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Usage
 * RetailCalendar calendar =
 *     RetailCalendar.of(
 *         "CUSTOM",
 *         RetailWeekPattern.FOUR_FIVE_FOUR,
 *         Month.JANUARY,
 *         DayOfWeek.SATURDAY,
 *         RetailYearEndRule.LAST_OF_MONTH,
 *         CalendarYearLabel.START_YEAR);
 * </pre>
 */
public
enum RetailYearEndRule
{
    NEAREST_END_OF_MONTH,
    LAST_OF_MONTH
}

//////////////////////////////////////////////////////////////////////////////
