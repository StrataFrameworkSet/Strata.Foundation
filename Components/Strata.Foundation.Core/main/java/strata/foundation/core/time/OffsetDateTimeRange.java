//////////////////////////////////////////////////////////////////////////////
// OffsetDateTimeRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.OffsetDateTime;

public
class OffsetDateTimeRange
    extends TemporalRange<OffsetDateTime>
{
    public
    OffsetDateTimeRange(OffsetDateTime beginning,OffsetDateTime end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static OffsetDateTimeRange
    of(OffsetDateTime beginning,OffsetDateTime end)
        throws NullPointerException, IllegalArgumentException
    {
        return new OffsetDateTimeRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
