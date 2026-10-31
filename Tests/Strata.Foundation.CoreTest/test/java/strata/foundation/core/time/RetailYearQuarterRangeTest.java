//////////////////////////////////////////////////////////////////////////////
// RetailYearQuarterRangeTest.java
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
class RetailYearQuarterRangeTest
{
    private static final RetailYearQuarter BEGINNING = RetailYearQuarter.of(RetailCalendar.NRF,2024,1);
    private static final RetailYearQuarter END = RetailYearQuarter.of(RetailCalendar.NRF,2024,3);

    private RetailYearQuarterRange
    roundTrip(RetailYearQuarterRange range) throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bytes))
        {
            out.writeObject(range);
        }

        try (ObjectInputStream in =
                 new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())))
        {
            return (RetailYearQuarterRange)in.readObject();
        }
    }

    @Test
    public void
    testConstructorAndGetters()
    {
        RetailYearQuarterRange range = new RetailYearQuarterRange(BEGINNING,END);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(END,range.getEnd());
    }

    @Test
    public void
    testOfAndGetters()
    {
        RetailYearQuarterRange range = RetailYearQuarterRange.of(BEGINNING,END);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(END,range.getEnd());
    }

    @Test
    public void
    testEqualEndpointsAllowed()
    {
        RetailYearQuarterRange range = RetailYearQuarterRange.of(BEGINNING,BEGINNING);

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
                () -> RetailYearQuarterRange.of(END,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testReversedConstructorThrows()
    {
        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> new RetailYearQuarterRange(END,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testNullBeginningThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> RetailYearQuarterRange.of(null,END));

        assertEquals("beginning is null",e.getMessage());
    }

    @Test
    public void
    testNullEndThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> new RetailYearQuarterRange(BEGINNING,null));

        assertEquals("end is null",e.getMessage());
    }

    @Test
    public void
    testEqualsAndHashCode()
    {
        RetailYearQuarterRange a = RetailYearQuarterRange.of(BEGINNING,END);
        RetailYearQuarterRange b = new RetailYearQuarterRange(BEGINNING,END);
        RetailYearQuarterRange c = RetailYearQuarterRange.of(BEGINNING,BEGINNING);

        assertEquals(a,b);
        assertEquals(a.hashCode(),b.hashCode());
        assertNotEquals(a,c);
        assertNotEquals(a,null);
    }

    @Test
    public void
    testNotEqualToBaseRanges()
    {
        RetailYearQuarterRange range = RetailYearQuarterRange.of(BEGINNING,END);
        TemporalRange<RetailYearQuarter> temporalRange = new TemporalRange<>(BEGINNING,END);
        CalendarPeriodRange<RetailYearQuarter> periodRange =
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
        assertEquals("{beginning=2024-Q1[NRF],end=2024-Q3[NRF]}",RetailYearQuarterRange.of(BEGINNING,END).toString());
    }

    @Test
    public void
    testSerializationRoundTrip() throws IOException, ClassNotFoundException
    {
        RetailYearQuarterRange expected = RetailYearQuarterRange.of(BEGINNING,END);
        RetailYearQuarterRange actual = roundTrip(expected);

        assertEquals(RetailYearQuarterRange.class,actual.getClass());
        assertEquals(expected,actual);
        assertEquals(expected.hashCode(),actual.hashCode());
        assertEquals(BEGINNING,actual.getBeginning());
        assertEquals(END,actual.getEnd());
    }

    @Test
    public void
    testLengthInPeriods()
    {
        assertEquals(3L,RetailYearQuarterRange.of(BEGINNING,END).lengthInPeriods());
    }

}

//////////////////////////////////////////////////////////////////////////////
