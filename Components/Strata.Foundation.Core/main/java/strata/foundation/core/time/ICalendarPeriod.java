//////////////////////////////////////////////////////////////////////////////
// ICalendarPeriod.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.LocalDate;

/**
 * <p>
 * A period of a calendar bounded by a first and last day, such as a year,
 * quarter, month, or week of a fiscal or retail calendar.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * ICalendarPeriod period = FiscalYearMonth.of(FiscalCalendar.US_FEDERAL,2026,1);
 *
 * // Usage
 * LocalDate first = period.getFirstDay();
 * LocalDate last = period.getLastDay();
 * boolean inPeriod = period.contains(LocalDate.of(2025,10,15));
 * </pre>
 */
public
interface ICalendarPeriod
{
    LocalDate
    getFirstDay();

    LocalDate
    getLastDay();

    boolean
    contains(LocalDate date);
}

//////////////////////////////////////////////////////////////////////////////
