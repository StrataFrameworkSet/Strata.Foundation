//////////////////////////////////////////////////////////////////////////////
// LocalDateRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.LocalDate;

public
class LocalDateRange
    extends TemporalRange<LocalDate>
{
    public
    LocalDateRange(LocalDate beginning,LocalDate end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static LocalDateRange
    of(LocalDate beginning,LocalDate end)
        throws NullPointerException, IllegalArgumentException
    {
        return new LocalDateRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
