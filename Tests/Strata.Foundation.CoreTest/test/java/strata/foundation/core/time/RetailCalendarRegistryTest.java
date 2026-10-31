//////////////////////////////////////////////////////////////////////////////
// RetailCalendarRegistryTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("CommitStage")
public
class RetailCalendarRegistryTest
{
    @Test
    public void
    testGetNrf()
    {
        Optional<RetailCalendar> result = RetailCalendarRegistry.get("NRF");

        assertEquals(Optional.of(RetailCalendar.NRF),result);
    }

    @Test
    public void
    testRegisterSameCalendarAgain()
    {
        assertDoesNotThrow(() -> RetailCalendarRegistry.register(RetailCalendar.NRF));
    }

    @Test
    public void
    testRegisterDifferentCalendarWithSameId()
    {
        RetailCalendar different =
            RetailCalendar.of(
                "NRF",
                RetailWeekPattern.FOUR_FOUR_FIVE,
                Month.JANUARY,
                DayOfWeek.SATURDAY,
                RetailYearEndRule.NEAREST_END_OF_MONTH,
                CalendarYearLabel.START_YEAR);

        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> RetailCalendarRegistry.register(different));

        assertEquals("a different retail calendar is already registered with id: NRF",e.getMessage());
    }

    @Test
    public void
    testRegisterAndGet()
    {
        RetailCalendar calendar =
            RetailCalendar.of(
                "REGISTRY_TEST_1",
                RetailWeekPattern.FOUR_FIVE_FOUR,
                Month.JANUARY,
                DayOfWeek.SATURDAY,
                RetailYearEndRule.NEAREST_END_OF_MONTH,
                CalendarYearLabel.START_YEAR);

        RetailCalendarRegistry.register(calendar);

        assertEquals(Optional.of(calendar),RetailCalendarRegistry.get("REGISTRY_TEST_1"));
        assertEquals(calendar,RetailCalendarRegistry.getOrThrow("REGISTRY_TEST_1"));
    }

    @Test
    public void
    testUnknownCalendar()
    {
        assertEquals(Optional.empty(),RetailCalendarRegistry.get("UNKNOWN_CALENDAR"));

        DateTimeException e =
            assertThrows(
                DateTimeException.class,
                () -> RetailCalendarRegistry.getOrThrow("UNKNOWN_CALENDAR"));

        assertEquals("Unknown retail calendar: UNKNOWN_CALENDAR",e.getMessage());
    }

    @Test
    public void
    testGetNull()
    {
        NullPointerException e =
            assertThrows(NullPointerException.class,() -> RetailCalendarRegistry.get(null));

        assertEquals("id is null",e.getMessage());
    }

    @Test
    public void
    testRegisterNull()
    {
        NullPointerException e =
            assertThrows(NullPointerException.class,() -> RetailCalendarRegistry.register(null));

        assertEquals("calendar is null",e.getMessage());
    }

    @Test
    public void
    testGetDefault()
    {
        assertEquals(RetailCalendar.NRF,RetailCalendarRegistry.getDefault());
    }

    @Test
    public void
    testSetDefault()
    {
        RetailCalendar calendar =
            RetailCalendar.of(
                "REGISTRY_DEFAULT_1",
                RetailWeekPattern.FOUR_FOUR_FIVE,
                Month.JANUARY,
                DayOfWeek.SATURDAY,
                RetailYearEndRule.NEAREST_END_OF_MONTH,
                CalendarYearLabel.START_YEAR);

        try
        {
            RetailCalendarRegistry.setDefault(calendar);

            assertEquals(calendar,RetailCalendarRegistry.getDefault());
            assertTrue(RetailCalendarRegistry.get("REGISTRY_DEFAULT_1").isPresent());
        }
        finally
        {
            RetailCalendarRegistry.setDefault(RetailCalendar.NRF);
        }

        assertEquals(RetailCalendar.NRF,RetailCalendarRegistry.getDefault());
    }

    @Test
    public void
    testSetDefaultConflicting()
    {
        RetailCalendar different =
            RetailCalendar.of(
                "NRF",
                RetailWeekPattern.FOUR_FOUR_FIVE,
                Month.JANUARY,
                DayOfWeek.SATURDAY,
                RetailYearEndRule.NEAREST_END_OF_MONTH,
                CalendarYearLabel.START_YEAR);

        assertThrows(
            IllegalArgumentException.class,
            () -> RetailCalendarRegistry.setDefault(different));

        assertEquals(RetailCalendar.NRF,RetailCalendarRegistry.getDefault());
    }

    @Test
    public void
    testSetDefaultNull()
    {
        NullPointerException e =
            assertThrows(NullPointerException.class,() -> RetailCalendarRegistry.setDefault(null));

        assertEquals("calendar is null",e.getMessage());
    }
}

//////////////////////////////////////////////////////////////////////////////
