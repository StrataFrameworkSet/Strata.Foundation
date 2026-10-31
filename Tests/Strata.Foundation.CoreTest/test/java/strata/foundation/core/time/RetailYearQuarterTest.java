//////////////////////////////////////////////////////////////////////////////
// RetailYearQuarterTest.java
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
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("CommitStage")
public
class RetailYearQuarterTest
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

    private RetailYearQuarter
    roundTrip(RetailYearQuarter quarter) throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bytes))
        {
            out.writeObject(quarter);
        }

        try (ObjectInputStream in =
                 new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())))
        {
            return (RetailYearQuarter)in.readObject();
        }
    }

    public static Stream<Arguments>
    firstLastSources()
    {
        return
            Stream.of(
                Arguments.of(2024,1,LocalDate.of(2024,2,4),LocalDate.of(2024,5,4),13),
                Arguments.of(2023,4,LocalDate.of(2023,10,29),LocalDate.of(2024,2,3),14),
                Arguments.of(2024,4,LocalDate.of(2024,11,3),LocalDate.of(2025,2,1),13));
    }

    public static Stream<Arguments>
    fromDateSources()
    {
        return
            Stream.of(
                Arguments.of(LocalDate.of(2024,2,3),RetailYearQuarter.of(RetailCalendar.NRF,2023,4)),
                Arguments.of(LocalDate.of(2024,2,4),RetailYearQuarter.of(RetailCalendar.NRF,2024,1)));
    }

    public static Stream<Arguments>
    containsSources()
    {
        return
            Stream.of(
                Arguments.of(LocalDate.of(2024,2,4),true),
                Arguments.of(LocalDate.of(2024,5,4),true),
                Arguments.of(LocalDate.of(2024,2,3),false),
                Arguments.of(LocalDate.of(2024,5,5),false));
    }

    public static Stream<Arguments>
    invalidParseSources()
    {
        return
            Stream.of(
                Arguments.of("2024-Q5[NRF]"),
                Arguments.of("2024-Q[NRF]"),
                Arguments.of("2024Q1"));
    }

    @ParameterizedTest
    @MethodSource("firstLastSources")
    public void
    testOf(int year,int quarter,LocalDate first,LocalDate last,int weeks)
    {
        RetailYearQuarter value = RetailYearQuarter.of(NRF,year,quarter);

        assertEquals(year,value.getYear());
        assertEquals(quarter,value.getQuarter());
        assertEquals(first,value.getFirstDay());
        assertEquals(last,value.getLastDay());
        assertEquals(weeks,value.lengthInWeeks());
    }

    @Test
    public void
    testFormatAndToString()
    {
        RetailYearQuarter value = RetailYearQuarter.of(NRF,2024,1);

        assertEquals("2024-Q1",value.format());
        assertEquals("2024-Q1[NRF]",value.toString());
    }

    @Test
    public void
    testOfInvalid()
    {
        assertThrows(DateTimeException.class,() -> RetailYearQuarter.of(NRF,2024,5));
        assertThrows(DateTimeException.class,() -> RetailYearQuarter.of(NRF,2024,0));
    }

    @Test
    public void
    testOfDefaultCalendar()
    {
        assertEquals(RetailYearQuarter.of(NRF,2024,1),RetailYearQuarter.of(2024,1));
    }

    @ParameterizedTest
    @MethodSource("fromDateSources")
    public void
    testFromDate(LocalDate date,RetailYearQuarter expected)
    {
        assertEquals(expected,RetailYearQuarter.from(NRF,date));
    }

    @Test
    public void
    testFromRetailYearMonth()
    {
        assertEquals(
            RetailYearQuarter.of(NRF,2024,4),
            RetailYearQuarter.from(NRF,RetailYearMonth.of(NRF,2024,10)));
    }

    @ParameterizedTest
    @MethodSource("containsSources")
    public void
    testContains(LocalDate date,boolean expected)
    {
        assertEquals(expected,RetailYearQuarter.of(NRF,2024,1).contains(date));
    }

    @Test
    public void
    testPlusAndMinusQuarters()
    {
        assertEquals(RetailYearQuarter.of(NRF,2025,1),RetailYearQuarter.of(NRF,2024,4).plusQuarters(1));
        assertEquals(RetailYearQuarter.of(NRF,2023,4),RetailYearQuarter.of(NRF,2024,1).minusQuarters(1));
    }

    @Test
    public void
    testPlusYears()
    {
        assertEquals(RetailYearQuarter.of(NRF,2025,1),RetailYearQuarter.of(NRF,2024,1).plusYears(1));
    }

    @Test
    public void
    testUntil()
    {
        RetailYearQuarter start = RetailYearQuarter.of(NRF,2024,1);
        RetailYearQuarter end   = RetailYearQuarter.of(NRF,2025,1);

        assertEquals(4L,start.until(end,NRF.quarters()));
        assertEquals(1L,start.until(end,NRF.years()));
    }

    @Test
    public void
    testPlusUnsupportedUnit()
    {
        assertThrows(
            UnsupportedTemporalTypeException.class,
            () -> RetailYearQuarter.of(NRF,2024,1).plus(1,NRF.months()));
    }

    @Test
    public void
    testWith()
    {
        RetailYearQuarter value = RetailYearQuarter.of(NRF,2024,1);

        assertEquals(RetailYearQuarter.of(NRF,2024,3),value.withQuarter(3));
        assertEquals(RetailYearQuarter.of(NRF,2025,1),value.withYear(2025));
        assertEquals(RetailYearQuarter.of(NRF,2024,2),value.with(NRF.quarterOfYear(),2));
    }

    @Test
    public void
    testWithQuarterInvalid()
    {
        assertThrows(DateTimeException.class,() -> RetailYearQuarter.of(NRF,2024,1).withQuarter(5));
    }

    @Test
    public void
    testIsSupported()
    {
        RetailYearQuarter value = RetailYearQuarter.of(NRF,2024,1);

        assertTrue(value.isSupported(NRF.year()));
        assertTrue(value.isSupported(NRF.quarterOfYear()));
        assertFalse(value.isSupported(NRF.monthOfYear()));
        assertTrue(value.isSupported(NRF.quarters()));
        assertTrue(value.isSupported(NRF.years()));
        assertFalse(value.isSupported(NRF.months()));
    }

    @Test
    public void
    testGetLong()
    {
        assertEquals(3L,RetailYearQuarter.of(NRF,2024,3).getLong(NRF.quarterOfYear()));
    }

    @Test
    public void
    testConversions()
    {
        RetailYearQuarter value = RetailYearQuarter.of(NRF,2024,2);

        assertEquals(RetailYear.of(NRF,2024),value.getRetailYear());
        assertEquals(RetailYearMonth.of(NRF,2024,5),value.atMonth(2));
        assertEquals(RetailYearWeek.of(NRF,2024,14),value.atWeek(1));
        assertEquals(LocalDate.of(2024,5,5),value.atDay(1));
        assertEquals(LocalDate.of(2024,8,3),value.atDay(91));
    }

    @Test
    public void
    testConversionsInvalid()
    {
        RetailYearQuarter value = RetailYearQuarter.of(NRF,2024,2);

        assertThrows(DateTimeException.class,() -> value.atMonth(4));
        assertThrows(DateTimeException.class,() -> value.atDay(92));
        assertThrows(DateTimeException.class,() -> RetailYearQuarter.of(NRF,2024,4).atWeek(14));
    }

    @Test
    public void
    testAtWeekOfFiftyThreeWeekYear()
    {
        assertEquals(RetailYearWeek.of(NRF,2023,53),RetailYearQuarter.of(NRF,2023,4).atWeek(14));
    }

    @Test
    public void
    testAdjustInto()
    {
        assertEquals(
            LocalDate.of(2024,8,6),
            LocalDate.of(2024,2,6).with(RetailYearQuarter.of(NRF,2024,3)));
    }

    @Test
    public void
    testNow()
    {
        Clock clock = Clock.fixed(Instant.parse("2024-05-05T12:00:00Z"),ZoneOffset.UTC);

        assertEquals(RetailYearQuarter.of(NRF,2024,2),RetailYearQuarter.now(NRF,clock));
    }

    @Test
    public void
    testParse()
    {
        assertEquals(RetailYearQuarter.of(NRF,2024,1),RetailYearQuarter.parse("2024-Q1[NRF]"));
        assertEquals(RetailYearQuarter.of(NRF,2024,1),RetailYearQuarter.parse("2024-Q1"));
    }

    @ParameterizedTest
    @MethodSource("invalidParseSources")
    public void
    testParseInvalid(String text)
    {
        assertThrows(DateTimeParseException.class,() -> RetailYearQuarter.parse(text));
    }

    @Test
    public void
    testCompareToDifferentCalendars()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> RetailYearQuarter.of(NRF,2024,1).compareTo(RetailYearQuarter.of(OTHER,2024,1)));
    }

    @Test
    public void
    testCompareTo()
    {
        RetailYearQuarter first  = RetailYearQuarter.of(NRF,2024,1);
        RetailYearQuarter second = RetailYearQuarter.of(NRF,2024,2);
        RetailYearQuarter third  = RetailYearQuarter.of(NRF,2025,1);

        assertTrue(first.compareTo(second) < 0);
        assertTrue(second.compareTo(third) < 0);
        assertTrue(third.compareTo(first) > 0);
        assertEquals(0,first.compareTo(RetailYearQuarter.of(NRF,2024,1)));
    }

    @Test
    public void
    testEqualsAndHashCode()
    {
        RetailYearQuarter value = RetailYearQuarter.of(NRF,2024,1);

        assertEquals(value,RetailYearQuarter.of(NRF,2024,1));
        assertEquals(value.hashCode(),RetailYearQuarter.of(NRF,2024,1).hashCode());
        assertNotEquals(value,RetailYearQuarter.of(NRF,2024,2));
        assertNotEquals(value,RetailYearQuarter.of(NRF,2025,1));
        assertNotEquals(value,RetailYearQuarter.of(OTHER,2024,1));
        assertNotEquals(null,value);
    }

    @Test
    public void
    testSerialization() throws IOException, ClassNotFoundException
    {
        RetailYearQuarter expected = RetailYearQuarter.of(NRF,2023,4);

        assertEquals(expected,roundTrip(expected));
    }

    @Test
    public void
    testJsonWrite() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapperSupplier().get();

        assertEquals(
            "{\"calendar\":\"NRF\",\"value\":\"2024-Q1\"}",
            mapper.writeValueAsString(RetailYearQuarter.of(NRF,2024,1)));
    }

    @Test
    public void
    testJsonRead() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapperSupplier().get();

        assertEquals(
            RetailYearQuarter.of(NRF,2024,1),
            mapper.readValue("{\"calendar\":\"NRF\",\"value\":\"2024-Q1\"}",RetailYearQuarter.class));
    }

    @Test
    public void
    testTemporalRange()
    {
        TemporalRange<RetailYearQuarter> range =
            new TemporalRange<>(RetailYearQuarter.of(NRF,2024,1),RetailYearQuarter.of(NRF,2024,4));

        assertEquals(RetailYearQuarter.of(NRF,2024,1),range.getBeginning());
        assertEquals(RetailYearQuarter.of(NRF,2024,4),range.getEnd());
    }

    @Test
    public void
    testTemporalRangeInvalid()
    {
        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> new TemporalRange<>(RetailYearQuarter.of(NRF,2024,4),RetailYearQuarter.of(NRF,2024,1)));

        assertEquals("beginning must be before end",e.getMessage());
    }
}

//////////////////////////////////////////////////////////////////////////////
