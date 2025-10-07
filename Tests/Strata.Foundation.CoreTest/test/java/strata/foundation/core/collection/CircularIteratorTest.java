/// ///////////////////////////////////////////////////////////////////////////
// CircularIteratorTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

import static org.junit.jupiter.api.Assertions.*;

@Tag("CommitStage")
public
class CircularIteratorTest
{
    private List<String> list;

    @BeforeEach
    public void
    setUp()
    {
        list = List.of("A","B","C");
    }

    @Test
    public void
    testNext()
    {
        CircularIterator<String> iterator = CircularIterator.of(list);

        for (int i = 0; i < 10; ++i)
        {
            System.out.println(iterator.next());
            assertTrue(iterator.hasNext());
        }
    }

    @Test
    public void
    testNextMutable()
    {
        Deque<String>            mutable = new ConcurrentLinkedDeque<>(list);
        CircularIterator<String> iterator = CircularIterator.of(mutable);

        for (int i = 0; i < 10; ++i)
        {
            if (i == 5)
                mutable.add("D");

            System.out.println(iterator.next());
            assertTrue(iterator.hasNext());
        }
    }

    @Test
    public void
    testNextOnEmpty()
    {
        CircularIterator<String> iterator = CircularIterator.of(List.of());

        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, () -> iterator.next());

        for (int i = 0; i < 10; ++i)
        {
            assertThrows(NoSuchElementException.class,() -> iterator.next());
            assertFalse(iterator.hasNext());
        }
    }

}

//////////////////////////////////////////////////////////////////////////////
