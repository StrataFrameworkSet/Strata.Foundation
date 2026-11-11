//////////////////////////////////////////////////////////////////////////////
// CalendarYearLabel.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

/**
 * <p>
 * Determines which Gregorian year labels a calendar year that does not
 * start in January: the year in which it starts ({@code START_YEAR}) or
 * the year in which it ends ({@code END_YEAR}).
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // A fiscal year starting October 2025 is labeled 2026
 * FiscalCalendar calendar =
 *     FiscalCalendar.of("US_FEDERAL",Month.OCTOBER,CalendarYearLabel.END_YEAR);
 * </pre>
 */
public
enum CalendarYearLabel
{
    START_YEAR,
    END_YEAR
}

//////////////////////////////////////////////////////////////////////////////
