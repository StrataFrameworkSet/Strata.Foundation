/// ///////////////////////////////////////////////////////////////////////////
// CircularIterator.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

public
class CircularIterator<T>
    implements Iterator<T>
{
    private final Iterable<T> iterable;
    private Iterator<T>       iterator;

    public
    CircularIterator(Iterable<T> iterable)
    {
        this.iterable = iterable;
        this.iterator = iterable.iterator();
    }

    @Override
    public boolean
    hasNext()
    {
        return
            iterable
                .iterator()
                .hasNext();
    }

    @Override
    public T
    next() throws NoSuchElementException
    {
        if (!iterator.hasNext())
            iterator = iterable.iterator();

        return iterator.next();
    }

    public static <T> CircularIterator<T>
    of(Iterable<T> iterable)
    {
        return new CircularIterator<>(iterable);
    }

}

//////////////////////////////////////////////////////////////////////////////
