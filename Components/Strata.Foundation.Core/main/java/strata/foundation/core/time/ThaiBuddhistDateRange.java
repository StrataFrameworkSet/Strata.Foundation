//////////////////////////////////////////////////////////////////////////////
// ThaiBuddhistDateRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.chrono.ThaiBuddhistDate;

public
class ThaiBuddhistDateRange
    extends TemporalRange<ThaiBuddhistDate>
{
    public
    ThaiBuddhistDateRange(ThaiBuddhistDate beginning,ThaiBuddhistDate end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static ThaiBuddhistDateRange
    of(ThaiBuddhistDate beginning,ThaiBuddhistDate end)
        throws NullPointerException, IllegalArgumentException
    {
        return new ThaiBuddhistDateRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
