//////////////////////////////////////////////////////////////////////////////
// YearMonthRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.YearMonth;

/**
 * <p>
 * {@link TemporalRange} of {@link java.time.YearMonth} values,
 * from a beginning through an end value.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * YearMonthRange range =
 *     YearMonthRange.of(
 *         YearMonth.of(2026,1),
 *         YearMonth.of(2026,6));
 *
 * // Usage
 * YearMonth beginning = range.getBeginning();
 * YearMonth end = range.getEnd();
 * Period period = range.getPeriod();
 * </pre>
 */
public
class YearMonthRange
    extends TemporalRange<YearMonth>
{
    public
    YearMonthRange(YearMonth beginning,YearMonth end)
        throws NullPointerException, IllegalArgumentException
    {
        super(beginning,end);
    }

    public static YearMonthRange
    of(YearMonth beginning,YearMonth end)
        throws NullPointerException, IllegalArgumentException
    {
        return new YearMonthRange(beginning,end);
    }
}

//////////////////////////////////////////////////////////////////////////////
