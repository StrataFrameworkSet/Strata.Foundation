//////////////////////////////////////////////////////////////////////////////
// LocalTimeRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.LocalTime;

public
class LocalTimeRange
    extends TemporalRange<LocalTime>
{
    public
    LocalTimeRange(LocalTime beginning,LocalTime end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static LocalTimeRange
    of(LocalTime beginning,LocalTime end)
        throws NullPointerException, IllegalArgumentException
    {
        return new LocalTimeRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
