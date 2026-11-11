//////////////////////////////////////////////////////////////////////////////
// LocalDateRange.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.LocalDate;

/**
 * <p>
 * {@link TemporalRange} of {@link java.time.LocalDate} values,
 * from a beginning through an end value.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * LocalDateRange range =
 *     LocalDateRange.of(
 *         LocalDate.of(2026,1,1),
 *         LocalDate.of(2026,3,31));
 *
 * // Usage
 * LocalDate beginning = range.getBeginning();
 * LocalDate end = range.getEnd();
 * Period period = range.getPeriod();
 * </pre>
 */
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
