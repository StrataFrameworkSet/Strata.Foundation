//////////////////////////////////////////////////////////////////////////////
// OffsetTimeRangeTest.java
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
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("CommitStage")
public
class OffsetTimeRangeTest
{
    private static final ZoneOffset PLUS_TWO = ZoneOffset.ofHours(2);
    private static final OffsetTime BEGINNING = OffsetTime.of(10,0,0,0,PLUS_TWO);
    private static final OffsetTime END = OffsetTime.of(11,30,0,0,PLUS_TWO);

    private OffsetTimeRange
    roundTrip(OffsetTimeRange range) throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bytes))
        {
            out.writeObject(range);
        }

        try (ObjectInputStream in =
                 new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())))
        {
            return (OffsetTimeRange)in.readObject();
        }
    }

    public static Stream<Arguments>
    durationSources()
    {
        return
            Stream.of(
                Arguments.of(
                    OffsetTime.of(10,0,0,0,PLUS_TWO),
                    OffsetTime.of(9,0,0,0,ZoneOffset.UTC),
                    Duration.ofHours(1)),
                Arguments.of(
                    OffsetTime.of(10,0,0,0,PLUS_TWO),
                    OffsetTime.of(11,30,0,0,PLUS_TWO),
                    Duration.ofMinutes(90)),
                Arguments.of(
                    OffsetTime.of(10,0,0,0,PLUS_TWO),
                    OffsetTime.of(10,0,0,0,PLUS_TWO),
                    Duration.ZERO));
    }

    @Test
    public void
    testConstructorAndGetters()
    {
        OffsetTimeRange range = new OffsetTimeRange(BEGINNING,END);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(END,range.getEnd());
    }

    @Test
    public void
    testOfAndGetters()
    {
        OffsetTimeRange range = OffsetTimeRange.of(BEGINNING,END);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(END,range.getEnd());
    }

    @Test
    public void
    testEqualEndpointsAllowed()
    {
        OffsetTimeRange range = OffsetTimeRange.of(BEGINNING,BEGINNING);

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
                () -> OffsetTimeRange.of(END,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testReversedConstructorThrows()
    {
        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> new OffsetTimeRange(END,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testReversedAcrossOffsetsThrows()
    {
        OffsetTime utc = OffsetTime.of(9,0,0,0,ZoneOffset.UTC);

        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> OffsetTimeRange.of(utc,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testNullBeginningThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> OffsetTimeRange.of(null,END));

        assertEquals("beginning is null",e.getMessage());
    }

    @Test
    public void
    testNullEndThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> new OffsetTimeRange(BEGINNING,null));

        assertEquals("end is null",e.getMessage());
    }

    @ParameterizedTest
    @MethodSource("durationSources")
    public void
    testGetDuration(OffsetTime beginning,OffsetTime end,Duration expected)
    {
        assertEquals(expected,OffsetTimeRange.of(beginning,end).getDuration());
    }

    @Test
    public void
    testGetPeriodUnsupported()
    {
        OffsetTimeRange range = OffsetTimeRange.of(BEGINNING,END);

        UnsupportedTemporalTypeException e =
            assertThrows(
                UnsupportedTemporalTypeException.class,
                () -> range.getPeriod());

        assertEquals("getPeriod is not supported for java.time.OffsetTime",e.getMessage());
    }

    @Test
    public void
    testEqualsAndHashCode()
    {
        OffsetTimeRange a = OffsetTimeRange.of(BEGINNING,END);
        OffsetTimeRange b = new OffsetTimeRange(BEGINNING,END);
        OffsetTimeRange c = OffsetTimeRange.of(BEGINNING,END.plusMinutes(1));
        OffsetTimeRange d = OffsetTimeRange.of(BEGINNING.minusMinutes(1),END);

        assertEquals(a,b);
        assertEquals(a.hashCode(),b.hashCode());
        assertNotEquals(a,c);
        assertNotEquals(a,d);
    }

    @Test
    public void
    testNotEqualToTemporalRange()
    {
        OffsetTimeRange range = OffsetTimeRange.of(BEGINNING,END);
        TemporalRange<OffsetTime> temporal = new TemporalRange<>(BEGINNING,END);

        assertNotEquals(range,temporal);
        assertNotEquals(temporal,range);
    }

    @Test
    public void
    testToString()
    {
        OffsetTimeRange range = OffsetTimeRange.of(BEGINNING,END);

        assertEquals("{beginning=10:00+02:00,end=11:30+02:00}",range.toString());
    }

    @Test
    public void
    testSerializationRoundTrip() throws IOException, ClassNotFoundException
    {
        OffsetTimeRange expected = OffsetTimeRange.of(BEGINNING,END);
        OffsetTimeRange actual = roundTrip(expected);

        assertEquals(OffsetTimeRange.class,actual.getClass());
        assertEquals(expected,actual);
        assertEquals(expected.hashCode(),actual.hashCode());
        assertEquals(BEGINNING,actual.getBeginning());
        assertEquals(END,actual.getEnd());
    }

}

//////////////////////////////////////////////////////////////////////////////
