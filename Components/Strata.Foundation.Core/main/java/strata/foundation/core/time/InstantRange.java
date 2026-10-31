//////////////////////////////////////////////////////////////////////////////
// InstantRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.Instant;

public
class InstantRange
    extends TemporalRange<Instant>
{
    public
    InstantRange(Instant beginning,Instant end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static InstantRange
    of(Instant beginning,Instant end)
        throws NullPointerException, IllegalArgumentException
    {
        return new InstantRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
