/// ///////////////////////////////////////////////////////////////////////////
// CircularIterator.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>
 * An {@link java.util.Iterator} that cycles endlessly over an
 * {@link Iterable}, restarting from the beginning each time
 * the underlying iterator is exhausted.
 * See: <a href="https://en.wikipedia.org/wiki/Circular_buffer">Circular buffer (Wikipedia)</a>
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * CircularIterator&lt;String&gt; iter =
 *     CircularIterator.of(List.of("a","b","c"));
 *
 * // Cycling iteration
 * iter.next(); // "a"
 * iter.next(); // "b"
 * iter.next(); // "c"
 * iter.next(); // "a" (cycles back)
 * </pre>
 *
 * @param <T> element type
 */
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
