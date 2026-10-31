//////////////////////////////////////////////////////////////////////////////
// JapaneseDateRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.chrono.JapaneseDate;

public
class JapaneseDateRange
    extends TemporalRange<JapaneseDate>
{
    public
    JapaneseDateRange(JapaneseDate beginning,JapaneseDate end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static JapaneseDateRange
    of(JapaneseDate beginning,JapaneseDate end)
        throws NullPointerException, IllegalArgumentException
    {
        return new JapaneseDateRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
