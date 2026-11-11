//////////////////////////////////////////////////////////////////////////////
// OwnedCloseable.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public
class OwnedCloseable<T extends AutoCloseable>
    implements AutoCloseable
{
    private final T             closeable;
    private final boolean       owner;
    private final AtomicBoolean closed;

    public
    OwnedCloseable(T closeable,boolean owner)
        throws NullPointerException
    {
        this.closeable = Require.notNull(closeable);
        this.owner = owner;
        this.closed = new AtomicBoolean(false);
    }

    @Override
    public void
    close() throws Exception
    {
        if (closed.compareAndSet(false,true) && owner)
            closeable.close();
    }

    public T
    get() { return closeable; }

    public static <T extends AutoCloseable> OwnedCloseable<T>
    ofOwned(T closeable)
        throws NullPointerException
    {
        return new OwnedCloseable<T>(closeable,true);
    }

    public static <T extends AutoCloseable> OwnedCloseable<T>
    ofBorrowed(T closeable)
        throws NullPointerException
    {
        return new OwnedCloseable<T>(closeable,false);
    }
}

//////////////////////////////////////////////////////////////////////////////
