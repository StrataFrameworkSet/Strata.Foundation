//////////////////////////////////////////////////////////////////////////////
// YearWeekTest.java
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.HijrahDate;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("CommitStage")
public
class YearWeekTest
{
    private YearWeek
    roundTrip(YearWeek yearWeek) throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bytes))
        {
            out.writeObject(yearWeek);
        }

        try (ObjectInputStream in =
                 new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())))
        {
            return (YearWeek)in.readObject();
        }
    }

    public static Stream<Arguments>
    fromSources()
    {
        return
            Stream.of(
                Arguments.of(LocalDate.of(2024,12,30),YearWeek.of(2025,1)),
                Arguments.of(LocalDate.of(2021,1,1),YearWeek.of(2020,53)),
                Arguments.of(LocalDate.of(2023,1,1),YearWeek.of(2022,52)),
                Arguments.of(LocalDate.of(2024,1,1),YearWeek.of(2024,1)),
                Arguments.of(LocalDateTime.of(2024,12,30,10,0),YearWeek.of(2025,1)),
                Arguments.of(
                    ZonedDateTime.of(
                        2021,1,1,10,0,0,0,ZoneId.of("America/New_York")),
                    YearWeek.of(2020,53)),
                Arguments.of(HijrahDate.from(LocalDate.of(2024,1,1)),YearWeek.of(2024,1)));
    }

    public static Stream<Arguments>
    yearLengthSources()
    {
        return
            Stream.of(
                Arguments.of(2020,53,true),
                Arguments.of(2023,52,false),
                Arguments.of(2024,52,false),
                Arguments.of(2026,53,true));
    }

    public static Stream<Arguments>
    firstLastDaySources()
    {
        return
            Stream.of(
                Arguments.of(
                    YearWeek.of(2024,1),LocalDate.of(2024,1,1),LocalDate.of(2024,1,7)),
                Arguments.of(
                    YearWeek.of(2020,53),LocalDate.of(2020,12,28),LocalDate.of(2021,1,3)),
                Arguments.of(
                    YearWeek.of(2025,1),LocalDate.of(2024,12,30),LocalDate.of(2025,1,5)));
    }

    public static Stream<Arguments>
    containsSources()
    {
        return
            Stream.of(
                Arguments.of(LocalDate.of(2024,1,1),true),
                Arguments.of(LocalDate.of(2024,1,7),true),
                Arguments.of(LocalDate.of(2023,12,31),false),
                Arguments.of(LocalDate.of(2024,1,8),false));
    }

    public static Stream<Arguments>
    plusWeeksSources()
    {
        return
            Stream.of(
                Arguments.of(YearWeek.of(2020,52),1L,YearWeek.of(2020,53)),
                Arguments.of(YearWeek.of(2020,52),2L,YearWeek.of(2021,1)),
                Arguments.of(YearWeek.of(2021,1),-1L,YearWeek.of(2020,53)));
    }

    public static Stream<Arguments>
    plusYearsSources()
    {
        return
            Stream.of(
                Arguments.of(YearWeek.of(2020,53),1L,YearWeek.of(2021,52)),
                Arguments.of(YearWeek.of(2020,53),6L,YearWeek.of(2026,53)),
                Arguments.of(YearWeek.of(2024,10),-4L,YearWeek.of(2020,10)));
    }

    public static Stream<Arguments>
    untilSources()
    {
        return
            Stream.of(
                Arguments.of(
                    YearWeek.of(2024,1),YearWeek.of(2025,1),ChronoUnit.WEEKS,52L),
                Arguments.of(
                    YearWeek.of(2020,1),YearWeek.of(2021,1),ChronoUnit.WEEKS,53L),
                Arguments.of(
                    YearWeek.of(2024,1),LocalDate.of(2024,1,15),ChronoUnit.WEEKS,2L),
                Arguments.of(
                    YearWeek.of(2020,10),YearWeek.of(2023,10),IsoFields.WEEK_BASED_YEARS,3L),
                Arguments.of(
                    YearWeek.of(2020,10),YearWeek.of(2023,9),IsoFields.WEEK_BASED_YEARS,2L),
                Arguments.of(
                    YearWeek.of(2020,53),YearWeek.of(2021,52),IsoFields.WEEK_BASED_YEARS,0L),
                Arguments.of(
                    YearWeek.of(2023,10),YearWeek.of(2020,10),IsoFields.WEEK_BASED_YEARS,-3L));
    }

    public static Stream<Arguments>
    invalidParseSources()
    {
        return
            Stream.of(
                Arguments.of("2023-W53"),
                Arguments.of("2024-W5"),
                Arguments.of("2024W05"),
                Arguments.of("2024-W05x"));
    }

    @Test
    public void
    testOf()
    {
        YearWeek yearWeek = YearWeek.of(2024,5);

        assertEquals(2024,yearWeek.getYear());
        assertEquals(5,yearWeek.getWeek());
    }

    @Test
    public void
    testOfInvalid()
    {
        assertThrows(DateTimeException.class,() -> YearWeek.of(2024,0));
        assertThrows(DateTimeException.class,() -> YearWeek.of(2023,53));
    }

    @Test
    public void
    testOf53WeekYears()
    {
        assertEquals(53,YearWeek.of(2020,53).getWeek());
        assertEquals(53,YearWeek.of(2026,53).getWeek());
    }

    @ParameterizedTest
    @MethodSource("yearLengthSources")
    public void
    testLengthOfYear(int year,int length,boolean is53)
    {
        YearWeek yearWeek = YearWeek.of(year,1);

        assertEquals(length,yearWeek.lengthOfYear());
        assertEquals(is53,yearWeek.is53WeekYear());
    }

    @ParameterizedTest
    @MethodSource("fromSources")
    public void
    testFrom(TemporalAccessor temporal,YearWeek expected)
    {
        assertEquals(expected,YearWeek.from(temporal));
    }

    @Test
    public void
    testFromYearWeek()
    {
        YearWeek yearWeek = YearWeek.of(2024,5);

        assertSame(yearWeek,YearWeek.from(yearWeek));
    }

    @Test
    public void
    testFromUnsupported()
    {
        assertThrows(DateTimeException.class,() -> YearWeek.from(LocalTime.of(10,0)));
    }

    @Test
    public void
    testFromNull()
    {
        NullPointerException e =
            assertThrows(NullPointerException.class,() -> YearWeek.from(null));

        assertEquals("temporal is null",e.getMessage());
    }

    @ParameterizedTest
    @MethodSource("firstLastDaySources")
    public void
    testFirstAndLastDay(YearWeek yearWeek,LocalDate first,LocalDate last)
    {
        assertEquals(first,yearWeek.getFirstDay());
        assertEquals(last,yearWeek.getLastDay());
    }

    @ParameterizedTest
    @MethodSource("containsSources")
    public void
    testContains(LocalDate date,boolean expected)
    {
        assertEquals(expected,YearWeek.of(2024,1).contains(date));
    }

    @Test
    public void
    testContainsNull()
    {
        NullPointerException e =
            assertThrows(NullPointerException.class,() -> YearWeek.of(2024,1).contains(null));

        assertEquals("date is null",e.getMessage());
    }

    @Test
    public void
    testAtDay()
    {
        YearWeek yearWeek = YearWeek.of(2024,1);

        assertEquals(LocalDate.of(2024,1,7),yearWeek.atDay(DayOfWeek.SUNDAY));
        assertEquals(LocalDate.of(2024,1,1),yearWeek.atDay(DayOfWeek.MONDAY));
    }

    @Test
    public void
    testAtDayNull()
    {
        NullPointerException e =
            assertThrows(NullPointerException.class,() -> YearWeek.of(2024,1).atDay(null));

        assertEquals("day is null",e.getMessage());
    }

    @ParameterizedTest
    @MethodSource("plusWeeksSources")
    public void
    testPlusWeeks(YearWeek start,long weeks,YearWeek expected)
    {
        assertEquals(expected,start.plusWeeks(weeks));
    }

    @Test
    public void
    testMinusWeeks()
    {
        assertEquals(YearWeek.of(2020,53),YearWeek.of(2021,1).minusWeeks(1));
    }

    @Test
    public void
    testPlusWeeksZero()
    {
        YearWeek yearWeek = YearWeek.of(2024,5);

        assertSame(yearWeek,yearWeek.plusWeeks(0));
    }

    @ParameterizedTest
    @MethodSource("plusYearsSources")
    public void
    testPlusYears(YearWeek start,long years,YearWeek expected)
    {
        assertEquals(expected,start.plusYears(years));
    }

    @Test
    public void
    testMinusYears()
    {
        assertEquals(YearWeek.of(2020,10),YearWeek.of(2024,10).minusYears(4));
    }

    @Test
    public void
    testWithYearClamps()
    {
        assertEquals(YearWeek.of(2021,52),YearWeek.of(2020,53).withYear(2021));
    }

    @Test
    public void
    testWithWeekInvalid()
    {
        assertThrows(DateTimeException.class,() -> YearWeek.of(2023,10).withWeek(53));
    }

    @Test
    public void
    testPlusUnits()
    {
        YearWeek yearWeek = YearWeek.of(2024,1);

        assertEquals(YearWeek.of(2024,3),yearWeek.plus(2,ChronoUnit.WEEKS));
        assertEquals(YearWeek.of(2025,1),yearWeek.plus(1,IsoFields.WEEK_BASED_YEARS));
        assertEquals(YearWeek.of(2034,1),yearWeek.plus(1,ChronoUnit.DECADES));
    }

    @Test
    public void
    testMinusUnits()
    {
        assertEquals(YearWeek.of(2023,52),YearWeek.of(2024,1).minus(1,ChronoUnit.WEEKS));
    }

    @Test
    public void
    testPlusUnsupportedUnit()
    {
        assertThrows(
            UnsupportedTemporalTypeException.class,
            () -> YearWeek.of(2024,1).plus(1,ChronoUnit.DAYS));
    }

    @Test
    public void
    testWithField()
    {
        assertEquals(
            YearWeek.of(2024,20),
            YearWeek.of(2024,10).with(IsoFields.WEEK_OF_WEEK_BASED_YEAR,20));
        assertEquals(
            YearWeek.of(2021,52),
            YearWeek.of(2020,53).with(IsoFields.WEEK_BASED_YEAR,2021));
    }

    @Test
    public void
    testWithFieldInvalid()
    {
        assertThrows(
            DateTimeException.class,
            () -> YearWeek.of(2023,10).with(IsoFields.WEEK_OF_WEEK_BASED_YEAR,53));
    }

    @Test
    public void
    testWithUnsupportedField()
    {
        assertThrows(
            UnsupportedTemporalTypeException.class,
            () -> YearWeek.of(2024,10).with(ChronoField.YEAR,2024));
    }

    @ParameterizedTest
    @MethodSource("untilSources")
    public void
    testUntil(YearWeek start,Temporal end,TemporalUnit unit,long expected)
    {
        assertEquals(expected,start.until(end,unit));
    }

    @Test
    public void
    testUntilUnsupportedUnit()
    {
        assertThrows(
            UnsupportedTemporalTypeException.class,
            () -> YearWeek.of(2024,1).until(YearWeek.of(2025,1),ChronoUnit.DAYS));
    }

    @Test
    public void
    testIsSupportedField()
    {
        YearWeek yearWeek = YearWeek.of(2024,1);

        assertTrue(yearWeek.isSupported(IsoFields.WEEK_BASED_YEAR));
        assertTrue(yearWeek.isSupported(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
        assertFalse(yearWeek.isSupported(ChronoField.YEAR));
        assertFalse(yearWeek.isSupported(ChronoField.DAY_OF_WEEK));
    }

    @Test
    public void
    testIsSupportedUnit()
    {
        YearWeek yearWeek = YearWeek.of(2024,1);

        assertTrue(yearWeek.isSupported(ChronoUnit.WEEKS));
        assertTrue(yearWeek.isSupported(IsoFields.WEEK_BASED_YEARS));
        assertTrue(yearWeek.isSupported(ChronoUnit.DECADES));
        assertFalse(yearWeek.isSupported(ChronoUnit.DAYS));
        assertFalse(yearWeek.isSupported(ChronoUnit.MONTHS));
    }

    @Test
    public void
    testRange()
    {
        assertEquals(
            ValueRange.of(1,53),
            YearWeek.of(2020,1).range(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
        assertEquals(
            ValueRange.of(1,52),
            YearWeek.of(2023,1).range(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
    }

    @Test
    public void
    testRangeUnsupportedField()
    {
        assertThrows(
            UnsupportedTemporalTypeException.class,
            () -> YearWeek.of(2024,5).range(ChronoField.YEAR));
    }

    @Test
    public void
    testGetLong()
    {
        YearWeek yearWeek = YearWeek.of(2024,5);

        assertEquals(2024L,yearWeek.getLong(IsoFields.WEEK_BASED_YEAR));
        assertEquals(5L,yearWeek.getLong(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
    }

    @Test
    public void
    testGetLongUnsupportedField()
    {
        assertThrows(
            UnsupportedTemporalTypeException.class,
            () -> YearWeek.of(2024,5).getLong(ChronoField.YEAR));
    }

    @Test
    public void
    testQuery()
    {
        YearWeek yearWeek = YearWeek.of(2024,5);

        assertEquals(ChronoUnit.WEEKS,yearWeek.query(TemporalQueries.precision()));
        assertEquals(IsoChronology.INSTANCE,yearWeek.query(TemporalQueries.chronology()));
        assertNull(yearWeek.query(TemporalQueries.localDate()));
    }

    @Test
    public void
    testAdjustInto()
    {
        assertEquals(
            LocalDate.of(2025,1,1),
            LocalDate.of(2024,6,12).with(YearWeek.of(2025,1)));
        assertEquals(
            LocalDate.of(2020,12,28),
            LocalDate.of(2024,6,10).with(YearWeek.of(2020,53)));
    }

    @Test
    public void
    testNow()
    {
        Clock clock = Clock.fixed(Instant.parse("2024-12-30T12:00:00Z"),ZoneOffset.UTC);

        assertEquals(YearWeek.of(2025,1),YearWeek.now(clock));
    }

    @Test
    public void
    testNowNullClock()
    {
        NullPointerException e =
            assertThrows(NullPointerException.class,() -> YearWeek.now((Clock)null));

        assertEquals("clock is null",e.getMessage());
    }

    @Test
    public void
    testFormat()
    {
        assertEquals("2024-W05",YearWeek.of(2024,5).format());
        assertEquals("2024-W05",YearWeek.of(2024,5).toString());
        assertEquals("2020-W53",YearWeek.of(2020,53).format());
    }

    @Test
    public void
    testParse()
    {
        YearWeek yearWeek = YearWeek.of(2020,53);

        assertEquals(YearWeek.of(2024,5),YearWeek.parse("2024-W05"));
        assertEquals(yearWeek,YearWeek.parse(yearWeek.format()));
    }

    @ParameterizedTest
    @MethodSource("invalidParseSources")
    public void
    testParseInvalid(String text)
    {
        assertThrows(DateTimeParseException.class,() -> YearWeek.parse(text));
    }

    @Test
    public void
    testParseNull()
    {
        NullPointerException e =
            assertThrows(NullPointerException.class,() -> YearWeek.parse(null));

        assertEquals("text is null",e.getMessage());
    }

    @Test
    public void
    testCompareTo()
    {
        YearWeek first  = YearWeek.of(2024,1);
        YearWeek second = YearWeek.of(2024,2);
        YearWeek third  = YearWeek.of(2025,1);

        assertTrue(first.compareTo(second) < 0);
        assertTrue(second.compareTo(third) < 0);
        assertTrue(third.compareTo(first) > 0);
        assertEquals(0,first.compareTo(YearWeek.of(2024,1)));
        assertTrue(first.isBefore(second));
        assertTrue(third.isAfter(second));
        assertFalse(first.isAfter(second));
        assertFalse(third.isBefore(second));
    }

    @Test
    public void
    testIsBeforeNull()
    {
        NullPointerException e =
            assertThrows(NullPointerException.class,() -> YearWeek.of(2024,1).isBefore(null));

        assertEquals("other is null",e.getMessage());
    }

    @Test
    public void
    testIsAfterNull()
    {
        assertThrows(NullPointerException.class,() -> YearWeek.of(2024,1).isAfter(null));
    }

    @Test
    public void
    testEqualsAndHashCode()
    {
        YearWeek yearWeek = YearWeek.of(2024,5);

        assertEquals(yearWeek,YearWeek.of(2024,5));
        assertEquals(yearWeek.hashCode(),YearWeek.of(2024,5).hashCode());
        assertNotEquals(yearWeek,YearWeek.of(2024,6));
        assertNotEquals(yearWeek,YearWeek.of(2025,5));
        assertNotEquals(null,yearWeek);
        assertNotEquals("2024-W05",yearWeek);
    }

    @Test
    public void
    testSerialization() throws IOException, ClassNotFoundException
    {
        YearWeek expected = YearWeek.of(2020,53);

        assertEquals(expected,roundTrip(expected));
    }

    @Test
    public void
    testJsonWrite() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapperSupplier().get();

        assertEquals("{\"value\":\"2024-W05\"}",mapper.writeValueAsString(YearWeek.of(2024,5)));
    }

    @Test
    public void
    testJsonRead() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapperSupplier().get();

        assertEquals(
            YearWeek.of(2024,5),
            mapper.readValue("{\"value\":\"2024-W05\"}",YearWeek.class));
    }

    @Test
    public void
    testJsonRoundTrip() throws JsonProcessingException
    {
        ObjectMapper mapper   = new ObjectMapperSupplier().get();
        YearWeek     expected = YearWeek.of(2020,53);

        assertEquals(
            expected,
            mapper.readValue(mapper.writeValueAsString(expected),YearWeek.class));
    }

    @Test
    public void
    testTemporalRange()
    {
        TemporalRange<YearWeek> range =
            new TemporalRange<>(YearWeek.of(2024,1),YearWeek.of(2024,5));

        assertEquals(YearWeek.of(2024,1),range.getBeginning());
        assertEquals(YearWeek.of(2024,5),range.getEnd());
    }

    @Test
    public void
    testTemporalRangeInvalid()
    {
        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> new TemporalRange<>(YearWeek.of(2024,5),YearWeek.of(2024,1)));

        assertEquals("beginning must be before end",e.getMessage());
    }
}

//////////////////////////////////////////////////////////////////////////////
