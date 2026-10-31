//////////////////////////////////////////////////////////////////////////////
// RetailYearMonthRangeTest.java
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
class RetailYearMonthRangeTest
{
    private static final RetailYearMonth BEGINNING = RetailYearMonth.of(RetailCalendar.NRF,2024,1);
    private static final RetailYearMonth END = RetailYearMonth.of(RetailCalendar.NRF,2024,3);

    private RetailYearMonthRange
    roundTrip(RetailYearMonthRange range) throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bytes))
        {
            out.writeObject(range);
        }

        try (ObjectInputStream in =
                 new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())))
        {
            return (RetailYearMonthRange)in.readObject();
        }
    }

    @Test
    public void
    testConstructorAndGetters()
    {
        RetailYearMonthRange range = new RetailYearMonthRange(BEGINNING,END);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(END,range.getEnd());
    }

    @Test
    public void
    testOfAndGetters()
    {
        RetailYearMonthRange range = RetailYearMonthRange.of(BEGINNING,END);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(END,range.getEnd());
    }

    @Test
    public void
    testEqualEndpointsAllowed()
    {
        RetailYearMonthRange range = RetailYearMonthRange.of(BEGINNING,BEGINNING);

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
                () -> RetailYearMonthRange.of(END,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testReversedConstructorThrows()
    {
        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> new RetailYearMonthRange(END,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testNullBeginningThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> RetailYearMonthRange.of(null,END));

        assertEquals("beginning is null",e.getMessage());
    }

    @Test
    public void
    testNullEndThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> new RetailYearMonthRange(BEGINNING,null));

        assertEquals("end is null",e.getMessage());
    }

    @Test
    public void
    testEqualsAndHashCode()
    {
        RetailYearMonthRange a = RetailYearMonthRange.of(BEGINNING,END);
        RetailYearMonthRange b = new RetailYearMonthRange(BEGINNING,END);
        RetailYearMonthRange c = RetailYearMonthRange.of(BEGINNING,BEGINNING);

        assertEquals(a,b);
        assertEquals(a.hashCode(),b.hashCode());
        assertNotEquals(a,c);
        assertNotEquals(a,null);
    }

    @Test
    public void
    testNotEqualToBaseRanges()
    {
        RetailYearMonthRange range = RetailYearMonthRange.of(BEGINNING,END);
        TemporalRange<RetailYearMonth> temporalRange = new TemporalRange<>(BEGINNING,END);
        CalendarPeriodRange<RetailYearMonth> periodRange =
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
        assertEquals("{beginning=2024-M01[NRF],end=2024-M03[NRF]}",RetailYearMonthRange.of(BEGINNING,END).toString());
    }

    @Test
    public void
    testSerializationRoundTrip() throws IOException, ClassNotFoundException
    {
        RetailYearMonthRange expected = RetailYearMonthRange.of(BEGINNING,END);
        RetailYearMonthRange actual = roundTrip(expected);

        assertEquals(RetailYearMonthRange.class,actual.getClass());
        assertEquals(expected,actual);
        assertEquals(expected.hashCode(),actual.hashCode());
        assertEquals(BEGINNING,actual.getBeginning());
        assertEquals(END,actual.getEnd());
    }

    @Test
    public void
    testLengthInPeriods()
    {
        assertEquals(3L,RetailYearMonthRange.of(BEGINNING,END).lengthInPeriods());
    }

}

//////////////////////////////////////////////////////////////////////////////
