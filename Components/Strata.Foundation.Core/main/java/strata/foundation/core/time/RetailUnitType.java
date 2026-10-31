//////////////////////////////////////////////////////////////////////////////
// RetailUnitType.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public
enum RetailUnitType
{
    MONTHS("Months",ChronoUnit.MONTHS.getDuration()),
    QUARTERS("Quarters",ChronoUnit.YEARS.getDuration().dividedBy(4)),
    YEARS("Years",ChronoUnit.YEARS.getDuration());

    private final String   displayName;
    private final Duration duration;

    RetailUnitType(String displayName,Duration duration)
    {
        this.displayName = displayName;
        this.duration    = duration;
    }

    public String getDisplayName() { return displayName; }

    public Duration getDuration() { return duration; }
}

//////////////////////////////////////////////////////////////////////////////
