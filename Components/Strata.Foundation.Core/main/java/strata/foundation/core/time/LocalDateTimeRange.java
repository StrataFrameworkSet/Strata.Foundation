//////////////////////////////////////////////////////////////////////////////
// LocalDateTimeRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.LocalDateTime;

public
class LocalDateTimeRange
    extends TemporalRange<LocalDateTime>
{
    public
    LocalDateTimeRange(LocalDateTime beginning,LocalDateTime end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static LocalDateTimeRange
    of(LocalDateTime beginning,LocalDateTime end)
        throws NullPointerException, IllegalArgumentException
    {
        return new LocalDateTimeRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
