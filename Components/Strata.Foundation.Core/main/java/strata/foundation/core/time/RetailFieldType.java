//////////////////////////////////////////////////////////////////////////////
// RetailFieldType.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

public
enum RetailFieldType
{
    YEAR("Year"),
    QUARTER_OF_YEAR("QuarterOfYear"),
    MONTH_OF_QUARTER("MonthOfQuarter"),
    MONTH_OF_YEAR("MonthOfYear"),
    WEEK_OF_YEAR("WeekOfYear"),
    WEEK_OF_QUARTER("WeekOfQuarter"),
    WEEK_OF_MONTH("WeekOfMonth"),
    DAY_OF_YEAR("DayOfYear");

    private final String displayName;

    RetailFieldType(String displayName)
    {
        this.displayName = displayName;
    }

    public String getDisplayName() { return displayName; }
}

//////////////////////////////////////////////////////////////////////////////
