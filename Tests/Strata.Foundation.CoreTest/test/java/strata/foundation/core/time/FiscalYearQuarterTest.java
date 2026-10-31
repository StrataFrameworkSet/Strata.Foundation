//////////////////////////////////////////////////////////////////////////////
// FiscalYearQuarterTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import strata.foundation.core.mapper.ObjectMapperSupplier;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.IsoFields;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("CommitStage")
public
class FiscalYearQuarterTest
{
    private static final FiscalCalendar US = FiscalCalendar.US_FEDERAL;
    private static final FiscalCalendar UK = FiscalCalendar.UK_GOVERNMENT;

    private FiscalYearQuarter
    roundTrip(FiscalYearQuarter value) throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bytes))
        {
            out.writeObject(value);
        }

        try (ObjectInputStream in =
                 new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())))
        {
            return (FiscalYearQuarter)in.readObject();
        }
    }

    @Test
    public void
    testOf()
    {
        FiscalYearQuarter value = FiscalYearQuarter.of(US,2025,1);

        assertEquals(2025,value.getYear());
        assertEquals(1,value.getQuarter());
        assertEquals(LocalDate.of(2024,10,1),value.getFirstDay());
        assertEquals(LocalDate.of(2024,12,31),value.getLastDay());
        assertEquals(92,value.lengthInDays());
        assertEquals("2025-Q1",value.format());
        assertEquals("2025-Q1[US_FEDERAL]",value.toString());
    }

    @Test
    public void
    testOfSecondQuarter()
    {
        FiscalYearQuarter value = FiscalYearQuarter.of(US,2025,2);

        assertEquals(LocalDate.of(2025,1,1),value.getFirstDay());
        assertEquals(LocalDate.of(2025,3,31),value.getLastDay());
        assertEquals(90,value.lengthInDays());
        assertEquals(91,FiscalYearQuarter.of(US,2024,2).lengthInDays());
    }

    @Test
    public void
    testOfInvalid()
    {
        assertThrows(DateTimeException.class,() -> FiscalYearQuarter.of(US,2025,5));
    }

    @Test
    public void
    testFrom()
    {
        assertEquals(FiscalYearQuarter.of(US,2025,2),FiscalYearQuarter.from(US,LocalDate.of(2025,1,15)));
        assertEquals(FiscalYearQuarter.of(US,2025,1),FiscalYearQuarter.from(US,LocalDate.of(2024,10,1)));
    }

    @Test
    public void
    testPlusAndMinus()
    {
        assertEquals(FiscalYearQuarter.of(US,2026,1),FiscalYearQuarter.of(US,2025,4).plusQuarters(1));
        assertEquals(FiscalYearQuarter.of(US,2024,4),FiscalYearQuarter.of(US,2025,1).minusQuarters(1));
        assertEquals(FiscalYearQuarter.of(US,2026,1),FiscalYearQuarter.of(US,2025,1).plusYears(1));
    }

    @Test
    public void
    testUntil()
    {
        assertEquals(
            4L,
            FiscalYearQuarter.of(US,2025,1).until(FiscalYearQuarter.of(US,2026,1),IsoFields.QUARTER_YEARS));
    }

    @Test
    public void
    testWithQuarter()
    {
        assertEquals(FiscalYearQuarter.of(US,2025,3),FiscalYearQuarter.of(US,2025,1).withQuarter(3));
        assertThrows(DateTimeException.class,() -> FiscalYearQuarter.of(US,2025,1).withQuarter(5));
    }

    @Test
    public void
    testConversions()
    {
        FiscalYearQuarter value = FiscalYearQuarter.of(US,2025,2);

        assertEquals(FiscalYear.of(US,2025),value.getFiscalYear());
        assertEquals(FiscalYearMonth.of(US,2025,5),value.atMonth(2));
        assertThrows(DateTimeException.class,() -> value.atMonth(4));
    }

    @Test
    public void
    testAtDay()
    {
        FiscalYearQuarter value = FiscalYearQuarter.of(US,2025,2);

        assertEquals(LocalDate.of(2025,3,31),value.atDay(90));
        assertThrows(DateTimeException.class,() -> value.atDay(91));
    }

    @Test
    public void
    testAdjustInto()
    {
        assertEquals(
            LocalDate.of(2025,7,15),
            LocalDate.of(2024,10,15).with(FiscalYearQuarter.of(US,2025,4)));
    }

    @Test
    public void
    testParse()
    {
        assertEquals(FiscalYearQuarter.of(US,2025,1),FiscalYearQuarter.parse("2025-Q1[US_FEDERAL]"));
    }

    @Test
    public void
    testParseInvalid()
    {
        assertThrows(DateTimeParseException.class,() -> FiscalYearQuarter.parse("2025-Q5[US_FEDERAL]"));
    }

    @Test
    public void
    testCompareToDifferentCalendar()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> FiscalYearQuarter.of(US,2025,1).compareTo(FiscalYearQuarter.of(UK,2025,1)));
    }

    @Test
    public void
    testCompareTo()
    {
        FiscalYearQuarter first  = FiscalYearQuarter.of(US,2025,1);
        FiscalYearQuarter second = FiscalYearQuarter.of(US,2025,2);

        assertTrue(first.compareTo(second) < 0);
        assertTrue(second.compareTo(first) > 0);
        assertEquals(0,first.compareTo(FiscalYearQuarter.of(US,2025,1)));
    }

    @Test
    public void
    testEquals()
    {
        FiscalYearQuarter value = FiscalYearQuarter.of(US,2025,1);

        assertEquals(value,FiscalYearQuarter.of(US,2025,1));
        assertEquals(value.hashCode(),FiscalYearQuarter.of(US,2025,1).hashCode());
        assertNotEquals(value,FiscalYearQuarter.of(US,2025,2));
        assertNotEquals(value,FiscalYearQuarter.of(UK,2025,1));
    }

    @Test
    public void
    testSerialization() throws IOException, ClassNotFoundException
    {
        FiscalYearQuarter expected = FiscalYearQuarter.of(US,2025,1);

        assertEquals(expected,roundTrip(expected));
    }

    @Test
    public void
    testJsonWrite() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapperSupplier().get();

        assertEquals(
            "{\"calendar\":\"US_FEDERAL\",\"value\":\"2025-Q1\"}",
            mapper.writeValueAsString(FiscalYearQuarter.of(US,2025,1)));
    }

    @Test
    public void
    testJsonRead() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapperSupplier().get();

        assertEquals(
            FiscalYearQuarter.of(US,2025,1),
            mapper.readValue(
                "{\"calendar\":\"US_FEDERAL\",\"value\":\"2025-Q1\"}",
                FiscalYearQuarter.class));
    }
}

//////////////////////////////////////////////////////////////////////////////
