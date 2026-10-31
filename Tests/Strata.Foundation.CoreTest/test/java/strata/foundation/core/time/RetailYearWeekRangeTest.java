//////////////////////////////////////////////////////////////////////////////
// RetailYearWeekRangeTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("CommitStage")
public
class RetailYearWeekRangeTest
{
    private static final RetailYearWeek BEGINNING = RetailYearWeek.of(RetailCalendar.NRF,2024,1);
    private static final RetailYearWeek END = RetailYearWeek.of(RetailCalendar.NRF,2024,5);

    private RetailYearWeekRange
    roundTrip(RetailYearWeekRange range) throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bytes))
        {
            out.writeObject(range);
        }

        try (ObjectInputStream in =
                 new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())))
        {
            return (RetailYearWeekRange)in.readObject();
        }
    }

    @Test
    public void
    testConstructorAndGetters()
    {
        RetailYearWeekRange range = new RetailYearWeekRange(BEGINNING,END);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(END,range.getEnd());
    }

    @Test
    public void
    testOfAndGetters()
    {
        RetailYearWeekRange range = RetailYearWeekRange.of(BEGINNING,END);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(END,range.getEnd());
    }

    @Test
    public void
    testEqualEndpointsAllowed()
    {
        RetailYearWeekRange range = RetailYearWeekRange.of(BEGINNING,BEGINNING);

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
                () -> RetailYearWeekRange.of(END,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testReversedConstructorThrows()
    {
        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> new RetailYearWeekRange(END,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testNullBeginningThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> RetailYearWeekRange.of(null,END));

        assertEquals("beginning is null",e.getMessage());
    }

    @Test
    public void
    testNullEndThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> new RetailYearWeekRange(BEGINNING,null));

        assertEquals("end is null",e.getMessage());
    }

    @Test
    public void
    testEqualsAndHashCode()
    {
        RetailYearWeekRange a = RetailYearWeekRange.of(BEGINNING,END);
        RetailYearWeekRange b = new RetailYearWeekRange(BEGINNING,END);
        RetailYearWeekRange c = RetailYearWeekRange.of(BEGINNING,BEGINNING);

        assertEquals(a,b);
        assertEquals(a.hashCode(),b.hashCode());
        assertNotEquals(a,c);
        assertNotEquals(a,null);
    }

    @Test
    public void
    testNotEqualToBaseRanges()
    {
        RetailYearWeekRange range = RetailYearWeekRange.of(BEGINNING,END);
        TemporalRange<RetailYearWeek> temporalRange = new TemporalRange<>(BEGINNING,END);
        CalendarPeriodRange<RetailYearWeek> periodRange =
            CalendarPeriodRange.ofCalendarPeriods(BEGINNING,END);

        assertNotEquals(range,temporalRange);
        assertNotEquals(temporalRange,range);
        assertNotEquals(range,periodRange);
        assertNotEquals(periodRange,range);
    }

    @Test
    public void
    testToString()
    {
        assertEquals("{beginning=2024-W01[NRF],end=2024-W05[NRF]}",RetailYearWeekRange.of(BEGINNING,END).toString());
    }

    @Test
    public void
    testSerializationRoundTrip() throws IOException, ClassNotFoundException
    {
        RetailYearWeekRange expected = RetailYearWeekRange.of(BEGINNING,END);
        RetailYearWeekRange actual = roundTrip(expected);

        assertEquals(RetailYearWeekRange.class,actual.getClass());
        assertEquals(expected,actual);
        assertEquals(expected.hashCode(),actual.hashCode());
        assertEquals(BEGINNING,actual.getBeginning());
        assertEquals(END,actual.getEnd());
    }

    @Test
    public void
    testLengthInPeriods()
    {
        assertEquals(5L,RetailYearWeekRange.of(BEGINNING,END).lengthInPeriods());
    }

}

//////////////////////////////////////////////////////////////////////////////
