//////////////////////////////////////////////////////////////////////////////
// ZonedDateTimeRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.ZonedDateTime;

public
class ZonedDateTimeRange
    extends TemporalRange<ZonedDateTime>
{
    public
    ZonedDateTimeRange(ZonedDateTime beginning,ZonedDateTime end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static ZonedDateTimeRange
    of(ZonedDateTime beginning,ZonedDateTime end)
        throws NullPointerException, IllegalArgumentException
    {
        return new ZonedDateTimeRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
