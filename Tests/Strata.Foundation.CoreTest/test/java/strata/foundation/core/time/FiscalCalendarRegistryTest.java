//////////////////////////////////////////////////////////////////////////////
// FiscalCalendarRegistryTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.DateTimeException;
import java.time.Month;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("CommitStage")
public
class FiscalCalendarRegistryTest
{
    @Test
    public void
    testGetDefault()
    {
        assertEquals(FiscalCalendar.CALENDAR_YEAR,FiscalCalendarRegistry.getDefault());
    }

    public static Stream<Arguments>
    builtInSources()
    {
        return
            Stream.of(
                Arguments.of("CALENDAR_YEAR",FiscalCalendar.CALENDAR_YEAR),
                Arguments.of("US_FEDERAL",FiscalCalendar.US_FEDERAL),
                Arguments.of("US_STATE",FiscalCalendar.US_STATE),
                Arguments.of("UK_GOVERNMENT",FiscalCalendar.UK_GOVERNMENT),
                Arguments.of("EU_GOVERNMENT",FiscalCalendar.EU_GOVERNMENT));
    }

    @ParameterizedTest
    @MethodSource("builtInSources")
    public void
    testGetBuiltIn(String id,FiscalCalendar expected)
    {
        assertEquals(Optional.of(expected),FiscalCalendarRegistry.get(id));
    }

    @Test
    public void
    testRegisterSameCalendarAgain()
    {
        assertDoesNotThrow(() -> FiscalCalendarRegistry.register(FiscalCalendar.US_FEDERAL));
    }

    @Test
    public void
    testRegisterDifferentCalendarWithSameId()
    {
        FiscalCalendar different =
            FiscalCalendar.of("US_FEDERAL",Month.JULY,CalendarYearLabel.END_YEAR);

        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> FiscalCalendarRegistry.register(different));

        assertEquals(
            "a different fiscal calendar is already registered with id: US_FEDERAL",
            e.getMessage());
    }

    @Test
    public void
    testRegisterAndGet()
    {
        FiscalCalendar calendar =
            FiscalCalendar.of("FISCAL_REGISTRY_TEST_1",Month.MARCH,CalendarYearLabel.END_YEAR);

        FiscalCalendarRegistry.register(calendar);

        assertEquals(Optional.of(calendar),FiscalCalendarRegistry.get("FISCAL_REGISTRY_TEST_1"));
        assertEquals(calendar,FiscalCalendarRegistry.getOrThrow("FISCAL_REGISTRY_TEST_1"));
    }

    @Test
    public void
    testUnknownCalendar()
    {
        assertEquals(Optional.empty(),FiscalCalendarRegistry.get("UNKNOWN_FISCAL"));

        DateTimeException e =
            assertThrows(
                DateTimeException.class,
                () -> FiscalCalendarRegistry.getOrThrow("UNKNOWN_FISCAL"));

        assertEquals("Unknown fiscal calendar: UNKNOWN_FISCAL",e.getMessage());
    }

    @Test
    public void
    testGetNull()
    {
        NullPointerException e =
            assertThrows(NullPointerException.class,() -> FiscalCalendarRegistry.get(null));

        assertEquals("id is null",e.getMessage());
    }

    @Test
    public void
    testRegisterNull()
    {
        NullPointerException e =
            assertThrows(NullPointerException.class,() -> FiscalCalendarRegistry.register(null));

        assertEquals("calendar is null",e.getMessage());
    }

    @Test
    public void
    testSetDefaultNull()
    {
        NullPointerException e =
            assertThrows(NullPointerException.class,() -> FiscalCalendarRegistry.setDefault(null));

        assertEquals("calendar is null",e.getMessage());
    }

    @Test
    public void
    testSetDefault()
    {
        try
        {
            FiscalCalendarRegistry.setDefault(FiscalCalendar.US_FEDERAL);

            assertEquals(FiscalCalendar.US_FEDERAL,FiscalCalendarRegistry.getDefault());
        }
        finally
        {
            FiscalCalendarRegistry.setDefault(FiscalCalendar.CALENDAR_YEAR);
        }

        assertEquals(FiscalCalendar.CALENDAR_YEAR,FiscalCalendarRegistry.getDefault());
    }

    @Test
    public void
    testSetDefaultConflicting()
    {
        FiscalCalendar different =
            FiscalCalendar.of("US_FEDERAL",Month.JULY,CalendarYearLabel.END_YEAR);

        assertThrows(
            IllegalArgumentException.class,
            () -> FiscalCalendarRegistry.setDefault(different));

        assertEquals(FiscalCalendar.CALENDAR_YEAR,FiscalCalendarRegistry.getDefault());
    }
}

//////////////////////////////////////////////////////////////////////////////
