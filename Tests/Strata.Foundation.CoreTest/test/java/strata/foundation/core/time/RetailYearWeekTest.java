//////////////////////////////////////////////////////////////////////////////
// RetailYearWeekTest.java
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
import java.time.Month;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQueries;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("CommitStage")
public
class RetailYearWeekTest
{
    private static final RetailCalendar NRF = RetailCalendar.NRF;

    private static final RetailCalendar OTHER =
        RetailCalendar.of(
            "QW_T445",
            RetailWeekPattern.FOUR_FOUR_FIVE,
            Month.JANUARY,
            DayOfWeek.SATURDAY,
            RetailYearEndRule.NEAREST_END_OF_MONTH,
            CalendarYearLabel.START_YEAR);

    private RetailYearWeek
    roundTrip(RetailYearWeek week) throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bytes))
        {
            out.writeObject(week);
        }

        try (ObjectInputStream in =
                 new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())))
        {
            return (RetailYearWeek)in.readObject();
        }
    }

    public static Stream<Arguments>
    fromSources()
    {
        return
            Stream.of(
                Arguments.of(LocalDate.of(2024,2,3),RetailYearWeek.of(RetailCalendar.NRF,2023,53)),
                Arguments.of(LocalDate.of(2024,2,4),RetailYearWeek.of(RetailCalendar.NRF,2024,1)),
                Arguments.of(LocalDate.of(2024,4,14),RetailYearWeek.of(RetailCalendar.NRF,2024,11)));
    }

    public static Stream<Arguments>
    plusWeeksSources()
    {
        return
            Stream.of(
                Arguments.of(
                    RetailYearWeek.of(RetailCalendar.NRF,2023,52),1L,
                    RetailYearWeek.of(RetailCalendar.NRF,2023,53)),
                Arguments.of(
                    RetailYearWeek.of(RetailCalendar.NRF,2023,53),1L,
                    RetailYearWeek.of(RetailCalendar.NRF,2024,1)),
                Arguments.of(
                    RetailYearWeek.of(RetailCalendar.NRF,2024,1),-1L,
                    RetailYearWeek.of(RetailCalendar.NRF,2023,53)));
    }

    public static Stream<Arguments>
    getLongSources()
    {
        return
            Stream.of(
                Arguments.of(RetailCalendar.NRF.year(),2024L),
                Arguments.of(RetailCalendar.NRF.quarterOfYear(),1L),
                Arguments.of(RetailCalendar.NRF.monthOfYear(),3L),
                Arguments.of(RetailCalendar.NRF.monthOfQuarter(),3L),
                Arguments.of(RetailCalendar.NRF.weekOfYear(),11L),
                Arguments.of(RetailCalendar.NRF.weekOfQuarter(),11L),
                Arguments.of(RetailCalendar.NRF.weekOfMonth(),2L));
    }

    public static Stream<Arguments>
    atDaySources()
    {
        return
            Stream.of(
                Arguments.of(DayOfWeek.SUNDAY,LocalDate.of(2024,2,4)),
                Arguments.of(DayOfWeek.SATURDAY,LocalDate.of(2024,2,10)),
                Arguments.of(DayOfWeek.MONDAY,LocalDate.of(2024,2,5)));
    }

    public static Stream<Arguments>
    containsSources()
    {
        return
            Stream.of(
                Arguments.of(LocalDate.of(2024,2,4),true),
                Arguments.of(LocalDate.of(2024,2,10),true),
                Arguments.of(LocalDate.of(2024,2,3),false),
                Arguments.of(LocalDate.of(2024,2,11),false));
    }

    public static Stream<Arguments>
    invalidParseSources()
    {
        return
            Stream.of(
                Arguments.of("2024-W53[NRF]"),
                Arguments.of("2024-W5[NRF]"),
                Arguments.of("2024W05"));
    }

    @Test
    public void
    testOf()
    {
        RetailYearWeek value = RetailYearWeek.of(NRF,2024,1);

        assertEquals(2024,value.getYear());
        assertEquals(1,value.getWeek());
        assertEquals(LocalDate.of(2024,2,4),value.getFirstDay());
        assertEquals(LocalDate.of(2024,2,10),value.getLastDay());
    }

    @Test
    public void
    testOfFiftyThreeWeekYear()
    {
        RetailYearWeek value = RetailYearWeek.of(NRF,2023,53);

        assertEquals(LocalDate.of(2024,1,28),value.getFirstDay());
        assertEquals(LocalDate.of(2024,2,3),value.getLastDay());
    }

    @Test
    public void
    testOfInvalid()
    {
        assertThrows(DateTimeException.class,() -> RetailYearWeek.of(NRF,2024,53));
        assertThrows(DateTimeException.class,() -> RetailYearWeek.of(NRF,2024,0));
    }

    @Test
    public void
    testFormatAndToString()
    {
        assertEquals("2024-W01",RetailYearWeek.of(NRF,2024,1).format());
        assertEquals("2024-W05",RetailYearWeek.of(NRF,2024,5).format());
        assertEquals("2024-W01[NRF]",RetailYearWeek.of(NRF,2024,1).toString());
    }

    @ParameterizedTest
    @MethodSource("fromSources")
    public void
    testFrom(LocalDate date,RetailYearWeek expected)
    {
        assertEquals(expected,RetailYearWeek.from(NRF,date));
    }

    @ParameterizedTest
    @MethodSource("plusWeeksSources")
    public void
    testPlusWeeks(RetailYearWeek start,long weeks,RetailYearWeek expected)
    {
        assertEquals(expected,start.plusWeeks(weeks));
    }

    @Test
    public void
    testMinusWeeks()
    {
        assertEquals(RetailYearWeek.of(NRF,2023,53),RetailYearWeek.of(NRF,2024,1).minusWeeks(1));
    }

    @Test
    public void
    testPlusYearsClamps()
    {
        assertEquals(RetailYearWeek.of(NRF,2024,52),RetailYearWeek.of(NRF,2023,53).plusYears(1));
        assertEquals(RetailYearWeek.of(NRF,2024,52),RetailYearWeek.of(NRF,2023,53).withYear(2024));
    }

    @Test
    public void
    testWithWeekInvalid()
    {
        assertThrows(DateTimeException.class,() -> RetailYearWeek.of(NRF,2024,10).withWeek(53));
    }

    @Test
    public void
    testPlusRetailUnits()
    {
        assertEquals(RetailYearWeek.of(NRF,2024,11),RetailYearWeek.of(NRF,2024,6).plus(1,NRF.months()));
        assertEquals(RetailYearWeek.of(NRF,2024,14),RetailYearWeek.of(NRF,2024,1).plus(1,NRF.quarters()));
    }

    @Test
    public void
    testUntil()
    {
        assertEquals(
            52L,
            RetailYearWeek.of(NRF,2024,1).until(RetailYearWeek.of(NRF,2025,1),ChronoUnit.WEEKS));
        assertEquals(
            53L,
            RetailYearWeek.of(NRF,2023,1).until(RetailYearWeek.of(NRF,2024,1),ChronoUnit.WEEKS));
    }

    @Test
    public void
    testUntilUnsupportedUnit()
    {
        assertThrows(
            UnsupportedTemporalTypeException.class,
            () -> RetailYearWeek.of(NRF,2024,1).until(RetailYearWeek.of(NRF,2025,1),ChronoUnit.DAYS));
    }

    @ParameterizedTest
    @MethodSource("getLongSources")
    public void
    testGetLong(TemporalField field,long expected)
    {
        assertEquals(expected,RetailYearWeek.of(NRF,2024,11).getLong(field));
    }

    @Test
    public void
    testRange()
    {
        assertEquals(ValueRange.of(1,5),RetailYearWeek.of(NRF,2023,53).range(NRF.weekOfMonth()));
    }

    @Test
    public void
    testGetLongUnsupportedField()
    {
        assertThrows(
            UnsupportedTemporalTypeException.class,
            () -> RetailYearWeek.of(NRF,2024,11).getLong(ChronoField.YEAR));
    }

    @Test
    public void
    testWithField()
    {
        RetailYearWeek value = RetailYearWeek.of(NRF,2024,11);

        assertEquals(RetailYearWeek.of(NRF,2024,10),value.with(NRF.weekOfMonth(),1));
        assertEquals(RetailYearWeek.of(NRF,2024,15),value.with(NRF.monthOfYear(),4));
    }

    @Test
    public void
    testIsSupportedField()
    {
        RetailYearWeek value = RetailYearWeek.of(NRF,2024,1);

        assertTrue(value.isSupported(NRF.year()));
        assertTrue(value.isSupported(NRF.quarterOfYear()));
        assertTrue(value.isSupported(NRF.monthOfYear()));
        assertTrue(value.isSupported(NRF.monthOfQuarter()));
        assertTrue(value.isSupported(NRF.weekOfYear()));
        assertTrue(value.isSupported(NRF.weekOfQuarter()));
        assertTrue(value.isSupported(NRF.weekOfMonth()));
        assertFalse(value.isSupported(NRF.dayOfYear()));
        assertFalse(value.isSupported(ChronoField.DAY_OF_WEEK));
    }

    @Test
    public void
    testIsSupportedUnit()
    {
        RetailYearWeek value = RetailYearWeek.of(NRF,2024,1);

        assertTrue(value.isSupported(ChronoUnit.WEEKS));
        assertTrue(value.isSupported(NRF.months()));
        assertTrue(value.isSupported(NRF.quarters()));
        assertTrue(value.isSupported(NRF.years()));
        assertFalse(value.isSupported(ChronoUnit.DAYS));
    }

    @Test
    public void
    testConversions()
    {
        RetailYearWeek value = RetailYearWeek.of(NRF,2023,53);

        assertEquals(RetailYearMonth.of(NRF,2023,12),value.getRetailYearMonth());
        assertEquals(RetailYearQuarter.of(NRF,2023,4),value.getRetailYearQuarter());
        assertEquals(RetailYear.of(NRF,2023),value.getRetailYear());
    }

    @ParameterizedTest
    @MethodSource("atDaySources")
    public void
    testAtDay(DayOfWeek day,LocalDate expected)
    {
        assertEquals(expected,RetailYearWeek.of(NRF,2024,1).atDay(day));
    }

    @Test
    public void
    testAtDayNull()
    {
        NullPointerException e =
            assertThrows(NullPointerException.class,() -> RetailYearWeek.of(NRF,2024,1).atDay(null));

        assertEquals("day is null",e.getMessage());
    }

    @Test
    public void
    testAdjustInto()
    {
        assertEquals(
            LocalDate.of(2024,4,9),
            LocalDate.of(2024,2,6).with(RetailYearWeek.of(NRF,2024,10)));
    }

    @Test
    public void
    testQuery()
    {
        assertEquals(
            ChronoUnit.WEEKS,
            RetailYearWeek.of(NRF,2024,1).query(TemporalQueries.precision()));
    }

    @Test
    public void
    testNow()
    {
        Clock clock = Clock.fixed(Instant.parse("2024-02-04T12:00:00Z"),ZoneOffset.UTC);

        assertEquals(RetailYearWeek.of(NRF,2024,1),RetailYearWeek.now(NRF,clock));
    }

    @Test
    public void
    testParse()
    {
        assertEquals(RetailYearWeek.of(NRF,2024,5),RetailYearWeek.parse("2024-W05[NRF]"));
        assertEquals(RetailYearWeek.of(NRF,2024,5),RetailYearWeek.parse("2024-W05"));
    }

    @ParameterizedTest
    @MethodSource("invalidParseSources")
    public void
    testParseInvalid(String text)
    {
        assertThrows(DateTimeParseException.class,() -> RetailYearWeek.parse(text));
    }

    @ParameterizedTest
    @MethodSource("containsSources")
    public void
    testContains(LocalDate date,boolean expected)
    {
        assertEquals(expected,RetailYearWeek.of(NRF,2024,1).contains(date));
    }

    @Test
    public void
    testCompareToDifferentCalendars()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> RetailYearWeek.of(NRF,2024,1).compareTo(RetailYearWeek.of(OTHER,2024,1)));
    }

    @Test
    public void
    testCompareTo()
    {
        RetailYearWeek first  = RetailYearWeek.of(NRF,2024,1);
        RetailYearWeek second = RetailYearWeek.of(NRF,2024,2);
        RetailYearWeek third  = RetailYearWeek.of(NRF,2025,1);

        assertTrue(first.compareTo(second) < 0);
        assertTrue(second.compareTo(third) < 0);
        assertTrue(third.compareTo(first) > 0);
        assertEquals(0,first.compareTo(RetailYearWeek.of(NRF,2024,1)));
    }

    @Test
    public void
    testEqualsAndHashCode()
    {
        RetailYearWeek value = RetailYearWeek.of(NRF,2024,5);

        assertEquals(value,RetailYearWeek.of(NRF,2024,5));
        assertEquals(value.hashCode(),RetailYearWeek.of(NRF,2024,5).hashCode());
        assertNotEquals(value,RetailYearWeek.of(NRF,2024,6));
        assertNotEquals(value,RetailYearWeek.of(NRF,2025,5));
        assertNotEquals(value,RetailYearWeek.of(OTHER,2024,5));
        assertNotEquals(null,value);
    }

    @Test
    public void
    testSerialization() throws IOException, ClassNotFoundException
    {
        RetailYearWeek expected = RetailYearWeek.of(NRF,2023,53);

        assertEquals(expected,roundTrip(expected));
    }

    @Test
    public void
    testJsonWrite() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapperSupplier().get();

        assertEquals(
            "{\"calendar\":\"NRF\",\"value\":\"2024-W05\"}",
            mapper.writeValueAsString(RetailYearWeek.of(NRF,2024,5)));
    }

    @Test
    public void
    testJsonRead() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapperSupplier().get();

        assertEquals(
            RetailYearWeek.of(NRF,2024,5),
            mapper.readValue("{\"calendar\":\"NRF\",\"value\":\"2024-W05\"}",RetailYearWeek.class));
    }

    @Test
    public void
    testTemporalRange()
    {
        TemporalRange<RetailYearWeek> range =
            new TemporalRange<>(RetailYearWeek.of(NRF,2024,1),RetailYearWeek.of(NRF,2024,5));

        assertEquals(RetailYearWeek.of(NRF,2024,1),range.getBeginning());
        assertEquals(RetailYearWeek.of(NRF,2024,5),range.getEnd());
    }

    @Test
    public void
    testTemporalRangeInvalid()
    {
        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> new TemporalRange<>(RetailYearWeek.of(NRF,2024,5),RetailYearWeek.of(NRF,2024,1)));

        assertEquals("beginning must be before end",e.getMessage());
    }
}

//////////////////////////////////////////////////////////////////////////////
