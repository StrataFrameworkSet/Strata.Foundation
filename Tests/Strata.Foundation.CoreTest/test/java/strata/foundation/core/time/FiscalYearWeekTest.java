//////////////////////////////////////////////////////////////////////////////
// FiscalYearWeekTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import strata.foundation.core.mapper.ObjectMapperSupplier;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("CommitStage")
public
class FiscalYearWeekTest
{
    private static final FiscalCalendar US  = FiscalCalendar.US_FEDERAL;
    private static final FiscalCalendar CAL = FiscalCalendar.CALENDAR_YEAR;
    private static final FiscalCalendar UK  = FiscalCalendar.UK_GOVERNMENT;

    private FiscalYearWeek
    roundTrip(FiscalYearWeek value) throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bytes))
        {
            out.writeObject(value);
        }

        try (ObjectInputStream in =
                 new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())))
        {
            return (FiscalYearWeek)in.readObject();
        }
    }

    public static Stream<Arguments>
    isoSources()
    {
        return
            Stream.of(
                Arguments.of(2020,53),
                Arguments.of(2021,1),
                Arguments.of(2024,1),
                Arguments.of(2025,1),
                Arguments.of(2023,52));
    }

    @ParameterizedTest
    @MethodSource("isoSources")
    public void
    testIsoEquivalence(int year,int week)
    {
        FiscalYearWeek value = FiscalYearWeek.of(CAL,year,week);

        assertEquals(YearWeek.of(year,week).getFirstDay(),value.getFirstDay());
        assertEquals(YearWeek.of(year,week).getLastDay(),value.getLastDay());
    }

    @Test
    public void
    testOf()
    {
        FiscalYearWeek value = FiscalYearWeek.of(CAL,2020,53);

        assertEquals(LocalDate.of(2020,12,28),value.getFirstDay());
        assertEquals(LocalDate.of(2021,1,3),value.getLastDay());
        assertEquals("2020-W53",value.format());
        assertEquals("2020-W53[CALENDAR_YEAR]",value.toString());
    }

    @Test
    public void
    testOfInvalid()
    {
        assertThrows(DateTimeException.class,() -> FiscalYearWeek.of(CAL,2021,53));
    }

    @Test
    public void
    testLengthOfYear()
    {
        assertEquals(53,FiscalYearWeek.of(CAL,2020,1).lengthOfYear());
        assertEquals(52,FiscalYearWeek.of(CAL,2023,1).lengthOfYear());
        assertEquals(53,FiscalYearWeek.of(CAL,2026,1).lengthOfYear());
        assertTrue(FiscalYearWeek.of(CAL,2020,1).is53WeekYear());
        assertFalse(FiscalYearWeek.of(CAL,2023,1).is53WeekYear());
        assertTrue(FiscalYearWeek.of(CAL,2026,1).is53WeekYear());
    }

    @Test
    public void
    testFromCalendarYear()
    {
        assertEquals(FiscalYearWeek.of(CAL,2025,1),FiscalYearWeek.from(CAL,LocalDate.of(2024,12,30)));
        assertEquals(FiscalYearWeek.of(CAL,2020,53),FiscalYearWeek.from(CAL,LocalDate.of(2021,1,1)));
        assertEquals(FiscalYearWeek.of(CAL,2022,52),FiscalYearWeek.from(CAL,LocalDate.of(2023,1,1)));
    }

    @Test
    public void
    testUsFederal()
    {
        assertEquals(LocalDate.of(2024,9,30),FiscalYearWeek.of(US,2025,1).getFirstDay());
        assertEquals(LocalDate.of(2027,9,27),FiscalYearWeek.of(US,2027,53).getFirstDay());
        assertEquals(LocalDate.of(2027,10,3),FiscalYearWeek.of(US,2027,53).getLastDay());
        assertThrows(DateTimeException.class,() -> FiscalYearWeek.of(US,2025,53));
        assertEquals(FiscalYearWeek.of(US,2024,52),FiscalYearWeek.from(US,LocalDate.of(2024,9,29)));
        assertEquals(FiscalYearWeek.of(US,2025,16),FiscalYearWeek.from(US,LocalDate.of(2025,1,15)));
    }

    @Test
    public void
    testPlusAndMinus()
    {
        assertEquals(FiscalYearWeek.of(CAL,2020,53),FiscalYearWeek.of(CAL,2020,52).plusWeeks(1));
        assertEquals(FiscalYearWeek.of(CAL,2020,53),FiscalYearWeek.of(CAL,2021,1).minusWeeks(1));
        assertEquals(FiscalYearWeek.of(CAL,2021,52),FiscalYearWeek.of(CAL,2020,53).plusYears(1));
    }

    @Test
    public void
    testWith()
    {
        assertEquals(FiscalYearWeek.of(CAL,2021,52),FiscalYearWeek.of(CAL,2020,53).withYear(2021));
        assertThrows(DateTimeException.class,() -> FiscalYearWeek.of(CAL,2023,10).withWeek(53));
    }

    @Test
    public void
    testUntil()
    {
        assertEquals(
            53L,
            FiscalYearWeek.of(CAL,2020,1).until(FiscalYearWeek.of(CAL,2021,1),ChronoUnit.WEEKS));
    }

    @Test
    public void
    testUntilUnsupportedUnit()
    {
        assertThrows(
            UnsupportedTemporalTypeException.class,
            () -> FiscalYearWeek.of(CAL,2020,1).until(FiscalYearWeek.of(CAL,2021,1),ChronoUnit.DAYS));
    }

    @Test
    public void
    testIsSupported()
    {
        FiscalYearWeek value = FiscalYearWeek.of(CAL,2020,1);

        assertTrue(value.isSupported(CAL.weekBasedYear()));
        assertTrue(value.isSupported(CAL.weekOfWeekBasedYear()));
        assertFalse(value.isSupported(CAL.year()));
        assertFalse(value.isSupported(CAL.monthOfYear()));
        assertTrue(value.isSupported(ChronoUnit.WEEKS));
        assertTrue(value.isSupported(CAL.weekBasedYears()));
        assertFalse(value.isSupported(ChronoUnit.MONTHS));
    }

    @Test
    public void
    testGetLong()
    {
        assertEquals(16L,FiscalYearWeek.of(US,2025,16).getLong(US.weekOfWeekBasedYear()));
    }

    @Test
    public void
    testAtDay()
    {
        FiscalYearWeek value = FiscalYearWeek.of(US,2025,1);

        assertEquals(LocalDate.of(2024,9,30),value.atDay(DayOfWeek.MONDAY));
        assertEquals(LocalDate.of(2024,10,6),value.atDay(DayOfWeek.SUNDAY));
    }

    @Test
    public void
    testAtDayNull()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> FiscalYearWeek.of(US,2025,1).atDay(null));

        assertEquals("day is null",e.getMessage());
    }

    @Test
    public void
    testAdjustInto()
    {
        assertEquals(
            LocalDate.of(2025,1,1),
            LocalDate.of(2024,6,12).with(FiscalYearWeek.of(CAL,2025,1)));
    }

    @Test
    public void
    testNow()
    {
        Clock clock = Clock.fixed(Instant.parse("2024-09-30T12:00:00Z"),ZoneOffset.UTC);

        assertEquals(FiscalYearWeek.of(US,2025,1),FiscalYearWeek.now(US,clock));
    }

    @Test
    public void
    testParse()
    {
        assertEquals(FiscalYearWeek.of(US,2025,1),FiscalYearWeek.parse("2025-W01[US_FEDERAL]"));
    }

    @Test
    public void
    testParseInvalid()
    {
        assertThrows(
            DateTimeParseException.class,
            () -> FiscalYearWeek.parse("2021-W53[CALENDAR_YEAR]"));
    }

    @Test
    public void
    testCompareTo()
    {
        FiscalYearWeek first  = FiscalYearWeek.of(US,2025,1);
        FiscalYearWeek second = FiscalYearWeek.of(US,2025,2);

        assertTrue(first.compareTo(second) < 0);
        assertTrue(second.compareTo(first) > 0);
        assertEquals(0,first.compareTo(FiscalYearWeek.of(US,2025,1)));
    }

    @Test
    public void
    testCompareToDifferentCalendar()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> FiscalYearWeek.of(US,2025,1).compareTo(FiscalYearWeek.of(UK,2025,1)));
    }

    @Test
    public void
    testEqualsAndHashCode()
    {
        FiscalYearWeek value = FiscalYearWeek.of(US,2025,1);

        assertEquals(value,FiscalYearWeek.of(US,2025,1));
        assertEquals(value.hashCode(),FiscalYearWeek.of(US,2025,1).hashCode());
        assertNotEquals(value,FiscalYearWeek.of(US,2025,2));
        assertNotEquals(value,FiscalYearWeek.of(UK,2025,1));
        assertNotEquals(null,value);
    }

    @Test
    public void
    testSerialization() throws IOException, ClassNotFoundException
    {
        FiscalYearWeek expected = FiscalYearWeek.of(US,2025,1);

        assertEquals(expected,roundTrip(expected));
    }

    @Test
    public void
    testJsonWrite() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapperSupplier().get();

        assertEquals(
            "{\"calendar\":\"US_FEDERAL\",\"value\":\"2025-W01\"}",
            mapper.writeValueAsString(FiscalYearWeek.of(US,2025,1)));
    }

    @Test
    public void
    testJsonRead() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapperSupplier().get();

        assertEquals(
            FiscalYearWeek.of(US,2025,1),
            mapper.readValue(
                "{\"calendar\":\"US_FEDERAL\",\"value\":\"2025-W01\"}",
                FiscalYearWeek.class));
    }

    @Test
    public void
    testTemporalRange()
    {
        TemporalRange<FiscalYearWeek> range =
            new TemporalRange<>(FiscalYearWeek.of(US,2025,1),FiscalYearWeek.of(US,2025,3));

        assertEquals(FiscalYearWeek.of(US,2025,1),range.getBeginning());
        assertEquals(FiscalYearWeek.of(US,2025,3),range.getEnd());
    }

    @Test
    public void
    testTemporalRangeInvalid()
    {
        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> new TemporalRange<>(FiscalYearWeek.of(US,2025,3),FiscalYearWeek.of(US,2025,1)));

        assertEquals("beginning must be before end",e.getMessage());
    }
}

//////////////////////////////////////////////////////////////////////////////
