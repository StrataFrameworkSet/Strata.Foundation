//////////////////////////////////////////////////////////////////////////////
// TemporalComparatorTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.HijrahDate;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.function.IntSupplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("CommitStage")
public
class TemporalComparatorTest
{
    private static <T extends Temporal> Arguments
    comparison(T a,T b,int expected)
    {
        IntSupplier result = () -> new TemporalComparator<T>().compare(a,b);

        return Arguments.of(result,expected);
    }

    public static Stream<Arguments>
    sources()
    {
        Instant        i1 = Instant.parse("2024-01-01T00:00:00Z");
        Instant        i2 = Instant.parse("2024-01-01T00:00:01Z");
        LocalDate      d1 = LocalDate.of(2024,1,1);
        LocalDate      d2 = LocalDate.of(2024,1,2);
        LocalDateTime  dt1 = LocalDateTime.of(2024,1,1,10,0,0);
        LocalDateTime  dt2 = LocalDateTime.of(2024,1,1,10,0,1);
        LocalTime      t1 = LocalTime.of(9,0);
        LocalTime      t2 = LocalTime.of(9,30);
        ZoneId         zone = ZoneId.of("America/New_York");
        ZonedDateTime  z1 = ZonedDateTime.of(dt1,zone);
        ZonedDateTime  z2 = ZonedDateTime.of(dt2,zone);
        OffsetDateTime o1 = OffsetDateTime.of(dt1,ZoneOffset.UTC);
        OffsetDateTime o2 = OffsetDateTime.of(dt2,ZoneOffset.UTC);
        OffsetTime     ot1 = OffsetTime.of(9,0,0,0,ZoneOffset.UTC);
        OffsetTime     ot2 = OffsetTime.of(9,30,0,0,ZoneOffset.UTC);
        Year           y1 = Year.of(2024);
        Year           y2 = Year.of(2025);
        YearMonth      ym1 = YearMonth.of(2024,12);
        YearMonth      ym2 = YearMonth.of(2025,1);

        return
            Stream.of(
                comparison(i1,i2,-1),
                comparison(i1,i1,0),
                comparison(i2,i1,1),
                comparison(d1,d2,-1),
                comparison(d1,d1,0),
                comparison(d2,d1,1),
                comparison(dt1,dt2,-1),
                comparison(dt1,dt1,0),
                comparison(dt2,dt1,1),
                comparison(t1,t2,-1),
                comparison(t1,t1,0),
                comparison(t2,t1,1),
                comparison(z1,z2,-1),
                comparison(z1,z1,0),
                comparison(z2,z1,1),
                comparison(o1,o2,-1),
                comparison(o1,o1,0),
                comparison(o2,o1,1),
                comparison(ot1,ot2,-1),
                comparison(ot1,ot1,0),
                comparison(ot2,ot1,1),
                comparison(y1,y2,-1),
                comparison(y1,y1,0),
                comparison(y2,y1,1),
                comparison(ym1,ym2,-1),
                comparison(ym1,ym1,0),
                comparison(ym2,ym1,1));
    }

    public static Stream<Arguments>
    boundarySources()
    {
        return
            Stream.of(
                comparison(
                    ZonedDateTime.of(2024,1,1,10,0,0,0,ZoneId.of("America/New_York")),
                    ZonedDateTime.of(2024,1,1,12,0,0,0,ZoneOffset.UTC),
                    1),
                comparison(
                    OffsetDateTime.of(2024,1,1,10,0,0,0,ZoneOffset.ofHours(2)),
                    OffsetDateTime.of(2024,1,1,9,0,0,0,ZoneOffset.UTC),
                    -1),
                comparison(
                    OffsetTime.of(10,0,0,0,ZoneOffset.ofHours(2)),
                    OffsetTime.of(9,0,0,0,ZoneOffset.UTC),
                    -1),
                comparison(
                    (Temporal)ZonedDateTime.of(2024,1,1,10,0,0,0,ZoneId.of("America/New_York")),
                    (Temporal)OffsetDateTime.of(2024,1,1,14,0,0,0,ZoneOffset.UTC),
                    1));
    }

    public static Stream<Arguments>
    sameInstantSources()
    {
        ZonedDateTime utc =
            ZonedDateTime.of(2024,6,1,12,0,0,0,ZoneId.of("UTC"));
        ZonedDateTime newYork =
            utc.withZoneSameInstant(ZoneId.of("America/New_York"));
        ZonedDateTime tokyo =
            utc.withZoneSameInstant(ZoneId.of("Asia/Tokyo"));

        return
            Stream.of(
                Arguments.of(utc,newYork),
                Arguments.of(newYork,utc),
                Arguments.of(newYork,tokyo),
                Arguments.of(tokyo,utc));
    }

    @ParameterizedTest
    @MethodSource("sources")
    public void
    testCompareSign(IntSupplier result,int expected)
    {
        assertEquals(expected,Integer.signum(result.getAsInt()));
    }

    @ParameterizedTest
    @MethodSource("boundarySources")
    public void
    testCompareAcrossZonesAndOffsets(IntSupplier result,int expected)
    {
        assertEquals(expected,Integer.signum(result.getAsInt()));
    }

    @ParameterizedTest
    @MethodSource("sameInstantSources")
    public void
    testSameInstantInDifferentZonesIsEqual(ZonedDateTime a,ZonedDateTime b)
    {
        assertEquals(0,new TemporalComparator<ZonedDateTime>().compare(a,b));
    }

    @Test
    public void
    testDifferentTypesWithoutInstantThrow()
    {
        TemporalComparator<Temporal> comparator = new TemporalComparator<>();
        LocalDate                    date = LocalDate.of(2024,1,1);

        assertThrows(
            IllegalArgumentException.class,
            () -> comparator.compare(date,Instant.parse("2024-01-01T00:00:00Z")));
        assertThrows(
            IllegalArgumentException.class,
            () -> comparator.compare(Instant.parse("2024-01-01T00:00:00Z"),date));
        assertThrows(
            IllegalArgumentException.class,
            () -> comparator.compare(date,LocalTime.of(10,0)));
    }

    @Test
    public void
    testDifferentComparableTypesThrow()
    {
        TemporalComparator<Temporal> comparator = new TemporalComparator<>();
        LocalDate                    date = LocalDate.of(2024,1,1);

        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> comparator.compare(date,HijrahDate.from(date)));

        assertEquals(
            "cannot compare java.time.LocalDate with java.time.chrono.HijrahDate",
            e.getMessage());
    }

    @Test
    public void
    testNonComparableTemporalThrows()
    {
        TemporalComparator<Temporal> comparator = new TemporalComparator<>();

        assertThrows(
            IllegalArgumentException.class,
            () -> comparator.compare(incomparable(),incomparable()));
    }

    private Temporal
    incomparable()
    {
        return
            new Temporal()
            {
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
            };
    }

}

//////////////////////////////////////////////////////////////////////////////
