//////////////////////////////////////////////////////////////////////////////
// LocalDateRangeTest.java
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
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("CommitStage")
public
class LocalDateRangeTest
{
    private static final LocalDate BEGINNING = LocalDate.of(2024,1,15);
    private static final LocalDate END = LocalDate.of(2024,3,20);

    private LocalDateRange
    roundTrip(LocalDateRange range) throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bytes))
        {
            out.writeObject(range);
        }

        try (ObjectInputStream in =
                 new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())))
        {
            return (LocalDateRange)in.readObject();
        }
    }

    public static Stream<Arguments>
    periodSources()
    {
        return
            Stream.of(
                Arguments.of(
                    LocalDate.of(2024,1,15),
                    LocalDate.of(2024,3,20),
                    Period.of(0,2,5)),
                Arguments.of(
                    LocalDate.of(2024,2,28),
                    LocalDate.of(2024,3,1),
                    Period.ofDays(2)),
                Arguments.of(
                    LocalDate.of(2023,2,28),
                    LocalDate.of(2023,3,1),
                    Period.ofDays(1)),
                Arguments.of(
                    LocalDate.of(2020,2,29),
                    LocalDate.of(2024,2,29),
                    Period.ofYears(4)));
    }

    public static Stream<Arguments>
    durationSources()
    {
        return
            Stream.of(
                Arguments.of(
                    LocalDate.of(2024,1,1),
                    LocalDate.of(2024,12,31),
                    Duration.ofDays(365)),
                Arguments.of(
                    LocalDate.of(2024,2,28),
                    LocalDate.of(2024,3,1),
                    Duration.ofDays(2)),
                Arguments.of(
                    LocalDate.of(2024,5,5),
                    LocalDate.of(2024,5,5),
                    Duration.ZERO));
    }

    @Test
    public void
    testConstructorAndGetters()
    {
        LocalDateRange range = new LocalDateRange(BEGINNING,END);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(END,range.getEnd());
    }

    @Test
    public void
    testOfAndGetters()
    {
        LocalDateRange range = LocalDateRange.of(BEGINNING,END);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(END,range.getEnd());
    }

    @Test
    public void
    testSameDayRange()
    {
        LocalDateRange range = LocalDateRange.of(BEGINNING,BEGINNING);

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
                () -> LocalDateRange.of(END,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testReversedConstructorThrows()
    {
        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> new LocalDateRange(END,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testNullBeginningThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> LocalDateRange.of(null,END));

        assertEquals("beginning is null",e.getMessage());
    }

    @Test
    public void
    testNullEndThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> new LocalDateRange(BEGINNING,null));

        assertEquals("end is null",e.getMessage());
    }

    @ParameterizedTest
    @MethodSource("periodSources")
    public void
    testGetPeriod(LocalDate beginning,LocalDate end,Period expected)
    {
        assertEquals(expected,LocalDateRange.of(beginning,end).getPeriod());
    }

    @ParameterizedTest
    @MethodSource("durationSources")
    public void
    testGetDuration(LocalDate beginning,LocalDate end,Duration expected)
    {
        assertEquals(expected,LocalDateRange.of(beginning,end).getDuration());
    }

    @Test
    public void
    testEqualsAndHashCode()
    {
        LocalDateRange a = LocalDateRange.of(BEGINNING,END);
        LocalDateRange b = new LocalDateRange(BEGINNING,END);
        LocalDateRange c = LocalDateRange.of(BEGINNING,END.plusDays(1));
        LocalDateRange d = LocalDateRange.of(BEGINNING.minusDays(1),END);

        assertEquals(a,b);
        assertEquals(a.hashCode(),b.hashCode());
        assertNotEquals(a,c);
        assertNotEquals(a,d);
    }

    @Test
    public void
    testSerializationRoundTrip() throws IOException, ClassNotFoundException
    {
        LocalDateRange expected = LocalDateRange.of(BEGINNING,END);
        LocalDateRange actual = roundTrip(expected);

        assertEquals(LocalDateRange.class,actual.getClass());
        assertEquals(expected,actual);
        assertEquals(expected.hashCode(),actual.hashCode());
        assertEquals(BEGINNING,actual.getBeginning());
        assertEquals(END,actual.getEnd());
    }

}

//////////////////////////////////////////////////////////////////////////////
