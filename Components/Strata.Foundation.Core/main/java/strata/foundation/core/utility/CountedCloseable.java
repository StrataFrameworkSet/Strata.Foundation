//////////////////////////////////////////////////////////////////////////////
// CountedCloseable.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * Reference-counted wrapper around an {@link AutoCloseable} resource.
 * Each copy shares the underlying resource and increments its reference
 * count; closing a copy decrements the count, and the underlying resource
 * is closed only when the last copy is closed. Each instance can be closed
 * at most once.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * CountedCloseable&lt;Connection&gt; first = CountedCloseable.of(connection);
 * CountedCloseable&lt;Connection&gt; second = CountedCloseable.ofCopy(first);
 *
 * // Usage
 * Connection shared = second.get();
 *
 * // Closing (connection is closed after both are closed)
 * first.close();
 * second.close();
 * </pre>
 *
 * @param <T> type of wrapped closeable resource
 */
public
class CountedCloseable<T extends AutoCloseable>
    implements ICopyable,AutoCloseable
{
    private final T             closeable;
    private final AtomicInteger count;
    private final AtomicBoolean closed;

    public
    CountedCloseable(T closeable)
        throws NullPointerException
    {
        this(closeable,true);
    }

    public
    CountedCloseable(T closeable,boolean initializeCount)
        throws NullPointerException
    {
        this.closeable = Require.notNull(closeable);
        this.count = new AtomicInteger(initializeCount ? 1 : 0);
        this.closed = new AtomicBoolean(false);
    }

    public
    CountedCloseable(CountedCloseable<T> other)
    {
        this.closeable = other.closeable;
        this.count = other.count;
        this.closed = new AtomicBoolean(false);
        this.count.incrementAndGet();
    }

    @Override
    public CountedCloseable<T>
    copy()
    {
        return new CountedCloseable<>(this);
    }

    @Override
    public void
    close() throws Exception
    {
        if (closed.compareAndSet(false,true) && count.decrementAndGet() == 0)
            closeable.close();
    }

    public T
    get() { return closeable; }

    public static <T extends AutoCloseable> CountedCloseable<T>
    of(T closeable)
        throws NullPointerException
    {
        return new CountedCloseable<T>(closeable);
    }

    public static <T extends AutoCloseable> CountedCloseable<T>
    ofZero(T closeable)
        throws NullPointerException
    {
        return new CountedCloseable<T>(closeable,false);
    }

    public static <T extends AutoCloseable> CountedCloseable<T>
    ofCopy(CountedCloseable<T> closeable)
    {
        return (CountedCloseable<T>)closeable.copy();
    }

}

//////////////////////////////////////////////////////////////////////////////
