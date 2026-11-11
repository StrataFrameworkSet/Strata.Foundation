//////////////////////////////////////////////////////////////////////////////
// RetailWeekPattern.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.DateTimeException;

/**
 * <p>
 * Number of weeks in each month of a 13-week retail quarter, such as
 * 4-5-4, 4-4-5, or 5-4-4.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Usage
 * RetailWeekPattern pattern = RetailWeekPattern.FOUR_FIVE_FOUR;
 * int weeks = pattern.getWeeksInMonthOfQuarter(2);
 * int minimum = pattern.getMinimumWeeksInMonth();
 * int maximum = pattern.getMaximumWeeksInMonth();
 * </pre>
 */
public
enum RetailWeekPattern
{
    FOUR_FIVE_FOUR(4,5,4),
    FOUR_FOUR_FIVE(4,4,5),
    FIVE_FOUR_FOUR(5,4,4);

    private final int firstMonthWeeks;
    private final int secondMonthWeeks;
    private final int thirdMonthWeeks;

    RetailWeekPattern(int firstMonthWeeks,int secondMonthWeeks,int thirdMonthWeeks)
    {
        this.firstMonthWeeks  = firstMonthWeeks;
        this.secondMonthWeeks = secondMonthWeeks;
        this.thirdMonthWeeks  = thirdMonthWeeks;
    }

    public int
    getWeeksInMonthOfQuarter(int monthOfQuarter)
        throws DateTimeException
    {
        return
            switch (monthOfQuarter)
            {
                case 1 -> firstMonthWeeks;
                case 2 -> secondMonthWeeks;
                case 3 -> thirdMonthWeeks;
                default ->
                    throw new DateTimeException("Invalid month of quarter: " + monthOfQuarter);
            };
    }

    public int
    getMinimumWeeksInMonth()
    {
        return Math.min(firstMonthWeeks,Math.min(secondMonthWeeks,thirdMonthWeeks));
    }

    public int
    getMaximumWeeksInMonth()
    {
        return Math.max(Math.max(firstMonthWeeks,secondMonthWeeks),thirdMonthWeeks + 1);
    }
}

//////////////////////////////////////////////////////////////////////////////
