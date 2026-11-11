//////////////////////////////////////////////////////////////////////////////
// YearRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.Year;

/**
 * <p>
 * {@link TemporalRange} of {@link java.time.Year} values,
 * from a beginning through an end value.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * YearRange range =
 *     YearRange.of(
 *         Year.of(2024),
 *         Year.of(2026));
 *
 * // Usage
 * Year beginning = range.getBeginning();
 * Year end = range.getEnd();
 * Period period = range.getPeriod();
 * </pre>
 */
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
