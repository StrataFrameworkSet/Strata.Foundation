//////////////////////////////////////////////////////////////////////////////
// TemporalRangeTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("CommitStage")
public
class TemporalRangeTest
{
    private static final ZoneId NEW_YORK = ZoneId.of("America/New_York");

    private static <T extends Temporal> Arguments
    valid(T beginning,T end)
    {
        Supplier<TemporalRange<?>> range = () -> new TemporalRange<>(beginning,end);
        Supplier<TemporalRange<?>> factory = () -> TemporalRange.ofTemporal(beginning,end);

        return Arguments.of(range,factory,beginning,end);
    }

    private static <T extends Temporal> Arguments
    reversed(T beginning,T end)
    {
        Supplier<TemporalRange<?>> range = () -> new TemporalRange<>(end,beginning);

        return Arguments.of(range);
    }

    private static <T extends Temporal> Arguments
    equalEndpoints(T value)
    {
        Supplier<TemporalRange<?>> range = () -> new TemporalRange<>(value,value);

        return Arguments.of(range,value);
    }

    public static Stream<Arguments>
    validSources()
    {
        return
            Stream.of(
                valid(
                    LocalDateTime.of(2024,1,1,10,0),
                    LocalDateTime.of(2024,1,3,13,0)),
                valid(
                    ZonedDateTime.of(2024,1,1,10,0,0,0,NEW_YORK),
                    ZonedDateTime.of(2024,1,3,13,0,0,0,NEW_YORK)),
                valid(
                    OffsetDateTime.of(2024,1,1,10,0,0,0,ZoneOffset.ofHours(-5)),
                    OffsetDateTime.of(2024,1,3,13,0,0,0,ZoneOffset.ofHours(-5))),
                valid(LocalTime.of(10,0),LocalTime.of(11,30)),
                valid(
                    Instant.parse("2024-01-01T10:00:00Z"),
                    Instant.parse("2024-01-03T13:00:00Z")),
                valid(LocalDate.of(2024,1,1),LocalDate.of(2024,1,11)),
                valid(
                    OffsetTime.of(10,0,0,0,ZoneOffset.UTC),
                    OffsetTime.of(11,30,0,0,ZoneOffset.UTC)),
                valid(Year.of(2024),Year.of(2026)),
                valid(YearMonth.of(2024,11),YearMonth.of(2025,2)));
    }

    public static Stream<Arguments>
    reversedSources()
    {
        return
            Stream.of(
                reversed(
                    LocalDateTime.of(2024,1,1,10,0),
                    LocalDateTime.of(2024,1,3,13,0)),
                reversed(
                    ZonedDateTime.of(2024,1,1,10,0,0,0,NEW_YORK),
                    ZonedDateTime.of(2024,1,3,13,0,0,0,NEW_YORK)),
                reversed(
                    OffsetDateTime.of(2024,1,1,10,0,0,0,ZoneOffset.UTC),
                    OffsetDateTime.of(2024,1,3,13,0,0,0,ZoneOffset.UTC)),
                reversed(LocalTime.of(10,0),LocalTime.of(11,30)),
                reversed(
                    Instant.parse("2024-01-01T10:00:00Z"),
                    Instant.parse("2024-01-03T13:00:00Z")),
                reversed(LocalDate.of(2024,1,1),LocalDate.of(2024,1,11)),
                reversed(
                    OffsetTime.of(10,0,0,0,ZoneOffset.UTC),
                    OffsetTime.of(11,30,0,0,ZoneOffset.UTC)),
                reversed(Year.of(2024),Year.of(2026)),
                reversed(YearMonth.of(2024,11),YearMonth.of(2025,2)));
    }

    public static Stream<Arguments>
    equalEndpointSources()
    {
        return
            Stream.of(
                equalEndpoints(LocalDateTime.of(2024,1,1,10,0)),
                equalEndpoints(ZonedDateTime.of(2024,1,1,10,0,0,0,NEW_YORK)),
                equalEndpoints(
                    OffsetDateTime.of(2024,1,1,10,0,0,0,ZoneOffset.UTC)),
                equalEndpoints(LocalTime.of(10,0)),
                equalEndpoints(Instant.parse("2024-01-01T10:00:00Z")),
                equalEndpoints(LocalDate.of(2024,1,1)),
                equalEndpoints(OffsetTime.of(10,0,0,0,ZoneOffset.UTC)),
                equalEndpoints(Year.of(2024)),
                equalEndpoints(YearMonth.of(2024,11)));
    }

    @ParameterizedTest
    @MethodSource("validSources")
    public void
    testConstructorAndGetters(
        Supplier<TemporalRange<?>> range,
        Supplier<TemporalRange<?>> factory,
        Object beginning,
        Object end)
    {
        TemporalRange<?> actual = range.get();

        assertEquals(beginning,actual.getBeginning());
        assertEquals(end,actual.getEnd());
        assertEquals(range.get(),actual);
    }

    @ParameterizedTest
    @MethodSource("validSources")
    public void
    testOfTemporalAndGetters(
        Supplier<TemporalRange<?>> range,
        Supplier<TemporalRange<?>> factory,
        Object beginning,
        Object end)
    {
        TemporalRange<?> actual = factory.get();

        assertEquals(beginning,actual.getBeginning());
        assertEquals(end,actual.getEnd());
        assertEquals(range.get(),actual);
    }

    @ParameterizedTest
    @MethodSource("equalEndpointSources")
    public void
    testEqualEndpointsAllowed(Supplier<TemporalRange<?>> range,Object value)
    {
        TemporalRange<?> actual = range.get();

        assertEquals(value,actual.getBeginning());
        assertEquals(value,actual.getEnd());
    }

    @ParameterizedTest
    @MethodSource("reversedSources")
    public void
    testReversedThrows(Supplier<TemporalRange<?>> range)
    {
        IllegalArgumentException e =
            assertThrows(IllegalArgumentException.class,range::get);

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testNullBeginningThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> new TemporalRange<Instant>(
                    null,Instant.parse("2024-01-01T00:00:00Z")));

        assertEquals("beginning is null",e.getMessage());
    }

    @Test
    public void
    testNullEndThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> new TemporalRange<Instant>(
                    Instant.parse("2024-01-01T00:00:00Z"),null));

        assertEquals("end is null",e.getMessage());
    }

    @Test
    public void
    testNullConstructorArgumentsThrow()
    {
        assertThrows(
            NullPointerException.class,
            () -> new TemporalRange<LocalDate>(null,LocalDate.of(2024,1,1)));
        assertThrows(
            NullPointerException.class,
            () -> new TemporalRange<LocalDate>(LocalDate.of(2024,1,1),null));
    }

    @Test
    public void
    testGetDurationLocalDateTime()
    {
        TemporalRange<LocalDateTime> range =
            new TemporalRange<>(
                LocalDateTime.of(2024,1,1,0,0),
                LocalDateTime.of(2024,1,3,3,0));

        assertEquals(Duration.ofDays(2).plusHours(3),range.getDuration());
    }

    @Test
    public void
    testGetDurationLocalTime()
    {
        TemporalRange<LocalTime> range =
            new TemporalRange<>(LocalTime.of(10,0),LocalTime.of(11,30));

        assertEquals(Duration.ofMinutes(90),range.getDuration());
    }

    @Test
    public void
    testGetDurationZonedDateTimeAcrossDaylightSavingTransition()
    {
        TemporalRange<ZonedDateTime> range =
            new TemporalRange<>(
                ZonedDateTime.of(2024,3,10,0,0,0,0,NEW_YORK),
                ZonedDateTime.of(2024,3,10,4,0,0,0,NEW_YORK));

        assertEquals(Duration.ofHours(3),range.getDuration());
    }

    @Test
    public void
    testGetDurationOffsetDateTime()
    {
        TemporalRange<OffsetDateTime> range =
            new TemporalRange<>(
                OffsetDateTime.of(2024,1,1,0,0,0,0,ZoneOffset.UTC),
                OffsetDateTime.of(2024,1,1,2,30,0,0,ZoneOffset.UTC));

        assertEquals(Duration.ofMinutes(150),range.getDuration());
    }

    @Test
    public void
    testGetDurationLocalDateUsesDays()
    {
        TemporalRange<LocalDate> range =
            new TemporalRange<>(LocalDate.of(2024,1,1),LocalDate.of(2024,1,11));

        assertEquals(Duration.ofDays(10),range.getDuration());
    }

    @Test
    public void
    testGetDurationZeroLength()
    {
        Instant instant = Instant.parse("2024-01-01T00:00:00Z");
        LocalDate date = LocalDate.of(2024,1,1);

        assertEquals(Duration.ZERO,new TemporalRange<>(instant,instant).getDuration());
        assertEquals(Duration.ZERO,new TemporalRange<>(date,date).getDuration());
    }

    @Test
    public void
    testGetPeriodLocalDateTime()
    {
        TemporalRange<LocalDateTime> range =
            new TemporalRange<>(
                LocalDateTime.of(2024,1,15,10,0),
                LocalDateTime.of(2024,3,20,9,0));

        assertEquals(Period.of(0,2,5),range.getPeriod());
    }

    @Test
    public void
    testGetPeriodZonedDateTimeUsesLocalDates()
    {
        TemporalRange<ZonedDateTime> range =
            new TemporalRange<>(
                ZonedDateTime.of(2024,1,31,23,0,0,0,ZoneOffset.ofHours(-5)),
                ZonedDateTime.of(2024,3,1,1,0,0,0,ZoneOffset.ofHours(-5)));

        assertEquals(Period.of(0,1,1),range.getPeriod());
    }

    @Test
    public void
    testGetPeriodInstantUsesUtcDates()
    {
        TemporalRange<Instant> range =
            new TemporalRange<>(
                Instant.parse("2024-01-31T23:00:00Z"),
                Instant.parse("2024-03-01T01:00:00Z"));

        assertEquals(Period.of(0,1,1),range.getPeriod());
    }

    @Test
    public void
    testGetPeriodLocalTimeThrows()
    {
        TemporalRange<LocalTime> range =
            new TemporalRange<>(LocalTime.of(10,0),LocalTime.of(11,0));

        UnsupportedTemporalTypeException e =
            assertThrows(UnsupportedTemporalTypeException.class,range::getPeriod);

        assertEquals("getPeriod is not supported for java.time.LocalTime",e.getMessage());
    }

    @Test
    public void
    testGetPeriodOffsetTimeThrows()
    {
        TemporalRange<OffsetTime> range =
            new TemporalRange<>(
                OffsetTime.of(10,0,0,0,ZoneOffset.UTC),
                OffsetTime.of(11,0,0,0,ZoneOffset.UTC));

        UnsupportedTemporalTypeException e =
            assertThrows(UnsupportedTemporalTypeException.class,range::getPeriod);

        assertEquals("getPeriod is not supported for java.time.OffsetTime",e.getMessage());
    }

    @Test
    public void
    testGetDurationOffsetTime()
    {
        TemporalRange<OffsetTime> range =
            new TemporalRange<>(
                OffsetTime.of(10,0,0,0,ZoneOffset.UTC),
                OffsetTime.of(11,30,0,0,ZoneOffset.UTC));

        assertEquals(Duration.ofMinutes(90),range.getDuration());
    }

    @ParameterizedTest
    @MethodSource("yearMonthPeriodSources")
    public void
    testGetPeriodYearMonth(YearMonth beginning,YearMonth end,Period expected)
    {
        assertEquals(expected,new TemporalRange<>(beginning,end).getPeriod());
    }

    public static Stream<Arguments>
    yearMonthPeriodSources()
    {
        return
            Stream.of(
                Arguments.of(YearMonth.of(2024,11),YearMonth.of(2026,1),Period.of(1,2,0)),
                Arguments.of(YearMonth.of(2024,1),YearMonth.of(2024,12),Period.ofMonths(11)),
                Arguments.of(YearMonth.of(2024,5),YearMonth.of(2024,5),Period.ZERO));
    }

    @ParameterizedTest
    @MethodSource("yearPeriodSources")
    public void
    testGetPeriodYear(Year beginning,Year end,Period expected)
    {
        assertEquals(expected,new TemporalRange<>(beginning,end).getPeriod());
    }

    public static Stream<Arguments>
    yearPeriodSources()
    {
        return
            Stream.of(
                Arguments.of(Year.of(2020),Year.of(2024),Period.ofYears(4)),
                Arguments.of(Year.of(2024),Year.of(2024),Period.ZERO));
    }

    @ParameterizedTest
    @MethodSource("yearMonthDurationSources")
    public void
    testGetDurationYearMonth(YearMonth beginning,YearMonth end,Duration expected)
    {
        assertEquals(expected,new TemporalRange<>(beginning,end).getDuration());
    }

    public static Stream<Arguments>
    yearMonthDurationSources()
    {
        return
            Stream.of(
                Arguments.of(YearMonth.of(2024,1),YearMonth.of(2024,3),Duration.ofDays(60)),
                Arguments.of(YearMonth.of(2023,1),YearMonth.of(2023,3),Duration.ofDays(59)),
                Arguments.of(YearMonth.of(2024,5),YearMonth.of(2024,5),Duration.ZERO));
    }

    @ParameterizedTest
    @MethodSource("yearDurationSources")
    public void
    testGetDurationYear(Year beginning,Year end,Duration expected)
    {
        assertEquals(expected,new TemporalRange<>(beginning,end).getDuration());
    }

    public static Stream<Arguments>
    yearDurationSources()
    {
        return
            Stream.of(
                Arguments.of(Year.of(2024),Year.of(2025),Duration.ofDays(366)),
                Arguments.of(Year.of(2023),Year.of(2024),Duration.ofDays(365)),
                Arguments.of(Year.of(2024),Year.of(2024),Duration.ZERO));
    }

    @Test
    public void
    testUnsupportedTemporalThrows()
    {
        TemporalRange<?> range = unsupportedRange();
        String           type = range.getBeginning().getClass().getName();

        UnsupportedTemporalTypeException period =
            assertThrows(UnsupportedTemporalTypeException.class,range::getPeriod);
        UnsupportedTemporalTypeException duration =
            assertThrows(UnsupportedTemporalTypeException.class,range::getDuration);

        assertEquals("getPeriod is not supported for " + type,period.getMessage());
        assertEquals("getDuration is not supported for " + type,duration.getMessage());
    }

    private TemporalRange<?>
    unsupportedRange()
    {
        class Unsupported
            implements Temporal, Comparable<Unsupported>
        {
            private final int value;

            Unsupported(int value) { this.value = value; }

            @Override
            public int
            compareTo(Unsupported other) { return Integer.compare(value,other.value); }

            @Override
            public boolean
            isSupported(TemporalUnit unit) { return false; }

            @Override
            public boolean
            isSupported(TemporalField field) { return false; }

            @Override
            public long
            getLong(TemporalField field)
            {
                throw new UnsupportedTemporalTypeException(field.toString());
            }

            @Override
            public Temporal
            with(TemporalField field,long newValue)
            {
                throw new UnsupportedTemporalTypeException(field.toString());
            }

            @Override
            public Temporal
            plus(long amountToAdd,TemporalUnit unit)
            {
                throw new UnsupportedTemporalTypeException(unit.toString());
            }

            @Override
            public long
            until(Temporal endExclusive,TemporalUnit unit)
            {
                throw new UnsupportedTemporalTypeException(unit.toString());
            }
        }

        return new TemporalRange<>(new Unsupported(1),new Unsupported(2));
    }

    @Test
    public void
    testEqualsAndHashCode()
    {
        Instant a = Instant.parse("2024-01-01T00:00:00Z");
        Instant b = Instant.parse("2024-01-02T00:00:00Z");
        Instant c = Instant.parse("2024-01-03T00:00:00Z");

        assertEquals(new TemporalRange<>(a,b),new TemporalRange<>(a,b));
        assertEquals(
            new TemporalRange<>(a,b).hashCode(),
            new TemporalRange<>(a,b).hashCode());
        assertNotEquals(new TemporalRange<>(a,b),new TemporalRange<>(a,c));
        assertNotEquals(new TemporalRange<>(a,b),new TemporalRange<>(b,c));
    }

    @Test
    public void
    testNotEqualToInstantRangeWithSameValues()
    {
        Instant a = Instant.parse("2024-01-01T00:00:00Z");
        Instant b = Instant.parse("2024-01-02T00:00:00Z");
        TemporalRange<Instant> base = new TemporalRange<>(a,b);
        InstantRange derived = InstantRange.of(a,b);

        assertNotEquals(base,derived);
        assertNotEquals(derived,base);
    }

    @Test
    public void
    testNotEqualToLocalDateRangeWithSameValues()
    {
        LocalDate a = LocalDate.of(2024,1,1);
        LocalDate b = LocalDate.of(2024,1,2);
        TemporalRange<LocalDate> base = new TemporalRange<>(a,b);
        LocalDateRange derived = LocalDateRange.of(a,b);

        assertNotEquals(base,derived);
        assertNotEquals(derived,base);
    }

    @Test
    public void
    testZonedDateTimeOrderedByInstant()
    {
        ZonedDateTime newYork = ZonedDateTime.of(2024,1,1,10,0,0,0,NEW_YORK);
        ZonedDateTime utc = ZonedDateTime.of(2024,1,1,12,0,0,0,ZoneOffset.UTC);

        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> new TemporalRange<>(newYork,utc));

        assertEquals("beginning must be before end",e.getMessage());
        assertEquals(Duration.ofHours(3),new TemporalRange<>(utc,newYork).getDuration());
    }

    @Test
    public void
    testSameInstantInDifferentZonesAllowed()
    {
        ZonedDateTime utc = ZonedDateTime.of(2024,6,1,12,0,0,0,ZoneOffset.UTC);
        ZonedDateTime tokyo = utc.withZoneSameInstant(ZoneId.of("Asia/Tokyo"));
        TemporalRange<ZonedDateTime> range = new TemporalRange<>(tokyo,utc);

        assertEquals(tokyo,range.getBeginning());
        assertEquals(utc,range.getEnd());
        assertEquals(Duration.ZERO,range.getDuration());
    }

    @Test
    public void
    testSameMomentsInDifferentZonesNotEqual()
    {
        ZoneId        tokyo = ZoneId.of("Asia/Tokyo");
        ZonedDateTime utcBeginning = ZonedDateTime.of(2024,6,1,12,0,0,0,ZoneOffset.UTC);
        ZonedDateTime utcEnd = ZonedDateTime.of(2024,6,1,14,0,0,0,ZoneOffset.UTC);
        TemporalRange<ZonedDateTime> utcRange =
            new TemporalRange<>(utcBeginning,utcEnd);
        TemporalRange<ZonedDateTime> tokyoRange =
            new TemporalRange<>(
                utcBeginning.withZoneSameInstant(tokyo),
                utcEnd.withZoneSameInstant(tokyo));

        assertEquals(utcRange.getDuration(),tokyoRange.getDuration());
        assertNotEquals(utcRange,tokyoRange);
        assertNotEquals(tokyoRange,utcRange);
    }

    @Test
    public void
    testDifferentTypesThrow()
    {
        ZonedDateTime zoned = ZonedDateTime.of(2024,6,1,12,0,0,0,ZoneOffset.UTC);

        IllegalArgumentException mixedDateAndInstant =
            assertThrows(
                IllegalArgumentException.class,
                () -> new TemporalRange<Temporal>(
                    LocalDate.of(2024,1,1),
                    Instant.parse("2024-01-02T00:00:00Z")));
        IllegalArgumentException mixedZonedAndOffset =
            assertThrows(
                IllegalArgumentException.class,
                () -> new TemporalRange<Temporal>(zoned,zoned.toOffsetDateTime()));

        assertEquals(
            "beginning and end must be of the same type",
            mixedDateAndInstant.getMessage());
        assertEquals(
            "beginning and end must be of the same type",
            mixedZonedAndOffset.getMessage());
    }

}

//////////////////////////////////////////////////////////////////////////////
