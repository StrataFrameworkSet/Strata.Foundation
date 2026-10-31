//////////////////////////////////////////////////////////////////////////////
// LocalDateTimeRangeTest.java
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
import java.time.LocalDateTime;
import java.time.Period;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("CommitStage")
public
class LocalDateTimeRangeTest
{
    private static final LocalDateTime BEGINNING = LocalDateTime.of(2024,1,1,10,0);
    private static final LocalDateTime END = LocalDateTime.of(2024,1,1,11,30);

    private LocalDateTimeRange
    roundTrip(LocalDateTimeRange range) throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bytes))
        {
            out.writeObject(range);
        }

        try (ObjectInputStream in =
                 new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())))
        {
            return (LocalDateTimeRange)in.readObject();
        }
    }

    public static Stream<Arguments>
    periodSources()
    {
        return
            Stream.of(
                Arguments.of(
                    LocalDateTime.of(2024,1,15,10,0),
                    LocalDateTime.of(2024,3,20,9,0),
                    Period.of(0,2,5)),
                Arguments.of(
                    LocalDateTime.of(2024,1,1,1,0),
                    LocalDateTime.of(2024,1,1,23,0),
                    Period.ZERO));
    }

    public static Stream<Arguments>
    durationSources()
    {
        return
            Stream.of(
                Arguments.of(
                    LocalDateTime.of(2024,1,1,0,0),
                    LocalDateTime.of(2024,1,3,3,0),
                    Duration.ofDays(2).plusHours(3)),
                Arguments.of(
                    LocalDateTime.of(2024,1,1,10,0),
                    LocalDateTime.of(2024,1,1,11,30),
                    Duration.ofMinutes(90)));
    }

    @Test
    public void
    testConstructorAndGetters()
    {
        LocalDateTimeRange range = new LocalDateTimeRange(BEGINNING,END);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(END,range.getEnd());
    }

    @Test
    public void
    testOfAndGetters()
    {
        LocalDateTimeRange range = LocalDateTimeRange.of(BEGINNING,END);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(END,range.getEnd());
    }

    @Test
    public void
    testEqualEndpointsAllowed()
    {
        LocalDateTimeRange range = LocalDateTimeRange.of(BEGINNING,BEGINNING);

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
                () -> LocalDateTimeRange.of(END,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testReversedConstructorThrows()
    {
        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> new LocalDateTimeRange(END,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testNullBeginningThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> LocalDateTimeRange.of(null,END));

        assertEquals("beginning is null",e.getMessage());
    }

    @Test
    public void
    testNullEndThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> new LocalDateTimeRange(BEGINNING,null));

        assertEquals("end is null",e.getMessage());
    }

    @ParameterizedTest
    @MethodSource("periodSources")
    public void
    testGetPeriod(LocalDateTime beginning,LocalDateTime end,Period expected)
    {
        assertEquals(expected,LocalDateTimeRange.of(beginning,end).getPeriod());
    }

    @ParameterizedTest
    @MethodSource("durationSources")
    public void
    testGetDuration(LocalDateTime beginning,LocalDateTime end,Duration expected)
    {
        assertEquals(expected,LocalDateTimeRange.of(beginning,end).getDuration());
    }

    @Test
    public void
    testEqualsAndHashCode()
    {
        LocalDateTimeRange a = LocalDateTimeRange.of(BEGINNING,END);
        LocalDateTimeRange b = new LocalDateTimeRange(BEGINNING,END);
        LocalDateTimeRange c = LocalDateTimeRange.of(BEGINNING,END.plusMinutes(1));
        LocalDateTimeRange d = LocalDateTimeRange.of(BEGINNING.minusMinutes(1),END);

        assertEquals(a,b);
        assertEquals(a.hashCode(),b.hashCode());
        assertNotEquals(a,c);
        assertNotEquals(a,d);
    }

    @Test
    public void
    testNotEqualToTemporalRange()
    {
        LocalDateTimeRange range = LocalDateTimeRange.of(BEGINNING,END);
        TemporalRange<LocalDateTime> temporal = new TemporalRange<>(BEGINNING,END);

        assertNotEquals(range,temporal);
        assertNotEquals(temporal,range);
    }

    @Test
    public void
    testToString()
    {
        LocalDateTimeRange range = LocalDateTimeRange.of(BEGINNING,END);

        assertEquals(
            "{beginning=2024-01-01T10:00,end=2024-01-01T11:30}",
            range.toString());
    }

    @Test
    public void
    testSerializationRoundTrip() throws IOException, ClassNotFoundException
    {
        LocalDateTimeRange expected = LocalDateTimeRange.of(BEGINNING,END);
        LocalDateTimeRange actual = roundTrip(expected);

        assertEquals(LocalDateTimeRange.class,actual.getClass());
        assertEquals(expected,actual);
        assertEquals(expected.hashCode(),actual.hashCode());
        assertEquals(BEGINNING,actual.getBeginning());
        assertEquals(END,actual.getEnd());
    }

}

//////////////////////////////////////////////////////////////////////////////
