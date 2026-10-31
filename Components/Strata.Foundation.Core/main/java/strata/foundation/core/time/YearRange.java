//////////////////////////////////////////////////////////////////////////////
// YearRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.Year;

public
class YearRange
    extends TemporalRange<Year>
{
    public
    YearRange(Year beginning,Year end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static YearRange
    of(Year beginning,Year end)
        throws NullPointerException, IllegalArgumentException
    {
        return new YearRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
