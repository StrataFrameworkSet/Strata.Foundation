//////////////////////////////////////////////////////////////////////////////
// RetailCalendarUnitTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.time.temporal.UnsupportedTemporalTypeException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("CommitStage")
public
class RetailCalendarUnitTest
{
    private static final RetailCalendar NRF = RetailCalendar.NRF;

    private static final RetailCalendar T445 =
        RetailCalendar.of(
            "UNIT_T445",
            RetailWeekPattern.FOUR_FOUR_FIVE,
            Month.JANUARY,
            DayOfWeek.SATURDAY,
            RetailYearEndRule.NEAREST_END_OF_MONTH,
            CalendarYearLabel.START_YEAR);

    @Test
    public void
    testPlus()
    {
        assertEquals(LocalDate.of(2024,3,3),LocalDate.of(2024,2,4).plus(1,NRF.months()));
        assertEquals(LocalDate.of(2025,2,2),LocalDate.of(2024,2,4).plus(12,NRF.months()));
        assertEquals(LocalDate.of(2023,12,31),LocalDate.of(2024,2,4).minus(1,NRF.months()));
        assertEquals(LocalDate.of(2024,4,28),LocalDate.of(2024,3,31).plus(1,NRF.months()));
        assertEquals(LocalDate.of(2024,5,5),LocalDate.of(2024,2,4).plus(1,NRF.quarters()));
        assertEquals(LocalDate.of(2025,2,2),LocalDate.of(2024,2,4).plus(1,NRF.years()));
        assertEquals(LocalDate.of(2025,2,1),LocalDate.of(2024,2,3).plus(1,NRF.years()));
    }

    @Test
    public void
    testPlusZero()
    {
        assertEquals(LocalDate.of(2024,2,4),LocalDate.of(2024,2,4).plus(0,NRF.months()));
    }

    @Test
    public void
    testPlusLocalDateTime()
    {
        assertEquals(
            LocalDateTime.of(2024,3,3,9,30),
            LocalDateTime.of(2024,2,4,9,30).plus(1,NRF.months()));
    }

    @Test
    public void
    testBetween()
    {
        LocalDate start = LocalDate.of(2024,2,4);

        assertEquals(1,start.until(LocalDate.of(2024,3,3),NRF.months()));
        assertEquals(1,NRF.months().between(start,LocalDate.of(2024,3,3)));
        assertEquals(0,start.until(LocalDate.of(2024,3,2),NRF.months()));
        assertEquals(0,NRF.months().between(start,LocalDate.of(2024,3,2)));
        assertEquals(-1,LocalDate.of(2024,3,3).until(start,NRF.months()));
        assertEquals(-1,NRF.months().between(LocalDate.of(2024,3,3),start));
        assertEquals(1,start.until(LocalDate.of(2025,2,2),NRF.years()));
        assertEquals(1,NRF.years().between(start,LocalDate.of(2025,2,2)));
        assertEquals(12,start.until(LocalDate.of(2025,2,2),NRF.months()));
        assertEquals(12,NRF.months().between(start,LocalDate.of(2025,2,2)));
        assertEquals(4,start.until(LocalDate.of(2025,2,2),NRF.quarters()));
        assertEquals(4,NRF.quarters().between(start,LocalDate.of(2025,2,2)));
        assertEquals(0,start.until(LocalDate.of(2025,2,1),NRF.years()));
        assertEquals(0,NRF.years().between(start,LocalDate.of(2025,2,1)));
    }

    @Test
    public void
    testMetadata()
    {
        assertTrue(NRF.months().isDurationEstimated());
        assertTrue(NRF.months().isDateBased());
        assertFalse(NRF.months().isTimeBased());
        assertEquals(ChronoUnit.MONTHS.getDuration(),NRF.months().getDuration());
        assertEquals(ChronoUnit.YEARS.getDuration(),NRF.years().getDuration());
        assertEquals("Months[NRF]",NRF.months().toString());
        assertTrue(NRF.months().isSupportedBy(LocalDate.of(2024,2,4)));
        assertFalse(NRF.months().isSupportedBy(LocalTime.of(10,0)));
    }

    @Test
    public void
    testUnsupportedTemporal()
    {
        assertThrows(
            UnsupportedTemporalTypeException.class,
            () -> LocalTime.of(10,0).plus(1,NRF.months()));
    }

    @Test
    public void
    testEqualsAndHashCode()
    {
        assertEquals(NRF.months(),NRF.months());
        assertEquals(NRF.months().hashCode(),NRF.months().hashCode());
        assertNotEquals(NRF.months(),NRF.quarters());
        assertNotEquals(NRF.months(),T445.months());
    }
}

//////////////////////////////////////////////////////////////////////////////
