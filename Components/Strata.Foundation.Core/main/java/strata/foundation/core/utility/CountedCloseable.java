//////////////////////////////////////////////////////////////////////////////
// CountedCloseable.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.concurrent.atomic.AtomicInteger;

public
class CountedCloseable<T extends AutoCloseable>
    implements ICopyable,AutoCloseable
{
    private final T             closeable;
    private final AtomicInteger count;

    public
    CountedCloseable(T closeable)
    {
        this.closeable = closeable;
        this.count = new AtomicInteger(1);
    }

    public
    CountedCloseable(CountedCloseable<T> other)
    {
        this.closeable = other.closeable;
        this.count = other.count;
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
        if (count.decrementAndGet() == 0)
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
