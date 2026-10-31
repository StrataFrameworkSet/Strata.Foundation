//////////////////////////////////////////////////////////////////////////////
// ZonedDateTimeRangeTest.java
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
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("CommitStage")
public
class ZonedDateTimeRangeTest
{
    private static final ZoneId NEW_YORK = ZoneId.of("America/New_York");
    private static final ZonedDateTime BEGINNING =
        ZonedDateTime.of(2024,1,1,10,0,0,0,NEW_YORK);
    private static final ZonedDateTime END =
        ZonedDateTime.of(2024,1,1,11,30,0,0,NEW_YORK);

    private ZonedDateTimeRange
    roundTrip(ZonedDateTimeRange range) throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bytes))
        {
            out.writeObject(range);
        }

        try (ObjectInputStream in =
                 new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())))
        {
            return (ZonedDateTimeRange)in.readObject();
        }
    }

    public static Stream<Arguments>
    periodSources()
    {
        return
            Stream.of(
                Arguments.of(
                    ZonedDateTime.of(2024,1,31,23,0,0,0,NEW_YORK),
                    ZonedDateTime.of(2024,3,1,1,0,0,0,NEW_YORK),
                    Period.of(0,1,1)),
                Arguments.of(
                    ZonedDateTime.of(2024,1,1,1,0,0,0,NEW_YORK),
                    ZonedDateTime.of(2024,1,1,23,0,0,0,NEW_YORK),
                    Period.ZERO));
    }

    public static Stream<Arguments>
    durationSources()
    {
        return
            Stream.of(
                Arguments.of(
                    ZonedDateTime.of(2024,3,10,0,0,0,0,NEW_YORK),
                    ZonedDateTime.of(2024,3,10,4,0,0,0,NEW_YORK),
                    Duration.ofHours(3)),
                Arguments.of(
                    ZonedDateTime.of(2024,1,1,10,0,0,0,NEW_YORK),
                    ZonedDateTime.of(2024,1,1,11,30,0,0,NEW_YORK),
                    Duration.ofMinutes(90)));
    }

    @Test
    public void
    testConstructorAndGetters()
    {
        ZonedDateTimeRange range = new ZonedDateTimeRange(BEGINNING,END);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(END,range.getEnd());
    }

    @Test
    public void
    testOfAndGetters()
    {
        ZonedDateTimeRange range = ZonedDateTimeRange.of(BEGINNING,END);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(END,range.getEnd());
    }

    @Test
    public void
    testEqualEndpointsAllowed()
    {
        ZonedDateTimeRange range = ZonedDateTimeRange.of(BEGINNING,BEGINNING);

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
                () -> ZonedDateTimeRange.of(END,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testReversedConstructorThrows()
    {
        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> new ZonedDateTimeRange(END,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testNullBeginningThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> ZonedDateTimeRange.of(null,END));

        assertEquals("beginning is null",e.getMessage());
    }

    @Test
    public void
    testNullEndThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> new ZonedDateTimeRange(BEGINNING,null));

        assertEquals("end is null",e.getMessage());
    }

    @ParameterizedTest
    @MethodSource("periodSources")
    public void
    testGetPeriod(ZonedDateTime beginning,ZonedDateTime end,Period expected)
    {
        assertEquals(expected,ZonedDateTimeRange.of(beginning,end).getPeriod());
    }

    @ParameterizedTest
    @MethodSource("durationSources")
    public void
    testGetDuration(ZonedDateTime beginning,ZonedDateTime end,Duration expected)
    {
        assertEquals(expected,ZonedDateTimeRange.of(beginning,end).getDuration());
    }

    @Test
    public void
    testOrderingUsesInstantAcrossZones()
    {
        ZonedDateTime newYork = ZonedDateTime.of(2024,1,1,10,0,0,0,NEW_YORK);
        ZonedDateTime utc = ZonedDateTime.of(2024,1,1,12,0,0,0,ZoneOffset.UTC);

        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> ZonedDateTimeRange.of(newYork,utc));

        assertEquals("beginning must be before end",e.getMessage());

        ZonedDateTimeRange range = ZonedDateTimeRange.of(utc,newYork);

        assertEquals(Duration.ofHours(3),range.getDuration());
    }

    @Test
    public void
    testSameInstantInDifferentZones()
    {
        ZonedDateTime utc = ZonedDateTime.of(2024,6,1,12,0,0,0,ZoneOffset.UTC);
        ZonedDateTime tokyo = utc.withZoneSameInstant(ZoneId.of("Asia/Tokyo"));

        ZonedDateTimeRange range = ZonedDateTimeRange.of(tokyo,utc);

        assertEquals(tokyo,range.getBeginning());
        assertEquals(utc,range.getEnd());
        assertEquals(Duration.ZERO,range.getDuration());
    }

    @Test
    public void
    testSameInstantsInDifferentZonesAreNotEqual()
    {
        ZonedDateTime utc = ZonedDateTime.of(2024,6,1,12,0,0,0,ZoneOffset.UTC);
        ZonedDateTime tokyo = utc.withZoneSameInstant(ZoneId.of("Asia/Tokyo"));

        ZonedDateTimeRange utcRange = ZonedDateTimeRange.of(utc,utc.plusHours(3));
        ZonedDateTimeRange tokyoRange = ZonedDateTimeRange.of(tokyo,tokyo.plusHours(3));

        assertNotEquals(utcRange,tokyoRange);
        assertEquals(utcRange.getDuration(),tokyoRange.getDuration());
    }

    @Test
    public void
    testEqualsAndHashCode()
    {
        ZonedDateTimeRange a = ZonedDateTimeRange.of(BEGINNING,END);
        ZonedDateTimeRange b = new ZonedDateTimeRange(BEGINNING,END);
        ZonedDateTimeRange c = ZonedDateTimeRange.of(BEGINNING,END.plusMinutes(1));
        ZonedDateTimeRange d = ZonedDateTimeRange.of(BEGINNING.minusMinutes(1),END);

        assertEquals(a,b);
        assertEquals(a.hashCode(),b.hashCode());
        assertNotEquals(a,c);
        assertNotEquals(a,d);
    }

    @Test
    public void
    testNotEqualToTemporalRange()
    {
        ZonedDateTimeRange range = ZonedDateTimeRange.of(BEGINNING,END);
        TemporalRange<ZonedDateTime> temporal = new TemporalRange<>(BEGINNING,END);

        assertNotEquals(range,temporal);
        assertNotEquals(temporal,range);
    }

    @Test
    public void
    testToString()
    {
        ZonedDateTimeRange range = ZonedDateTimeRange.of(BEGINNING,END);

        assertEquals(
            "{beginning=2024-01-01T10:00-05:00[America/New_York],end=2024-01-01T11:30-05:00[America/New_York]}",
            range.toString());
    }

    @Test
    public void
    testSerializationRoundTrip() throws IOException, ClassNotFoundException
    {
        ZonedDateTimeRange expected = ZonedDateTimeRange.of(BEGINNING,END);
        ZonedDateTimeRange actual = roundTrip(expected);

        assertEquals(ZonedDateTimeRange.class,actual.getClass());
        assertEquals(expected,actual);
        assertEquals(expected.hashCode(),actual.hashCode());
        assertEquals(BEGINNING,actual.getBeginning());
        assertEquals(END,actual.getEnd());
    }

}

//////////////////////////////////////////////////////////////////////////////
