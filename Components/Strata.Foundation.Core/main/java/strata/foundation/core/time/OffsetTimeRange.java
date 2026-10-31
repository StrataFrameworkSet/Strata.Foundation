//////////////////////////////////////////////////////////////////////////////
// OffsetTimeRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.OffsetTime;

public
class OffsetTimeRange
    extends TemporalRange<OffsetTime>
{
    public
    OffsetTimeRange(OffsetTime beginning,OffsetTime end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static OffsetTimeRange
    of(OffsetTime beginning,OffsetTime end)
        throws NullPointerException, IllegalArgumentException
    {
        return new OffsetTimeRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
