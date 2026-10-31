//////////////////////////////////////////////////////////////////////////////
// HijrahDateRangeTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.HijrahDate;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("CommitStage")
public
class HijrahDateRangeTest
{
    private static final HijrahDate BEGINNING = HijrahDate.from(LocalDate.of(2024,1,15));
    private static final HijrahDate END = HijrahDate.from(LocalDate.of(2024,3,20));

    private HijrahDateRange
    roundTrip(HijrahDateRange range) throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bytes))
        {
            out.writeObject(range);
        }

        try (ObjectInputStream in =
                 new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())))
        {
            return (HijrahDateRange)in.readObject();
        }
    }

    public static Stream<Arguments>
    periodSources()
    {
        return
            Stream.of(
                Arguments.of(
                    HijrahDate.from(LocalDate.of(2024,1,15)),
                    HijrahDate.from(LocalDate.of(2024,3,20)),
                    Period.of(0,2,5)),
                Arguments.of(
                    HijrahDate.from(LocalDate.of(2024,2,28)),
                    HijrahDate.from(LocalDate.of(2024,3,1)),
                    Period.ofDays(2)),
                Arguments.of(
                    HijrahDate.from(LocalDate.of(2024,5,5)),
                    HijrahDate.from(LocalDate.of(2024,5,5)),
                    Period.ZERO));
    }

    public static Stream<Arguments>
    durationSources()
    {
        return
            Stream.of(
                Arguments.of(
                    HijrahDate.from(LocalDate.of(2024,1,15)),
                    HijrahDate.from(LocalDate.of(2024,3,20)),
                    Duration.ofDays(65)),
                Arguments.of(
                    HijrahDate.from(LocalDate.of(2024,2,28)),
                    HijrahDate.from(LocalDate.of(2024,3,1)),
                    Duration.ofDays(2)),
                Arguments.of(
                    HijrahDate.from(LocalDate.of(2024,5,5)),
                    HijrahDate.from(LocalDate.of(2024,5,5)),
                    Duration.ZERO));
    }

    @Test
    public void
    testConstructorAndGetters()
    {
        HijrahDateRange range = new HijrahDateRange(BEGINNING,END);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(END,range.getEnd());
    }

    @Test
    public void
    testOfAndGetters()
    {
        HijrahDateRange range = HijrahDateRange.of(BEGINNING,END);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(END,range.getEnd());
    }

    @Test
    public void
    testEqualEndpointsRange()
    {
        HijrahDateRange range = HijrahDateRange.of(BEGINNING,BEGINNING);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(BEGINNING,range.getEnd());
        assertEquals(Period.ZERO,range.getPeriod());
        assertEquals(Duration.ZERO,range.getDuration());
    }

    @Test
    public void
    testReversedThrows()
    {
        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> HijrahDateRange.of(END,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testReversedConstructorThrows()
    {
        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> new HijrahDateRange(END,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testNullBeginningThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> HijrahDateRange.of(null,END));

        assertEquals("beginning is null",e.getMessage());
    }

    @Test
    public void
    testNullBeginningConstructorThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> new HijrahDateRange(null,END));

        assertEquals("beginning is null",e.getMessage());
    }

    @Test
    public void
    testNullEndThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> new HijrahDateRange(BEGINNING,null));

        assertEquals("end is null",e.getMessage());
    }

    @Test
    public void
    testNullEndOfThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> HijrahDateRange.of(BEGINNING,null));

        assertEquals("end is null",e.getMessage());
    }

    @ParameterizedTest
    @MethodSource("periodSources")
    public void
    testGetPeriod(HijrahDate beginning,HijrahDate end,Period expected)
    {
        assertEquals(expected,HijrahDateRange.of(beginning,end).getPeriod());
    }

    @ParameterizedTest
    @MethodSource("durationSources")
    public void
    testGetDuration(HijrahDate beginning,HijrahDate end,Duration expected)
    {
        assertEquals(expected,HijrahDateRange.of(beginning,end).getDuration());
    }

    @Test
    public void
    testEqualsAndHashCode()
    {
        HijrahDateRange a = HijrahDateRange.of(BEGINNING,END);
        HijrahDateRange b = new HijrahDateRange(BEGINNING,END);
        HijrahDateRange c = HijrahDateRange.of(BEGINNING,END.plus(1,ChronoUnit.DAYS));
        HijrahDateRange d = HijrahDateRange.of(BEGINNING.minus(1,ChronoUnit.DAYS),END);

        assertEquals(a,b);
        assertEquals(a.hashCode(),b.hashCode());
        assertNotEquals(a,c);
        assertNotEquals(a,d);
    }

    @Test
    public void
    testNotEqualToTemporalRange()
    {
        HijrahDateRange range = HijrahDateRange.of(BEGINNING,END);
        TemporalRange<HijrahDate> other = new TemporalRange<>(BEGINNING,END);

        assertNotEquals(range,other);
        assertNotEquals(other,range);
    }

    @Test
    public void
    testToString()
    {
        assertEquals(String.format("{beginning=%s,end=%s}",BEGINNING,END),HijrahDateRange.of(BEGINNING,END).toString());
    }

    @Test
    public void
    testSerializationRoundTrip() throws IOException, ClassNotFoundException
    {
        HijrahDateRange expected = HijrahDateRange.of(BEGINNING,END);
        HijrahDateRange actual = roundTrip(expected);

        assertEquals(HijrahDateRange.class,actual.getClass());
        assertEquals(expected,actual);
        assertEquals(expected.hashCode(),actual.hashCode());
        assertEquals(BEGINNING,actual.getBeginning());
        assertEquals(END,actual.getEnd());
    }

}

//////////////////////////////////////////////////////////////////////////////
