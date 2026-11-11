//////////////////////////////////////////////////////////////////////////////
// JapaneseDateRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.chrono.JapaneseDate;

/**
 * <p>
 * {@link TemporalRange} of {@link java.time.chrono.JapaneseDate} values,
 * from a beginning through an end value.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * JapaneseDateRange range =
 *     JapaneseDateRange.of(
 *         JapaneseDate.of(2026,1,1),
 *         JapaneseDate.of(2026,3,31));
 *
 * // Usage
 * JapaneseDate beginning = range.getBeginning();
 * JapaneseDate end = range.getEnd();
 * Period period = range.getPeriod();
 * </pre>
 */
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
