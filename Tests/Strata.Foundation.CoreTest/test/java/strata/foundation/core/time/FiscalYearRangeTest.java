//////////////////////////////////////////////////////////////////////////////
// FiscalYearRangeTest.java
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
class FiscalYearRangeTest
{
    private static final FiscalYear BEGINNING = FiscalYear.of(FiscalCalendar.US_FEDERAL,2025);
    private static final FiscalYear END = FiscalYear.of(FiscalCalendar.US_FEDERAL,2027);

    private FiscalYearRange
    roundTrip(FiscalYearRange range) throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bytes))
        {
            out.writeObject(range);
        }

        try (ObjectInputStream in =
                 new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())))
        {
            return (FiscalYearRange)in.readObject();
        }
    }

    @Test
    public void
    testConstructorAndGetters()
    {
        FiscalYearRange range = new FiscalYearRange(BEGINNING,END);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(END,range.getEnd());
    }

    @Test
    public void
    testOfAndGetters()
    {
        FiscalYearRange range = FiscalYearRange.of(BEGINNING,END);

        assertEquals(BEGINNING,range.getBeginning());
        assertEquals(END,range.getEnd());
    }

    @Test
    public void
    testEqualEndpointsAllowed()
    {
        FiscalYearRange range = FiscalYearRange.of(BEGINNING,BEGINNING);

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
                () -> FiscalYearRange.of(END,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testReversedConstructorThrows()
    {
        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> new FiscalYearRange(END,BEGINNING));

        assertEquals("beginning must be before end",e.getMessage());
    }

    @Test
    public void
    testNullBeginningThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> FiscalYearRange.of(null,END));

        assertEquals("beginning is null",e.getMessage());
    }

    @Test
    public void
    testNullEndThrows()
    {
        NullPointerException e =
            assertThrows(
                NullPointerException.class,
                () -> new FiscalYearRange(BEGINNING,null));

        assertEquals("end is null",e.getMessage());
    }

    @Test
    public void
    testEqualsAndHashCode()
    {
        FiscalYearRange a = FiscalYearRange.of(BEGINNING,END);
        FiscalYearRange b = new FiscalYearRange(BEGINNING,END);
        FiscalYearRange c = FiscalYearRange.of(BEGINNING,BEGINNING);

        assertEquals(a,b);
        assertEquals(a.hashCode(),b.hashCode());
        assertNotEquals(a,c);
        assertNotEquals(a,null);
    }

    @Test
    public void
    testNotEqualToBaseRanges()
    {
        FiscalYearRange range = FiscalYearRange.of(BEGINNING,END);
        TemporalRange<FiscalYear> temporalRange = new TemporalRange<>(BEGINNING,END);
        CalendarPeriodRange<FiscalYear> periodRange =
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
        assertEquals("{beginning=2025[US_FEDERAL],end=2027[US_FEDERAL]}",FiscalYearRange.of(BEGINNING,END).toString());
    }

    @Test
    public void
    testSerializationRoundTrip() throws IOException, ClassNotFoundException
    {
        FiscalYearRange expected = FiscalYearRange.of(BEGINNING,END);
        FiscalYearRange actual = roundTrip(expected);

        assertEquals(FiscalYearRange.class,actual.getClass());
        assertEquals(expected,actual);
        assertEquals(expected.hashCode(),actual.hashCode());
        assertEquals(BEGINNING,actual.getBeginning());
        assertEquals(END,actual.getEnd());
    }

    @Test
    public void
    testLengthInPeriods()
    {
        assertEquals(3L,FiscalYearRange.of(BEGINNING,END).lengthInPeriods());
    }

}

//////////////////////////////////////////////////////////////////////////////
