/// ///////////////////////////////////////////////////////////////////////////
// MultiMapTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.collection;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("CommitStage")
public
class SetValuedMultiMapTest
{
    @Test
    public void
    testPut()
    {
        IMultiMap<String,Integer> subject = new SetValuedMultiMap<>();

        assertEquals(0,subject.size());

        subject
            .put("foo", 1)
            .put("bar", 2)
            .put("baz", 3);

        assertEquals(3,subject.size());

        assertEquals(1,subject.get("foo").size());
        assertTrue(subject.get("foo").contains(1));

        assertEquals(1,subject.get("bar").size());
        assertTrue(subject.get("bar").contains(2));

        assertEquals(1,subject.get("baz").size());
        assertTrue(subject.get("baz").contains(3));

        subject
            .put("foo", 2)
            .put("foo", 3)
            .put("bar", 11)
            .put("bar", 33);

        assertEquals(3,subject.size());
        assertEquals(3,subject.get("foo").size());
        assertEquals(Set.of(1,2,3),subject.get("foo"));
        assertEquals(Set.of(11,2,33),subject.get("bar"));
        assertEquals(Set.of(3),subject.get("baz"));
    }

    @Test
    public void
    testPutAll()
    {
        IMultiMap<String,Integer> subject = new SetValuedMultiMap<>();

        assertEquals(0,subject.size());

        subject
            .putAll("foo", Set.of(1,2,3))
            .putAll("bar", Set.of(2,3,4))
            .putAll("baz", Set.of());

        assertEquals(3,subject.size());

        assertEquals(3,subject.get("foo").size());
        assertEquals(Set.of(1,2,3),subject.get("foo"));

        assertEquals(3,subject.get("bar").size());
        assertEquals(Set.of(2,3,4),subject.get("bar"));

        assertEquals(0,subject.get("baz").size());
        assertEquals(Set.of(),subject.get("baz"));

        subject
            .put("foo", 2)
            .put("foo", 3)
            .put("bar", 11)
            .put("bar", 33)
            .putAll("baz",Set.of(3,5,7,11));

        assertEquals(3,subject.size());

        assertEquals(3,subject.get("foo").size());
        assertEquals(Set.of(1,2,3),subject.get("foo"));

        assertEquals(5,subject.get("bar").size());
        assertEquals(Set.of(2,3,4,11,33),subject.get("bar"));

        assertEquals(4,subject.get("baz").size());
        assertEquals(Set.of(3,5,7,11),subject.get("baz"));
    }

    @Test
    public void
    testFlatten()
    {
        IMultiMap<String,Integer>   subject = new SetValuedMultiMap<>();
        List<Entry<String,Integer>> flattened = null;

        assertEquals(0,subject.size());

        subject
            .putAll("foo", Set.of(1,2,3))
            .putAll("bar", Set.of(2,3,4))
            .putAll("baz", Set.of());

        flattened = subject.flatten();

        assertEquals(6,flattened.size());

        assertEquals(
            3,
            flattened
                .stream()
                .filter(e -> e.getKey().equals("foo"))
                .count());
        assertEquals(
            List.of(1,2,3),
            flattened
                .stream()
                .filter(e -> e.getKey().equals("foo"))
                .map(e -> e.getValue())
                .sorted()
                .collect(Collectors.toList()));

        assertEquals(
            3,
            flattened
                .stream()
                .filter(e -> e.getKey().equals("bar"))
                .count());
        assertEquals(
            List.of(2,3,4),
            flattened
                .stream()
                .filter(e -> e.getKey().equals("bar"))
                .map(e -> e.getValue())
                .sorted()
                .collect(Collectors.toList()));


        assertEquals(
            0,
            flattened
                .stream()
                .filter(e -> e.getKey().equals("baz"))
                .count());
        assertEquals(
            List.of(),
            flattened
                .stream()
                .filter(e -> e.getKey().equals("baz"))
                .map(e -> e.getValue())
                .sorted()
                .collect(Collectors.toList()));
    }

    @Test
    public void
    testMakeUnionWith()
    {
        IMultiMap<String,Integer> subjectX = new SetValuedMultiMap<>();
        IMultiMap<String,Integer> subjectY = new SetValuedMultiMap<>();
        IMultiMap<String,Integer> subjectUnion = null;

        subjectX
            .putAll("a", Set.of(1,2,3))
            .putAll("b", Set.of(2,3,4))
            .putAll("c", Set.of(3,4,5));

        subjectY
            .putAll("b", Set.of(3,4,5))
            .putAll("c", Set.of(4,5,6))
            .putAll("d", Set.of(5,6,7));

        subjectUnion = subjectX.makeUnionWith(subjectY);

        assertEquals(4,subjectUnion.size());
        assertEquals(Set.of("a","b","c","d"),subjectUnion.keySet());
        assertEquals(Set.of(1,2,3),subjectUnion.get("a"));
        assertEquals(Set.of(2,3,4,5),subjectUnion.get("b"));
        assertEquals(Set.of(3,4,5,6),subjectUnion.get("c"));
        assertEquals(Set.of(5,6,7),subjectUnion.get("d"));
    }

    @Test
    public void
    testMakeIntersectionWith()
    {
        IMultiMap<String,Integer> subjectX = new SetValuedMultiMap<>();
        IMultiMap<String,Integer> subjectY = new SetValuedMultiMap<>();
        IMultiMap<String,Integer> subjectIntersection = null;

        subjectX
            .putAll("a", Set.of(1,2,3))
            .putAll("b", Set.of(2,3,4))
            .putAll("c", Set.of(3,4,5));

        subjectY
            .putAll("b", Set.of(3,4,5))
            .putAll("c", Set.of(4,5,6))
            .putAll("d", Set.of(5,6,7));

        subjectIntersection = subjectX.makeIntersectionWith(subjectY);

        assertEquals(2,subjectIntersection.size());
        assertEquals(Set.of("b","c"),subjectIntersection.keySet());
        assertEquals(Set.of(3,4),subjectIntersection.get("b"));
        assertEquals(Set.of(4,5),subjectIntersection.get("c"));

        subjectIntersection = subjectX.makeIntersectionWith(subjectX);

        assertEquals(subjectX.size(),subjectIntersection.size());
        assertEquals(subjectX.keySet(),subjectIntersection.keySet());
        assertEquals(subjectX.get("b"),subjectIntersection.get("b"));
        assertEquals(subjectX.get("c"),subjectIntersection.get("c"));

    }

    @Test
    public void
    testMakeSymmetricDifferenceWith()
    {
        IMultiMap<String,Integer> subjectX = new SetValuedMultiMap<>();
        IMultiMap<String,Integer> subjectY = new SetValuedMultiMap<>();
        IMultiMap<String,Integer> subjectDiff = null;

        subjectX
            .putAll("a", Set.of(1,2,3))
            .putAll("b", Set.of(2,3,4))
            .putAll("c", Set.of(3,4,5));

        subjectY
            .putAll("b", Set.of(3,4,5))
            .putAll("c", Set.of(4,5,6))
            .putAll("d", Set.of(5,6,7));

        subjectDiff = subjectX.makeSymmetricDifferenceWith(subjectY);

        assertEquals(2,subjectDiff.size());
        assertEquals(Set.of("a","d"),subjectDiff.keySet());
        assertEquals(Set.of(1,2,3),subjectDiff.get("a"));
        assertEquals(Set.of(5,6,7),subjectDiff.get("d"));

        subjectDiff = subjectX.makeSymmetricDifferenceWith(subjectX);

        assertEquals(0,subjectDiff.size());
        assertEquals(Set.of(),subjectDiff.keySet());

    }

    //@Test
    public void
    testPutAllTimeComplexity()
    {
        IMultiMap<String,Integer> subject = new SetValuedMultiMap<>();
        Set<Integer>             values = new HashSet<>();
        Instant                  start = null;
        Instant                  finish = null;
        Map<Integer,Long>        durations = new HashMap<>();

        for (int i = 0; i < 10; i++)
            values.add(i);

        start = Instant.now();
        subject.putAll("foo",values);
        finish = Instant.now();

        durations.put(10,Duration.between(start, finish).toNanos());

        values.clear();
        subject.clear();

        for (int i = 0; i < 1000; i++)
            values.add(i);

        start = Instant.now();
        subject.putAll("foo",values);
        finish = Instant.now();

        durations.put(1000,Duration.between(start, finish).toNanos());

        values.clear();
        subject.clear();

        for (int i = 0; i < 100000; i++)
            values.add(i);

        start = Instant.now();
        subject.putAll("foo",values);
        finish = Instant.now();

        durations.put(100000,Duration.between(start, finish).toNanos());

        values.clear();
        subject.clear();

        for (int i = 0; i < 1000000; i++)
            values.add(i);

        start = Instant.now();
        subject.putAll("foo",values);
        finish = Instant.now();

        durations.put(1000000,Duration.between(start, finish).toNanos());

        values.clear();
        subject.clear();

        for (int i = 0; i < 10000000; i++)
            values.add(i);

        start = Instant.now();
        subject.putAll("foo",values);
        finish = Instant.now();

        durations.put(10000000,Duration.between(start, finish).toNanos());

        values.clear();
        subject.clear();

        for (int i = 0; i < 100000000; i++)
            values.add(i);

        start = Instant.now();
        subject.putAll("foo",values);
        finish = Instant.now();

        durations.put(100000000,Duration.between(start, finish).toNanos());

        durations
            .entrySet()
            .stream()
            .sorted((x,y) -> x.getKey().compareTo(y.getKey()))
            .forEach(duration -> System.out.println(duration));
    }


}

//////////////////////////////////////////////////////////////////////////////
