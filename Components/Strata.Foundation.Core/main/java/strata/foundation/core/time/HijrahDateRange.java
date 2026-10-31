//////////////////////////////////////////////////////////////////////////////
// HijrahDateRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.chrono.HijrahDate;

public
class HijrahDateRange
    extends TemporalRange<HijrahDate>
{
    public
    HijrahDateRange(HijrahDate beginning,HijrahDate end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static HijrahDateRange
    of(HijrahDate beginning,HijrahDate end)
        throws NullPointerException, IllegalArgumentException
    {
        return new HijrahDateRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
