//////////////////////////////////////////////////////////////////////////////
// YearMonthRangeTest.java
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
import java.time.YearMonth;
import java.time.Period;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("CommitStage")
public
class YearMonthRangeTest
{
    private static final YearMonth BEGINNING = YearMonth.of(2024,11);
    private static final YearMonth END = YearMonth.of(2026,1);

    private YearMonthRange
    roundTrip(YearMonthRange range) throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bytes))
        {
            out.writeObject(range);
        }

        try (ObjectInputStream in =
                 new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())))
        {
            return (YearMonthRange)in.readObject();
        }
    }

    public static Stream<Arguments>
    periodSources()
    {
        return
            Stream.of(
                Arguments.of(
                    YearMonth.of(2024,11),
                    YearMonth.of(2026,1),
                    Period.of(1,2,0)),
                Arguments.of(
                    YearMonth.of(2024,1),
                    YearMonth.of(2024,12),
                    Period.ofMonths(11)),
                Arguments.of(
                    YearMonth.of(2024,5),
                    YearMonth.of(2024,5),
                    Period.ZERO));
    }

    public static Stream<Arguments>
    durationSources()
    {
        return
            Stream.of(
                Arguments.of(
                    YearMonth.of(2024,1),
                    YearMonth.of(2024,3),
                    Duration.ofDays(60)),
                Arguments.of(
                    YearMonth.of(2023,1),
                    YearMonth.of(2023,3),
                    Duration.ofDays(59)),
                Arguments.of(
                    YearMonth.of(2024,5),
                    YearMonth.of(2024,5),
                    Duration.ZERO));
    }

    @Test
    public void
    testConstructorAndGetters()
    {
        YearMonthRange range = new YearMonthRange(BEGINNING,END);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(END,range.getEnd());
    }

    @Test
    public void
    testOfAndGetters()
    {
        YearMonthRange range = YearMonthRange.of(BEGINNING,END);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(END,range.getEnd());
    }

    @Test
    public void
    testEqualEndpointsRange()
    {
        YearMonthRange range = YearMonthRange.of(BEGINNING,BEGINNING);

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
                () -> YearMonthRange.of(END,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testReversedConstructorThrows()
    {
        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> new YearMonthRange(END,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testNullBeginningThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> YearMonthRange.of(null,END));

        assertEquals("beginning is null",e.getMessage());
    }

    @Test
    public void
    testNullBeginningConstructorThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> new YearMonthRange(null,END));

        assertEquals("beginning is null",e.getMessage());
    }

    @Test
    public void
    testNullEndThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> new YearMonthRange(BEGINNING,null));

        assertEquals("end is null",e.getMessage());
    }

    @Test
    public void
    testNullEndOfThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> YearMonthRange.of(BEGINNING,null));

        assertEquals("end is null",e.getMessage());
    }

    @ParameterizedTest
    @MethodSource("periodSources")
    public void
    testGetPeriod(YearMonth beginning,YearMonth end,Period expected)
    {
        assertEquals(expected,YearMonthRange.of(beginning,end).getPeriod());
    }

    @ParameterizedTest
    @MethodSource("durationSources")
    public void
    testGetDuration(YearMonth beginning,YearMonth end,Duration expected)
    {
        assertEquals(expected,YearMonthRange.of(beginning,end).getDuration());
    }

    @Test
    public void
    testEqualsAndHashCode()
    {
        YearMonthRange a = YearMonthRange.of(BEGINNING,END);
        YearMonthRange b = new YearMonthRange(BEGINNING,END);
        YearMonthRange c = YearMonthRange.of(BEGINNING,END.plusMonths(1));
        YearMonthRange d = YearMonthRange.of(BEGINNING.minusMonths(1),END);

        assertEquals(a,b);
        assertEquals(a.hashCode(),b.hashCode());
        assertNotEquals(a,c);
        assertNotEquals(a,d);
    }

    @Test
    public void
    testNotEqualToTemporalRange()
    {
        YearMonthRange range = YearMonthRange.of(BEGINNING,END);
        TemporalRange<YearMonth> other = new TemporalRange<>(BEGINNING,END);

        assertNotEquals(range,other);
        assertNotEquals(other,range);
    }

    @Test
    public void
    testToString()
    {
        assertEquals("{beginning=2024-11,end=2026-01}",YearMonthRange.of(BEGINNING,END).toString());
    }

    @Test
    public void
    testSerializationRoundTrip() throws IOException, ClassNotFoundException
    {
        YearMonthRange expected = YearMonthRange.of(BEGINNING,END);
        YearMonthRange actual = roundTrip(expected);

        assertEquals(YearMonthRange.class,actual.getClass());
        assertEquals(expected,actual);
        assertEquals(expected.hashCode(),actual.hashCode());
        assertEquals(BEGINNING,actual.getBeginning());
        assertEquals(END,actual.getEnd());
    }

}

//////////////////////////////////////////////////////////////////////////////
