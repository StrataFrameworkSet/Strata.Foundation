//////////////////////////////////////////////////////////////////////////////
// FiscalFieldType.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

/**
 * <p>
 * Kinds of {@link FiscalCalendarField} supported by a {@link FiscalCalendar},
 * each with a display name.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Usage
 * FiscalFieldType type = FiscalFieldType.MONTH_OF_YEAR;
 * String name = type.getDisplayName();
 * </pre>
 */
public
enum FiscalFieldType
{
    YEAR("Year"),
    QUARTER_OF_YEAR("QuarterOfYear"),
    MONTH_OF_QUARTER("MonthOfQuarter"),
    MONTH_OF_YEAR("MonthOfYear"),
    DAY_OF_YEAR("DayOfYear"),
    WEEK_BASED_YEAR("WeekBasedYear"),
    WEEK_OF_WEEK_BASED_YEAR("WeekOfWeekBasedYear");

    private final String displayName;

    FiscalFieldType(String displayName)
    {
        this.displayName = displayName;
    }

    public String getDisplayName() { return displayName; }
}

//////////////////////////////////////////////////////////////////////////////
