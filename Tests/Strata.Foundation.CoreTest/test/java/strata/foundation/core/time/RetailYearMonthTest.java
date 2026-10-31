//////////////////////////////////////////////////////////////////////////////
// RetailYearMonthTest.java
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
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalQueries;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("CommitStage")
public
class RetailYearMonthTest
{
    private static final RetailCalendar NRF = RetailCalendar.NRF;

    private static final RetailCalendar T445 =
        RetailCalendar.of(
            "YM_T445",
            RetailWeekPattern.FOUR_FOUR_FIVE,
            Month.JANUARY,
            DayOfWeek.SATURDAY,
            RetailYearEndRule.NEAREST_END_OF_MONTH,
            CalendarYearLabel.START_YEAR);

    private RetailYearMonth
    roundTrip(RetailYearMonth value) throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bytes))
        {
            out.writeObject(value);
        }

        try (ObjectInputStream in =
                 new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())))
        {
            return (RetailYearMonth)in.readObject();
        }
    }

    public static Stream<Arguments>
    fromSources()
    {
        return
            Stream.of(
                Arguments.of(LocalDate.of(2024,2,3),RetailYearMonth.of(NRF,2023,12)),
                Arguments.of(LocalDate.of(2024,2,4),RetailYearMonth.of(NRF,2024,1)),
                Arguments.of(LocalDate.of(2024,4,10),RetailYearMonth.of(NRF,2024,3)));
    }

    public static Stream<Arguments>
    containsSources()
    {
        return
            Stream.of(
                Arguments.of(LocalDate.of(2024,4,7),true),
                Arguments.of(LocalDate.of(2024,5,4),true),
                Arguments.of(LocalDate.of(2024,4,6),false),
                Arguments.of(LocalDate.of(2024,5,5),false));
    }

    public static Stream<Arguments>
    invalidParseSources()
    {
        return
            Stream.of(
                Arguments.of("2024-M03[UNKNOWN_CAL]"),
                Arguments.of("2024-M13[NRF]"),
                Arguments.of("2024-3"),
                Arguments.of("2024-M3"),
                Arguments.of("2024-M03[NRF"));
    }

    @Test
    public void
    testOf()
    {
        RetailYearMonth value = RetailYearMonth.of(NRF,2024,3);

        assertEquals(2024,value.getYear());
        assertEquals(3,value.getMonth());
        assertEquals(LocalDate.of(2024,4,7),value.getFirstDay());
        assertEquals(LocalDate.of(2024,5,4),value.getLastDay());
        assertEquals(4,value.lengthInWeeks());
        assertEquals("2024-M03",value.format());
        assertEquals("2024-M03[NRF]",value.toString());
    }

    @Test
    public void
    testOfFiveWeekMonth()
    {
        RetailYearMonth value = RetailYearMonth.of(NRF,2023,12);

        assertEquals(LocalDate.of(2023,12,31),value.getFirstDay());
        assertEquals(LocalDate.of(2024,2,3),value.getLastDay());
        assertEquals(5,value.lengthInWeeks());
    }

    @Test
    public void
    testOfInvalid()
    {
        assertThrows(DateTimeException.class,() -> RetailYearMonth.of(NRF,2024,13));
        assertThrows(DateTimeException.class,() -> RetailYearMonth.of(NRF,2024,0));
    }

    @Test
    public void
    testOfNullCalendar()
    {
        NullPointerException e =
            assertThrows(NullPointerException.class,() -> RetailYearMonth.of(null,2024,3));

        assertEquals("calendar is null",e.getMessage());
    }

    @ParameterizedTest
    @MethodSource("fromSources")
    public void
    testFrom(LocalDate date,RetailYearMonth expected)
    {
        assertEquals(expected,RetailYearMonth.from(NRF,date));
    }

    @Test
    public void
    testFromUnsupported()
    {
        assertThrows(
            DateTimeException.class,
            () -> RetailYearMonth.from(NRF,LocalTime.of(10,0)));
    }

    @Test
    public void
    testFromRetailYearMonth()
    {
        RetailYearMonth value = RetailYearMonth.of(NRF,2024,3);

        assertSame(value,RetailYearMonth.from(NRF,value));
    }

    @Test
    public void
    testFromRetailYearWeek()
    {
        assertEquals(
            RetailYearMonth.of(NRF,2023,12),
            RetailYearMonth.from(NRF,RetailYearWeek.of(NRF,2023,53)));
    }

    @ParameterizedTest
    @MethodSource("containsSources")
    public void
    testContains(LocalDate date,boolean expected)
    {
        assertEquals(expected,RetailYearMonth.of(NRF,2024,3).contains(date));
    }

    @Test
    public void
    testPlusAndMinus()
    {
        assertEquals(RetailYearMonth.of(NRF,2025,1),RetailYearMonth.of(NRF,2024,12).plusMonths(1));
        assertEquals(RetailYearMonth.of(NRF,2023,12),RetailYearMonth.of(NRF,2024,1).minusMonths(1));
        assertEquals(RetailYearMonth.of(NRF,2024,12),RetailYearMonth.of(NRF,2023,12).plusYears(1));
        assertEquals(RetailYearMonth.of(NRF,2023,3),RetailYearMonth.of(NRF,2024,3).minusYears(1));
        assertEquals(
            RetailYearMonth.of(NRF,2024,5),
            RetailYearMonth.of(NRF,2024,2).plus(1,NRF.quarters()));
    }

    @Test
    public void
    testPlusUnsupportedUnit()
    {
        assertThrows(
            UnsupportedTemporalTypeException.class,
            () -> RetailYearMonth.of(NRF,2024,3).plus(1,ChronoUnit.WEEKS));
    }

    @Test
    public void
    testUntil()
    {
        RetailYearMonth start = RetailYearMonth.of(NRF,2024,1);
        RetailYearMonth end   = RetailYearMonth.of(NRF,2025,1);

        assertEquals(12L,start.until(end,NRF.months()));
        assertEquals(4L,start.until(end,NRF.quarters()));
        assertEquals(1L,start.until(end,NRF.years()));
        assertEquals(-2L,RetailYearMonth.of(NRF,2024,3).until(start,NRF.months()));
    }

    @Test
    public void
    testUntilUnsupportedUnit()
    {
        assertThrows(
            UnsupportedTemporalTypeException.class,
            () -> RetailYearMonth.of(NRF,2024,3).until(RetailYearMonth.of(NRF,2024,5),ChronoUnit.DAYS));
    }

    @Test
    public void
    testWith()
    {
        RetailYearMonth value = RetailYearMonth.of(NRF,2024,3);

        assertEquals(RetailYearMonth.of(NRF,2024,7),value.with(NRF.monthOfYear(),7));
        assertEquals(RetailYearMonth.of(NRF,2024,7),value.withMonth(7));
        assertEquals(RetailYearMonth.of(NRF,2025,3),value.withYear(2025));
        assertEquals(RetailYearMonth.of(NRF,2024,6),value.with(NRF.quarterOfYear(),2));
        assertEquals(RetailYearMonth.of(NRF,2024,1),value.with(NRF.monthOfQuarter(),1));
    }

    @Test
    public void
    testWithInvalid()
    {
        assertThrows(DateTimeException.class,() -> RetailYearMonth.of(NRF,2024,3).withMonth(13));
    }

    @Test
    public void
    testWithUnsupportedField()
    {
        assertThrows(
            UnsupportedTemporalTypeException.class,
            () -> RetailYearMonth.of(NRF,2024,3).with(ChronoField.YEAR,2024));
    }

    @Test
    public void
    testIsSupportedField()
    {
        RetailYearMonth value = RetailYearMonth.of(NRF,2024,3);

        assertTrue(value.isSupported(NRF.year()));
        assertTrue(value.isSupported(NRF.quarterOfYear()));
        assertTrue(value.isSupported(NRF.monthOfYear()));
        assertTrue(value.isSupported(NRF.monthOfQuarter()));
        assertFalse(value.isSupported(NRF.weekOfYear()));
        assertFalse(value.isSupported(ChronoField.YEAR));
        assertFalse(value.isSupported(T445.monthOfYear()));
    }

    @Test
    public void
    testIsSupportedUnit()
    {
        RetailYearMonth value = RetailYearMonth.of(NRF,2024,3);

        assertTrue(value.isSupported(NRF.months()));
        assertTrue(value.isSupported(NRF.quarters()));
        assertTrue(value.isSupported(NRF.years()));
        assertFalse(value.isSupported(ChronoUnit.WEEKS));
        assertFalse(value.isSupported(T445.months()));
    }

    @Test
    public void
    testGetLong()
    {
        RetailYearMonth value = RetailYearMonth.of(NRF,2024,3);

        assertEquals(2024L,value.getLong(NRF.year()));
        assertEquals(1L,value.getLong(NRF.quarterOfYear()));
        assertEquals(3L,value.getLong(NRF.monthOfYear()));
        assertEquals(3L,value.getLong(NRF.monthOfQuarter()));
        assertEquals(ValueRange.of(1,12),value.range(NRF.monthOfYear()));
    }

    @Test
    public void
    testGetLongUnsupportedField()
    {
        RetailYearMonth value = RetailYearMonth.of(NRF,2024,3);

        assertThrows(UnsupportedTemporalTypeException.class,() -> value.getLong(NRF.weekOfYear()));
        assertThrows(UnsupportedTemporalTypeException.class,() -> value.getLong(ChronoField.YEAR));
    }

    @Test
    public void
    testConversions()
    {
        assertEquals(RetailYear.of(NRF,2024),RetailYearMonth.of(NRF,2024,3).getRetailYear());
        assertEquals(
            RetailYearQuarter.of(NRF,2024,1),
            RetailYearMonth.of(NRF,2024,3).getRetailYearQuarter());
        assertEquals(
            RetailYearQuarter.of(NRF,2024,4),
            RetailYearMonth.of(NRF,2024,10).getRetailYearQuarter());
    }

    @Test
    public void
    testAtWeek()
    {
        assertEquals(RetailYearWeek.of(NRF,2024,10),RetailYearMonth.of(NRF,2024,3).atWeek(1));
        assertEquals(RetailYearWeek.of(NRF,2023,53),RetailYearMonth.of(NRF,2023,12).atWeek(5));
        assertThrows(DateTimeException.class,() -> RetailYearMonth.of(NRF,2024,3).atWeek(5));
    }

    @Test
    public void
    testAtDay()
    {
        RetailYearMonth value = RetailYearMonth.of(NRF,2024,3);

        assertEquals(LocalDate.of(2024,4,7),value.atDay(1));
        assertEquals(LocalDate.of(2024,5,4),value.atDay(28));
        assertThrows(DateTimeException.class,() -> value.atDay(29));
    }

    @Test
    public void
    testAdjustInto()
    {
        assertEquals(
            LocalDate.of(2024,4,14),
            LocalDate.of(2024,3,10).with(RetailYearMonth.of(NRF,2024,3)));
    }

    @Test
    public void
    testQuery()
    {
        RetailYearMonth value = RetailYearMonth.of(NRF,2024,3);

        assertEquals(NRF.months(),value.query(TemporalQueries.precision()));
        assertEquals(IsoChronology.INSTANCE,value.query(TemporalQueries.chronology()));
    }

    @Test
    public void
    testNow()
    {
        Clock clock = Clock.fixed(Instant.parse("2024-04-10T12:00:00Z"),ZoneOffset.UTC);

        assertEquals(RetailYearMonth.of(NRF,2024,3),RetailYearMonth.now(NRF,clock));
    }

    @Test
    public void
    testParse()
    {
        assertEquals(RetailYearMonth.of(NRF,2024,3),RetailYearMonth.parse("2024-M03[NRF]"));
        assertEquals(RetailYearMonth.of(NRF,2024,3),RetailYearMonth.parse("2024-M03"));
    }

    @Test
    public void
    testParseWithCalendar()
    {
        RetailYearMonth value = RetailYearMonth.parse("2024-M03",T445);

        assertEquals(RetailYearMonth.of(T445,2024,3),value);
        assertEquals("2024-M03[YM_T445]",value.toString());
    }

    @Test
    public void
    testParseCalendarMismatch()
    {
        assertThrows(
            DateTimeParseException.class,
            () -> RetailYearMonth.parse("2024-M03[NRF]",T445));
    }

    @ParameterizedTest
    @MethodSource("invalidParseSources")
    public void
    testParseInvalid(String text)
    {
        assertThrows(DateTimeParseException.class,() -> RetailYearMonth.parse(text));
    }

    @Test
    public void
    testParseNull()
    {
        NullPointerException e =
            assertThrows(NullPointerException.class,() -> RetailYearMonth.parse(null));

        assertEquals("text is null",e.getMessage());
    }

    @Test
    public void
    testParseNullCalendar()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> RetailYearMonth.parse("2024-M03",null));

        assertEquals("calendar is null",e.getMessage());
    }

    @Test
    public void
    testCompareTo()
    {
        RetailYearMonth first  = RetailYearMonth.of(NRF,2024,3);
        RetailYearMonth second = RetailYearMonth.of(NRF,2024,4);
        RetailYearMonth third  = RetailYearMonth.of(NRF,2025,1);

        assertTrue(first.compareTo(second) < 0);
        assertTrue(second.compareTo(third) < 0);
        assertTrue(third.compareTo(first) > 0);
        assertEquals(0,first.compareTo(RetailYearMonth.of(NRF,2024,3)));
        assertTrue(first.isBefore(second));
        assertTrue(third.isAfter(second));
        assertFalse(first.isAfter(second));
        assertFalse(third.isBefore(second));
    }

    @Test
    public void
    testCompareToDifferentCalendar()
    {
        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> RetailYearMonth.of(NRF,2024,3).compareTo(RetailYearMonth.of(T445,2024,3)));

        assertEquals("cannot compare values from different calendars: NRF and YM_T445",e.getMessage());
    }

    @Test
    public void
    testEqualsAndHashCode()
    {
        RetailYearMonth value = RetailYearMonth.of(NRF,2024,3);

        assertEquals(value,RetailYearMonth.of(NRF,2024,3));
        assertEquals(value.hashCode(),RetailYearMonth.of(NRF,2024,3).hashCode());
        assertNotEquals(value,RetailYearMonth.of(NRF,2024,4));
        assertNotEquals(value,RetailYearMonth.of(NRF,2025,3));
        assertNotEquals(value,RetailYearMonth.of(T445,2024,3));
        assertNotEquals(value,RetailYearQuarter.of(NRF,2024,1));
        assertNotEquals(null,value);
    }

    @Test
    public void
    testSerialization() throws IOException, ClassNotFoundException
    {
        RetailYearMonth expected = RetailYearMonth.of(NRF,2024,3);

        assertEquals(expected,roundTrip(expected));
    }

    @Test
    public void
    testJsonWrite() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapperSupplier().get();

        assertEquals(
            "{\"calendar\":\"NRF\",\"value\":\"2024-M03\"}",
            mapper.writeValueAsString(RetailYearMonth.of(NRF,2024,3)));
    }

    @Test
    public void
    testJsonRead() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapperSupplier().get();

        assertEquals(
            RetailYearMonth.of(NRF,2024,3),
            mapper.readValue(
                "{\"calendar\":\"NRF\",\"value\":\"2024-M03\"}",
                RetailYearMonth.class));
    }

    @Test
    public void
    testJsonReadMissingCalendar()
    {
        ObjectMapper mapper = new ObjectMapperSupplier().get();

        assertThrows(
            JsonProcessingException.class,
            () -> mapper.readValue("{\"value\":\"2024-M03\"}",RetailYearMonth.class));
    }

    @Test
    public void
    testDefaultCalendarOverloads()
    {
        assertEquals(NRF,RetailYearMonth.of(2024,3).getCalendar());

        RetailYearMonth before = RetailYearMonth.of(2024,3);

        RetailCalendar def =
            RetailCalendar.of(
                "YM_DEFAULT_445",
                RetailWeekPattern.FOUR_FOUR_FIVE,
                Month.JANUARY,
                DayOfWeek.SATURDAY,
                RetailYearEndRule.NEAREST_END_OF_MONTH,
                CalendarYearLabel.START_YEAR);

        try
        {
            RetailCalendarRegistry.setDefault(def);

            assertEquals(def,RetailYearMonth.of(2024,3).getCalendar());
            assertEquals(def,RetailYearMonth.parse("2024-M03").getCalendar());
            assertEquals(NRF,RetailYearMonth.parse("2024-M03[NRF]").getCalendar());
            assertEquals(NRF,before.getCalendar());
        }
        finally
        {
            RetailCalendarRegistry.setDefault(RetailCalendar.NRF);
        }
    }

    @Test
    public void
    testTemporalRange()
    {
        TemporalRange<RetailYearMonth> range =
            new TemporalRange<>(RetailYearMonth.of(NRF,2024,1),RetailYearMonth.of(NRF,2024,3));

        assertEquals(RetailYearMonth.of(NRF,2024,1),range.getBeginning());
        assertEquals(RetailYearMonth.of(NRF,2024,3),range.getEnd());
    }

    @Test
    public void
    testTemporalRangeInvalid()
    {
        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> new TemporalRange<>(RetailYearMonth.of(NRF,2024,3),RetailYearMonth.of(NRF,2024,1)));

        assertEquals("beginning must be before end",e.getMessage());
    }
}

//////////////////////////////////////////////////////////////////////////////
