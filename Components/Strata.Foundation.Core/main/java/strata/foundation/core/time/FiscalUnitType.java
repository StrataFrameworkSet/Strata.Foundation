//////////////////////////////////////////////////////////////////////////////
// FiscalUnitType.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public
enum FiscalUnitType
{
    WEEK_BASED_YEARS("WeekBasedYears",ChronoUnit.YEARS.getDuration());

    private final String   displayName;
    private final Duration duration;

    FiscalUnitType(String displayName,Duration duration)
    {
        this.displayName = displayName;
        this.duration    = duration;
    }

    public String getDisplayName() { return displayName; }

    public Duration getDuration() { return duration; }
}

//////////////////////////////////////////////////////////////////////////////
