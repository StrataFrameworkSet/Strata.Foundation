//////////////////////////////////////////////////////////////////////////////
// FiscalUnitType.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * <p>
 * Kinds of {@link FiscalCalendarUnit} supported by a {@link FiscalCalendar},
 * each with a display name and an estimated duration.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Usage
 * FiscalUnitType type = FiscalUnitType.WEEK_BASED_YEARS;
 * String name = type.getDisplayName();
 * Duration duration = type.getDuration();
 * </pre>
 */
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
