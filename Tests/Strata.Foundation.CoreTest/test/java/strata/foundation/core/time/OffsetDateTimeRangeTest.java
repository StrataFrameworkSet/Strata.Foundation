//////////////////////////////////////////////////////////////////////////////
// OffsetDateTimeRangeTest.java
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
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("CommitStage")
public
class OffsetDateTimeRangeTest
{
    private static final ZoneOffset PLUS_TWO = ZoneOffset.ofHours(2);
    private static final ZoneOffset MINUS_FIVE = ZoneOffset.ofHours(-5);
    private static final OffsetDateTime BEGINNING =
        OffsetDateTime.of(2024,1,1,10,0,0,0,PLUS_TWO);
    private static final OffsetDateTime END =
        OffsetDateTime.of(2024,1,1,11,30,0,0,PLUS_TWO);

    private OffsetDateTimeRange
    roundTrip(OffsetDateTimeRange range) throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bytes))
        {
            out.writeObject(range);
        }

        try (ObjectInputStream in =
                 new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())))
        {
            return (OffsetDateTimeRange)in.readObject();
        }
    }

    public static Stream<Arguments>
    periodSources()
    {
        return
            Stream.of(
                Arguments.of(
                    OffsetDateTime.of(2024,1,31,23,0,0,0,MINUS_FIVE),
                    OffsetDateTime.of(2024,3,1,1,0,0,0,MINUS_FIVE),
                    Period.of(0,1,1)),
                Arguments.of(
                    OffsetDateTime.of(2024,1,1,1,0,0,0,MINUS_FIVE),
                    OffsetDateTime.of(2024,1,1,23,0,0,0,MINUS_FIVE),
                    Period.ZERO));
    }

    public static Stream<Arguments>
    durationSources()
    {
        return
            Stream.of(
                Arguments.of(
                    OffsetDateTime.of(2024,1,1,10,0,0,0,PLUS_TWO),
                    OffsetDateTime.of(2024,1,1,9,0,0,0,ZoneOffset.UTC),
                    Duration.ofHours(1)),
                Arguments.of(
                    OffsetDateTime.of(2024,1,1,10,0,0,0,PLUS_TWO),
                    OffsetDateTime.of(2024,1,1,11,30,0,0,PLUS_TWO),
                    Duration.ofMinutes(90)));
    }

    @Test
    public void
    testConstructorAndGetters()
    {
        OffsetDateTimeRange range = new OffsetDateTimeRange(BEGINNING,END);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(END,range.getEnd());
    }

    @Test
    public void
    testOfAndGetters()
    {
        OffsetDateTimeRange range = OffsetDateTimeRange.of(BEGINNING,END);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(END,range.getEnd());
    }

    @Test
    public void
    testEqualEndpointsAllowed()
    {
        OffsetDateTimeRange range = OffsetDateTimeRange.of(BEGINNING,BEGINNING);

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
                () -> OffsetDateTimeRange.of(END,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testReversedConstructorThrows()
    {
        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> new OffsetDateTimeRange(END,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testReversedAcrossOffsetsThrows()
    {
        OffsetDateTime utc = OffsetDateTime.of(2024,1,1,9,0,0,0,ZoneOffset.UTC);

        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> OffsetDateTimeRange.of(utc,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testNullBeginningThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> OffsetDateTimeRange.of(null,END));

        assertEquals("beginning is null",e.getMessage());
    }

    @Test
    public void
    testNullEndThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> new OffsetDateTimeRange(BEGINNING,null));

        assertEquals("end is null",e.getMessage());
    }

    @ParameterizedTest
    @MethodSource("periodSources")
    public void
    testGetPeriod(OffsetDateTime beginning,OffsetDateTime end,Period expected)
    {
        assertEquals(expected,OffsetDateTimeRange.of(beginning,end).getPeriod());
    }

    @ParameterizedTest
    @MethodSource("durationSources")
    public void
    testGetDuration(OffsetDateTime beginning,OffsetDateTime end,Duration expected)
    {
        assertEquals(expected,OffsetDateTimeRange.of(beginning,end).getDuration());
    }

    @Test
    public void
    testEqualsAndHashCode()
    {
        OffsetDateTimeRange a = OffsetDateTimeRange.of(BEGINNING,END);
        OffsetDateTimeRange b = new OffsetDateTimeRange(BEGINNING,END);
        OffsetDateTimeRange c = OffsetDateTimeRange.of(BEGINNING,END.plusMinutes(1));
        OffsetDateTimeRange d = OffsetDateTimeRange.of(BEGINNING.minusMinutes(1),END);

        assertEquals(a,b);
        assertEquals(a.hashCode(),b.hashCode());
        assertNotEquals(a,c);
        assertNotEquals(a,d);
    }

    @Test
    public void
    testNotEqualToTemporalRange()
    {
        OffsetDateTimeRange range = OffsetDateTimeRange.of(BEGINNING,END);
        TemporalRange<OffsetDateTime> temporal = new TemporalRange<>(BEGINNING,END);

        assertNotEquals(range,temporal);
        assertNotEquals(temporal,range);
    }

    @Test
    public void
    testToString()
    {
        OffsetDateTimeRange range = OffsetDateTimeRange.of(BEGINNING,END);

        assertEquals(
            "{beginning=2024-01-01T10:00+02:00,end=2024-01-01T11:30+02:00}",
            range.toString());
    }

    @Test
    public void
    testSerializationRoundTrip() throws IOException, ClassNotFoundException
    {
        OffsetDateTimeRange expected = OffsetDateTimeRange.of(BEGINNING,END);
        OffsetDateTimeRange actual = roundTrip(expected);

        assertEquals(OffsetDateTimeRange.class,actual.getClass());
        assertEquals(expected,actual);
        assertEquals(expected.hashCode(),actual.hashCode());
        assertEquals(BEGINNING,actual.getBeginning());
        assertEquals(END,actual.getEnd());
    }

}

//////////////////////////////////////////////////////////////////////////////
