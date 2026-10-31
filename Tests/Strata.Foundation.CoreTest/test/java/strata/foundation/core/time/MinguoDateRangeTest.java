//////////////////////////////////////////////////////////////////////////////
// MinguoDateRangeTest.java
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
import java.time.chrono.MinguoDate;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("CommitStage")
public
class MinguoDateRangeTest
{
    private static final MinguoDate BEGINNING = MinguoDate.from(LocalDate.of(2024,1,15));
    private static final MinguoDate END = MinguoDate.from(LocalDate.of(2024,3,20));

    private MinguoDateRange
    roundTrip(MinguoDateRange range) throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bytes))
        {
            out.writeObject(range);
        }

        try (ObjectInputStream in =
                 new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())))
        {
            return (MinguoDateRange)in.readObject();
        }
    }

    public static Stream<Arguments>
    periodSources()
    {
        return
            Stream.of(
                Arguments.of(
                    MinguoDate.from(LocalDate.of(2024,1,15)),
                    MinguoDate.from(LocalDate.of(2024,3,20)),
                    Period.of(0,2,5)),
                Arguments.of(
                    MinguoDate.from(LocalDate.of(2024,2,28)),
                    MinguoDate.from(LocalDate.of(2024,3,1)),
                    Period.ofDays(2)),
                Arguments.of(
                    MinguoDate.from(LocalDate.of(2024,5,5)),
                    MinguoDate.from(LocalDate.of(2024,5,5)),
                    Period.ZERO));
    }

    public static Stream<Arguments>
    durationSources()
    {
        return
            Stream.of(
                Arguments.of(
                    MinguoDate.from(LocalDate.of(2024,1,15)),
                    MinguoDate.from(LocalDate.of(2024,3,20)),
                    Duration.ofDays(65)),
                Arguments.of(
                    MinguoDate.from(LocalDate.of(2024,2,28)),
                    MinguoDate.from(LocalDate.of(2024,3,1)),
                    Duration.ofDays(2)),
                Arguments.of(
                    MinguoDate.from(LocalDate.of(2024,5,5)),
                    MinguoDate.from(LocalDate.of(2024,5,5)),
                    Duration.ZERO));
    }

    @Test
    public void
    testConstructorAndGetters()
    {
        MinguoDateRange range = new MinguoDateRange(BEGINNING,END);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(END,range.getEnd());
    }

    @Test
    public void
    testOfAndGetters()
    {
        MinguoDateRange range = MinguoDateRange.of(BEGINNING,END);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(END,range.getEnd());
    }

    @Test
    public void
    testEqualEndpointsRange()
    {
        MinguoDateRange range = MinguoDateRange.of(BEGINNING,BEGINNING);

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
                () -> MinguoDateRange.of(END,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testReversedConstructorThrows()
    {
        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> new MinguoDateRange(END,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testNullBeginningThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> MinguoDateRange.of(null,END));

        assertEquals("beginning is null",e.getMessage());
    }

    @Test
    public void
    testNullBeginningConstructorThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> new MinguoDateRange(null,END));

        assertEquals("beginning is null",e.getMessage());
    }

    @Test
    public void
    testNullEndThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> new MinguoDateRange(BEGINNING,null));

        assertEquals("end is null",e.getMessage());
    }

    @Test
    public void
    testNullEndOfThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> MinguoDateRange.of(BEGINNING,null));

        assertEquals("end is null",e.getMessage());
    }

    @ParameterizedTest
    @MethodSource("periodSources")
    public void
    testGetPeriod(MinguoDate beginning,MinguoDate end,Period expected)
    {
        assertEquals(expected,MinguoDateRange.of(beginning,end).getPeriod());
    }

    @ParameterizedTest
    @MethodSource("durationSources")
    public void
    testGetDuration(MinguoDate beginning,MinguoDate end,Duration expected)
    {
        assertEquals(expected,MinguoDateRange.of(beginning,end).getDuration());
    }

    @Test
    public void
    testEqualsAndHashCode()
    {
        MinguoDateRange a = MinguoDateRange.of(BEGINNING,END);
        MinguoDateRange b = new MinguoDateRange(BEGINNING,END);
        MinguoDateRange c = MinguoDateRange.of(BEGINNING,END.plus(1,ChronoUnit.DAYS));
        MinguoDateRange d = MinguoDateRange.of(BEGINNING.minus(1,ChronoUnit.DAYS),END);

        assertEquals(a,b);
        assertEquals(a.hashCode(),b.hashCode());
        assertNotEquals(a,c);
        assertNotEquals(a,d);
    }

    @Test
    public void
    testNotEqualToTemporalRange()
    {
        MinguoDateRange range = MinguoDateRange.of(BEGINNING,END);
        TemporalRange<MinguoDate> other = new TemporalRange<>(BEGINNING,END);

        assertNotEquals(range,other);
        assertNotEquals(other,range);
    }

    @Test
    public void
    testToString()
    {
        assertEquals(String.format("{beginning=%s,end=%s}",BEGINNING,END),MinguoDateRange.of(BEGINNING,END).toString());
    }

    @Test
    public void
    testSerializationRoundTrip() throws IOException, ClassNotFoundException
    {
        MinguoDateRange expected = MinguoDateRange.of(BEGINNING,END);
        MinguoDateRange actual = roundTrip(expected);

        assertEquals(MinguoDateRange.class,actual.getClass());
        assertEquals(expected,actual);
        assertEquals(expected.hashCode(),actual.hashCode());
        assertEquals(BEGINNING,actual.getBeginning());
        assertEquals(END,actual.getEnd());
    }

}

//////////////////////////////////////////////////////////////////////////////
