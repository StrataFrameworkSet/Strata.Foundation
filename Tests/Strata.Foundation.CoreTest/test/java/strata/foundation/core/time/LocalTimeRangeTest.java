//////////////////////////////////////////////////////////////////////////////
// LocalTimeRangeTest.java
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
import java.time.LocalTime;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("CommitStage")
public
class LocalTimeRangeTest
{
    private static final LocalTime BEGINNING = LocalTime.of(10,0);
    private static final LocalTime END = LocalTime.of(11,30);

    private LocalTimeRange
    roundTrip(LocalTimeRange range) throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bytes))
        {
            out.writeObject(range);
        }

        try (ObjectInputStream in =
                 new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())))
        {
            return (LocalTimeRange)in.readObject();
        }
    }

    public static Stream<Arguments>
    durationSources()
    {
        return
            Stream.of(
                Arguments.of(
                    LocalTime.of(10,0),
                    LocalTime.of(11,30),
                    Duration.ofMinutes(90)),
                Arguments.of(
                    LocalTime.of(0,0),
                    LocalTime.of(23,59),
                    Duration.ofHours(23).plusMinutes(59)),
                Arguments.of(
                    LocalTime.of(10,0),
                    LocalTime.of(10,0),
                    Duration.ZERO));
    }

    @Test
    public void
    testConstructorAndGetters()
    {
        LocalTimeRange range = new LocalTimeRange(BEGINNING,END);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(END,range.getEnd());
    }

    @Test
    public void
    testOfAndGetters()
    {
        LocalTimeRange range = LocalTimeRange.of(BEGINNING,END);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(END,range.getEnd());
    }

    @Test
    public void
    testEqualEndpointsAllowed()
    {
        LocalTimeRange range = LocalTimeRange.of(BEGINNING,BEGINNING);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(BEGINNING,range.getEnd());
        assertEquals(Duration.ZERO,range.getDuration());
    }

    @Test
    public void
    testReversedThrows()
    {
        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> LocalTimeRange.of(END,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testReversedConstructorThrows()
    {
        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> new LocalTimeRange(END,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testNullBeginningThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> LocalTimeRange.of(null,END));

        assertEquals("beginning is null",e.getMessage());
    }

    @Test
    public void
    testNullEndThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> new LocalTimeRange(BEGINNING,null));

        assertEquals("end is null",e.getMessage());
    }

    @ParameterizedTest
    @MethodSource("durationSources")
    public void
    testGetDuration(LocalTime beginning,LocalTime end,Duration expected)
    {
        assertEquals(expected,LocalTimeRange.of(beginning,end).getDuration());
    }

    @Test
    public void
    testGetPeriodUnsupported()
    {
        LocalTimeRange range = LocalTimeRange.of(BEGINNING,END);

        UnsupportedTemporalTypeException e =
            assertThrows(
                UnsupportedTemporalTypeException.class,
                () -> range.getPeriod());

        assertEquals("getPeriod is not supported for java.time.LocalTime",e.getMessage());
    }

    @Test
    public void
    testEqualsAndHashCode()
    {
        LocalTimeRange a = LocalTimeRange.of(BEGINNING,END);
        LocalTimeRange b = new LocalTimeRange(BEGINNING,END);
        LocalTimeRange c = LocalTimeRange.of(BEGINNING,END.plusMinutes(1));
        LocalTimeRange d = LocalTimeRange.of(BEGINNING.minusMinutes(1),END);

        assertEquals(a,b);
        assertEquals(a.hashCode(),b.hashCode());
        assertNotEquals(a,c);
        assertNotEquals(a,d);
    }

    @Test
    public void
    testNotEqualToTemporalRange()
    {
        LocalTimeRange range = LocalTimeRange.of(BEGINNING,END);
        TemporalRange<LocalTime> temporal = new TemporalRange<>(BEGINNING,END);

        assertNotEquals(range,temporal);
        assertNotEquals(temporal,range);
    }

    @Test
    public void
    testToString()
    {
        LocalTimeRange range = LocalTimeRange.of(BEGINNING,END);

        assertEquals("{beginning=10:00,end=11:30}",range.toString());
    }

    @Test
    public void
    testSerializationRoundTrip() throws IOException, ClassNotFoundException
    {
        LocalTimeRange expected = LocalTimeRange.of(BEGINNING,END);
        LocalTimeRange actual = roundTrip(expected);

        assertEquals(LocalTimeRange.class,actual.getClass());
        assertEquals(expected,actual);
        assertEquals(expected.hashCode(),actual.hashCode());
        assertEquals(BEGINNING,actual.getBeginning());
        assertEquals(END,actual.getEnd());
    }

}

//////////////////////////////////////////////////////////////////////////////
