//////////////////////////////////////////////////////////////////////////////
// FiscalYearQuarterRangeTest.java
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
class FiscalYearQuarterRangeTest
{
    private static final FiscalYearQuarter BEGINNING = FiscalYearQuarter.of(FiscalCalendar.US_FEDERAL,2025,1);
    private static final FiscalYearQuarter END = FiscalYearQuarter.of(FiscalCalendar.US_FEDERAL,2025,4);

    private FiscalYearQuarterRange
    roundTrip(FiscalYearQuarterRange range) throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bytes))
        {
            out.writeObject(range);
        }

        try (ObjectInputStream in =
                 new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())))
        {
            return (FiscalYearQuarterRange)in.readObject();
        }
    }

    @Test
    public void
    testConstructorAndGetters()
    {
        FiscalYearQuarterRange range = new FiscalYearQuarterRange(BEGINNING,END);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(END,range.getEnd());
    }

    @Test
    public void
    testOfAndGetters()
    {
        FiscalYearQuarterRange range = FiscalYearQuarterRange.of(BEGINNING,END);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(END,range.getEnd());
    }

    @Test
    public void
    testEqualEndpointsAllowed()
    {
        FiscalYearQuarterRange range = FiscalYearQuarterRange.of(BEGINNING,BEGINNING);

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
                () -> FiscalYearQuarterRange.of(END,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testReversedConstructorThrows()
    {
        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> new FiscalYearQuarterRange(END,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testNullBeginningThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> FiscalYearQuarterRange.of(null,END));

        assertEquals("beginning is null",e.getMessage());
    }

    @Test
    public void
    testNullEndThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> new FiscalYearQuarterRange(BEGINNING,null));

        assertEquals("end is null",e.getMessage());
    }

    @Test
    public void
    testEqualsAndHashCode()
    {
        FiscalYearQuarterRange a = FiscalYearQuarterRange.of(BEGINNING,END);
        FiscalYearQuarterRange b = new FiscalYearQuarterRange(BEGINNING,END);
        FiscalYearQuarterRange c = FiscalYearQuarterRange.of(BEGINNING,BEGINNING);

        assertEquals(a,b);
        assertEquals(a.hashCode(),b.hashCode());
        assertNotEquals(a,c);
        assertNotEquals(a,null);
    }

    @Test
    public void
    testNotEqualToBaseRanges()
    {
        FiscalYearQuarterRange range = FiscalYearQuarterRange.of(BEGINNING,END);
        TemporalRange<FiscalYearQuarter> temporalRange = new TemporalRange<>(BEGINNING,END);
        CalendarPeriodRange<FiscalYearQuarter> periodRange =
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
        assertEquals("{beginning=2025-Q1[US_FEDERAL],end=2025-Q4[US_FEDERAL]}",FiscalYearQuarterRange.of(BEGINNING,END).toString());
    }

    @Test
    public void
    testSerializationRoundTrip() throws IOException, ClassNotFoundException
    {
        FiscalYearQuarterRange expected = FiscalYearQuarterRange.of(BEGINNING,END);
        FiscalYearQuarterRange actual = roundTrip(expected);

        assertEquals(FiscalYearQuarterRange.class,actual.getClass());
        assertEquals(expected,actual);
        assertEquals(expected.hashCode(),actual.hashCode());
        assertEquals(BEGINNING,actual.getBeginning());
        assertEquals(END,actual.getEnd());
    }

    @Test
    public void
    testLengthInPeriods()
    {
        assertEquals(4L,FiscalYearQuarterRange.of(BEGINNING,END).lengthInPeriods());
    }

}

//////////////////////////////////////////////////////////////////////////////
