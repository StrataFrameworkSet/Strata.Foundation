//////////////////////////////////////////////////////////////////////////////
// CountedCloseable.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public
class CountedCloseable<T extends AutoCloseable>
    implements ICopyable,AutoCloseable
{
    private final T             closeable;
    private final AtomicInteger count;
    private final AtomicBoolean closed;

    public
    CountedCloseable(T closeable)
    {
        this.closeable = Require.notNull(closeable);
        this.count = new AtomicInteger(1);
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
    public ICopyable
    copy()
    {
        return new  CountedCloseable<>(this);
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
    {
        return new CountedCloseable<T>(closeable);
    }

    public static <T extends AutoCloseable> CountedCloseable<T>
    ofCopy(CountedCloseable<T> closeable)
    {
        return (CountedCloseable<T>)closeable.copy();
    }

}

//////////////////////////////////////////////////////////////////////////////
