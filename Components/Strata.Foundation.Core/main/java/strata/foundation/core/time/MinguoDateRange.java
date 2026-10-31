//////////////////////////////////////////////////////////////////////////////
// MinguoDateRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.chrono.MinguoDate;

public
class MinguoDateRange
    extends TemporalRange<MinguoDate>
{
    public
    MinguoDateRange(MinguoDate beginning,MinguoDate end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static MinguoDateRange
    of(MinguoDate beginning,MinguoDate end)
        throws NullPointerException, IllegalArgumentException
    {
        return new MinguoDateRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
