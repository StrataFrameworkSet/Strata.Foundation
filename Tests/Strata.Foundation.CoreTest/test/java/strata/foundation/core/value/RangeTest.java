//////////////////////////////////////////////////////////////////////////////
// RangeTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("CommitStage")
public
class RangeTest
{
    public static Stream<Arguments>
    reversedSource()
    {
        return Stream.of(
            Arguments.of(5,1),
            Arguments.of("b","a"),
            Arguments.of(new BigDecimal("2.5"),new BigDecimal("1.5")),
            Arguments.of(2.0d,1.0d));
    }

    @SuppressWarnings("unchecked")
    private static Comparator<Object>
    natural()
    {
        return (a,b) -> ((Comparable<Object>)a).compareTo(b);
    }

    @SuppressWarnings("unchecked")
    private static <T> T
    roundTrip(T value) throws IOException,ClassNotFoundException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bytes))
        {
            out.writeObject(value);
        }

        try (ObjectInputStream in =
                 new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())))
        {
            return (T)in.readObject();
        }
    }

    @Test
    public void
    testConstructorWithInteger()
    {
        Integer      beginning = 1000;
        Integer      end = 2000;
        Range<Integer> range = new Range<Integer>(beginning,end,Integer::compare);

        assertSame(beginning,range.getBeginning());
        assertSame(end,range.getEnd());
    }

    @Test
    public void
    testOfWithInteger()
    {
        Integer      beginning = 1000;
        Integer      end = 2000;
        Range<Integer> range = Range.of(beginning,end);

        assertSame(beginning,range.getBeginning());
        assertSame(end,range.getEnd());
    }

    @Test
    public void
    testOfWithString()
    {
        String        beginning = new String("apple");
        String        end = new String("pear");
        Range<String> range = Range.of(beginning,end);

        assertSame(beginning,range.getBeginning());
        assertSame(end,range.getEnd());
    }

    @Test
    public void
    testOfWithBigDecimal()
    {
        BigDecimal        beginning = new BigDecimal("1.25");
        BigDecimal        end = new BigDecimal("9.75");
        Range<BigDecimal> range = Range.of(beginning,end);

        assertSame(beginning,range.getBeginning());
        assertSame(end,range.getEnd());
    }

    @Test
    public void
    testOfWithComparator()
    {
        Integer        beginning = 1000;
        Integer        end = 2000;
        Range<Integer> range = Range.of(beginning,end,Integer::compare);

        assertSame(beginning,range.getBeginning());
        assertSame(end,range.getEnd());
    }

    @Test
    public void
    testEqualBeginningAndEndAllowed()
    {
        Range<Integer> range = Range.of(7,7);

        assertEquals(7,range.getBeginning());
        assertEquals(7,range.getEnd());
    }

    @ParameterizedTest
    @MethodSource("reversedSource")
    public void
    testReversedThrows(Object beginning,Object end)
    {
        IllegalArgumentException exception =
            assertThrows(
                IllegalArgumentException.class,
                () -> new Range<Object>(beginning,end,natural()));

        assertEquals("beginning must be before end",exception.getMessage());
    }

    @Test
    public void
    testReversedOfThrows()
    {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class,() -> Range.of(5,1));

        assertEquals("beginning must be before end",exception.getMessage());
    }

    @Test
    public void
    testNullBeginningThrows()
    {
        NullPointerException exception =
            assertThrows(
                NullPointerException.class,
                () -> new Range<Integer>(null,1,Integer::compare));

        assertEquals("beginning is null",exception.getMessage());
    }

    @Test
    public void
    testNullEndThrows()
    {
        NullPointerException exception =
            assertThrows(
                NullPointerException.class,
                () -> new Range<Integer>(1,null,Integer::compare));

        assertEquals("end is null",exception.getMessage());
    }

    @Test
    public void
    testNullComparatorThrows()
    {
        NullPointerException exception =
            assertThrows(
                NullPointerException.class,
                () -> new Range<Integer>(1,2,null));

        assertEquals("comparator is null",exception.getMessage());
    }

    @Test
    public void
    testBothNullReportsBeginningFirst()
    {
        NullPointerException exception =
            assertThrows(
                NullPointerException.class,
                () -> new Range<Integer>(null,null,Integer::compare));

        assertEquals("beginning is null",exception.getMessage());
    }

    @Test
    public void
    testNullEndAndNullComparatorReportsEndFirst()
    {
        NullPointerException exception =
            assertThrows(
                NullPointerException.class,
                () -> new Range<Integer>(1,null,null));

        assertEquals("end is null",exception.getMessage());
    }

    @Test
    public void
    testComparatorNotInvokedWhenArgumentIsNull()
    {
        Comparator<Integer> throwing =
            (a,b) -> { throw new IllegalStateException("compare invoked"); };

        NullPointerException beginning =
            assertThrows(
                NullPointerException.class,
                () -> new Range<Integer>(null,1,throwing));
        NullPointerException end =
            assertThrows(
                NullPointerException.class,
                () -> new Range<Integer>(1,null,throwing));

        assertEquals("beginning is null",beginning.getMessage());
        assertEquals("end is null",end.getMessage());
    }

    @Test
    public void
    testReverseOrderComparatorAllowsDescending()
    {
        Range<Integer> range = Range.of(10,1,Comparator.<Integer>reverseOrder());

        assertEquals(10,range.getBeginning());
        assertEquals(1,range.getEnd());
    }

    @Test
    public void
    testReverseOrderComparatorRejectsAscending()
    {
        IllegalArgumentException exception =
            assertThrows(
                IllegalArgumentException.class,
                () -> Range.of(1,10,Comparator.<Integer>reverseOrder()));

        assertEquals("beginning must be before end",exception.getMessage());
    }

    @Test
    public void
    testCaseInsensitiveComparatorTreatsCaseVariantsAsEqual()
    {
        Range<String> range = Range.of("a","A",String.CASE_INSENSITIVE_ORDER);

        assertEquals("a",range.getBeginning());
        assertEquals("A",range.getEnd());
    }

    @Test
    public void
    testEqualsReflexive()
    {
        Range<Integer> range = Range.of(1,5);

        assertEquals(range,range);
    }

    @Test
    public void
    testEqualsSymmetric()
    {
        Range<Integer> a = Range.of(1,5);
        Range<Integer> b = Range.of(1,5);

        assertEquals(a,b);
        assertEquals(b,a);
    }

    @Test
    public void
    testEqualsTransitive()
    {
        Range<Integer> a = Range.of(1,5);
        Range<Integer> b = Range.of(1,5);
        Range<Integer> c = Range.of(1,5);

        assertEquals(a,b);
        assertEquals(b,c);
        assertEquals(a,c);
    }

    @Test
    public void
    testEqualsConsistent()
    {
        Range<Integer> a = Range.of(1,5);
        Range<Integer> b = Range.of(1,5);

        for (int i = 0; i < 3; i++)
            assertEquals(a,b);
    }

    @Test
    public void
    testNotEqualToNull()
    {
        assertNotEquals(null,Range.of(1,5));
    }

    @Test
    public void
    testNotEqualToDifferentType()
    {
        assertNotEquals("1-5",Range.of(1,5));
        assertNotEquals(Range.of(1,5),"1-5");
    }

    @Test
    public void
    testNotEqualWhenBeginningDiffers()
    {
        assertNotEquals(Range.of(1,5),Range.of(2,5));
    }

    @Test
    public void
    testNotEqualWhenEndDiffers()
    {
        assertNotEquals(Range.of(1,5),Range.of(1,6));
    }

    @Test
    public void
    testEqualRangesHaveEqualHashCodes()
    {
        assertEquals(Range.of(1,5).hashCode(),Range.of(1,5).hashCode());
        assertEquals(Range.of("a","z").hashCode(),Range.of("a","z").hashCode());
    }

    @Test
    public void
    testDifferentComparatorsSameEndpointsAreEqual()
    {
        Range<Integer> a = Range.of(1,5);
        Range<Integer> b = Range.of(1,5,Integer::compare);
        Range<Integer> c = Range.of(1,5,Comparator.<Integer>naturalOrder());

        assertEquals(a,b);
        assertEquals(b,c);
        assertEquals(a.hashCode(),b.hashCode());
        assertEquals(b.hashCode(),c.hashCode());
    }

    @Test
    public void
    testSubclassNotEqual()
    {
        Range<Integer> base = Range.of(1,5);
        Range<Integer> subclass = new Range<Integer>(1,5,Integer::compare) {};

        assertNotEquals(base,subclass);
        assertNotEquals(subclass,base);
    }

    @Test
    public void
    testUsableAsHashSetKey()
    {
        Set<Range<Integer>> set = new HashSet<Range<Integer>>();

        set.add(Range.of(1,5));
        set.add(Range.of(1,5));
        set.add(Range.of(1,5,Integer::compare));
        set.add(Range.of(2,5));

        assertEquals(2,set.size());
        assertEquals(true,set.contains(Range.of(2,5)));
    }

    @Test
    public void
    testSerializationRoundTripInteger() throws Exception
    {
        Range<Integer> expected = Range.of(1,5);
        Range<Integer> actual = roundTrip(expected);

        assertEquals(expected,actual);
        assertEquals(expected.hashCode(),actual.hashCode());
        assertEquals(1,actual.getBeginning());
        assertEquals(5,actual.getEnd());
    }

    @Test
    public void
    testSerializationRoundTripString() throws Exception
    {
        Range<String> expected = Range.of("alpha","omega");
        Range<String> actual = roundTrip(expected);

        assertEquals(expected,actual);
        assertEquals("alpha",actual.getBeginning());
        assertEquals("omega",actual.getEnd());
    }

    @Test
    public void
    testToString()
    {
        assertEquals("{beginning=1,end=5}",Range.of(1,5).toString());
        assertEquals("{beginning=alpha,end=omega}",Range.of("alpha","omega").toString());
    }

    @Test
    public void
    testDifferentTypesThrow()
    {
        Comparator<Number> numeric =
            (a,b) -> Double.compare(a.doubleValue(),b.doubleValue());

        IllegalArgumentException mixedObjects =
            assertThrows(
                IllegalArgumentException.class,
                () -> new Range<Object>(1,"a",natural()));
        IllegalArgumentException mixedNumbers =
            assertThrows(
                IllegalArgumentException.class,
                () -> new Range<Number>(1,2L,numeric));

        assertEquals(
            "beginning and end must be of the same type",
            mixedObjects.getMessage());
        assertEquals(
            "beginning and end must be of the same type",
            mixedNumbers.getMessage());
    }

    @Test
    public void
    testSameTypeCheckedBeforeComparator()
    {
        Comparator<Object> throwing =
            (a,b) -> { throw new IllegalStateException("compare invoked"); };

        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> new Range<Object>(1,"a",throwing));

        assertEquals("beginning and end must be of the same type",e.getMessage());
    }

    @Test
    public void
    testOfWithParentComparableTypes()
    {
        LocalDate     d1 = LocalDate.of(2024,1,1);
        LocalDate     d2 = LocalDate.of(2024,1,2);
        LocalDateTime t1 = LocalDateTime.of(2024,1,1,10,0);
        LocalDateTime t2 = LocalDateTime.of(2024,1,1,11,0);
        ZonedDateTime z1 = ZonedDateTime.of(t1,ZoneOffset.UTC);
        ZonedDateTime z2 = ZonedDateTime.of(t2,ZoneOffset.UTC);

        Range<LocalDate>     dates = Range.of(d1,d2);
        Range<LocalDateTime> dateTimes = Range.of(t1,t2);
        Range<ZonedDateTime> zoned = Range.of(z1,z2);

        assertSame(d1,dates.getBeginning());
        assertSame(d2,dates.getEnd());
        assertSame(t1,dateTimes.getBeginning());
        assertSame(t2,dateTimes.getEnd());
        assertSame(z1,zoned.getBeginning());
        assertSame(z2,zoned.getEnd());
    }

    @Test
    public void
    testOfZonedDateTimeUsesNaturalOrdering()
    {
        ZonedDateTime utc = ZonedDateTime.of(2024,6,1,12,0,0,0,ZoneOffset.UTC);
        ZonedDateTime tokyo = utc.withZoneSameInstant(ZoneId.of("Asia/Tokyo"));

        IllegalArgumentException e =
            assertThrows(
                IllegalArgumentException.class,
                () -> Range.of(tokyo,utc));

        assertEquals("beginning must be before end",e.getMessage());
        assertSame(utc,Range.of(utc,tokyo).getBeginning());
    }

}

//////////////////////////////////////////////////////////////////////////////
