//////////////////////////////////////////////////////////////////////////////
// InstantRangeTest.java
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
import java.time.Instant;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("CommitStage")
public
class InstantRangeTest
{
    private static final Instant BEGINNING = Instant.parse("2024-01-01T10:00:00Z");
    private static final Instant END = Instant.parse("2024-01-01T11:30:00Z");

    private InstantRange
    roundTrip(InstantRange range) throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bytes))
        {
            out.writeObject(range);
        }

        try (ObjectInputStream in =
                 new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())))
        {
            return (InstantRange)in.readObject();
        }
    }

    public static Stream<Arguments>
    durationSources()
    {
        return
            Stream.of(
                Arguments.of(
                    "2024-01-01T10:00:00Z",
                    "2024-01-01T11:30:00Z",
                    Duration.ofMinutes(90)),
                Arguments.of(
                    "2024-01-01T00:00:00.000000001Z",
                    "2024-01-01T00:00:00.999999999Z",
                    Duration.ofNanos(999999998L)),
                Arguments.of(
                    "2024-01-01T10:00:00Z",
                    "2024-01-01T10:00:00Z",
                    Duration.ZERO));
    }

    public static Stream<Arguments>
    periodSources()
    {
        return
            Stream.of(
                Arguments.of(
                    "2024-01-01T23:30:00Z",
                    "2024-01-02T00:30:00Z",
                    Period.ofDays(1)),
                Arguments.of(
                    "2024-12-31T23:30:00Z",
                    "2025-01-01T00:30:00Z",
                    Period.ofDays(1)),
                Arguments.of(
                    "2024-01-31T23:00:00Z",
                    "2024-03-01T01:00:00Z",
                    Period.of(0,1,1)),
                Arguments.of(
                    "2024-01-01T01:00:00Z",
                    "2024-01-01T23:00:00Z",
                    Period.ZERO));
    }

    @Test
    public void
    testConstructorAndGetters()
    {
        InstantRange range = new InstantRange(BEGINNING,END);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(END,range.getEnd());
    }

    @Test
    public void
    testOfAndGetters()
    {
        InstantRange range = InstantRange.of(BEGINNING,END);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(END,range.getEnd());
    }

    @Test
    public void
    testEqualEndpointsAllowed()
    {
        InstantRange range = InstantRange.of(BEGINNING,BEGINNING);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(BEGINNING,range.getEnd());
    }

    @Test
    public void
    testReversedThrows()
    {
        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> InstantRange.of(END,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testReversedConstructorThrows()
    {
        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> new InstantRange(END,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testNullBeginningThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> InstantRange.of(null,END));

        assertEquals("beginning is null",e.getMessage());
    }

    @Test
    public void
    testNullEndThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> new InstantRange(BEGINNING,null));

        assertEquals("end is null",e.getMessage());
    }

    @ParameterizedTest
    @MethodSource("durationSources")
    public void
    testGetDuration(String beginning,String end,Duration expected)
    {
        InstantRange range =
            InstantRange.of(Instant.parse(beginning),Instant.parse(end));

        assertEquals(expected,range.getDuration());
    }

    @ParameterizedTest
    @MethodSource("periodSources")
    public void
    testGetPeriodUsesUtcDates(String beginning,String end,Period expected)
    {
        InstantRange range =
            InstantRange.of(Instant.parse(beginning),Instant.parse(end));

        assertEquals(expected,range.getPeriod());
    }

    @Test
    public void
    testEqualsAndHashCode()
    {
        InstantRange a = InstantRange.of(BEGINNING,END);
        InstantRange b = new InstantRange(BEGINNING,END);
        InstantRange c = InstantRange.of(BEGINNING,END.plusSeconds(1));
        InstantRange d = InstantRange.of(BEGINNING.minusSeconds(1),END);

        assertEquals(a,b);
        assertEquals(a.hashCode(),b.hashCode());
        assertNotEquals(a,c);
        assertNotEquals(a,d);
    }

    @Test
    public void
    testHashSetDeduplicates()
    {
        Set<InstantRange> set = new HashSet<>();

        set.add(InstantRange.of(BEGINNING,END));
        set.add(new InstantRange(BEGINNING,END));
        set.add(InstantRange.of(BEGINNING,END.plusSeconds(1)));

        assertEquals(2,set.size());
    }

    @Test
    public void
    testSerializationRoundTrip() throws IOException, ClassNotFoundException
    {
        InstantRange expected = InstantRange.of(BEGINNING,END);
        InstantRange actual = roundTrip(expected);

        assertEquals(InstantRange.class,actual.getClass());
        assertEquals(expected,actual);
        assertEquals(expected.hashCode(),actual.hashCode());
        assertEquals(BEGINNING,actual.getBeginning());
        assertEquals(END,actual.getEnd());
    }

}

//////////////////////////////////////////////////////////////////////////////
