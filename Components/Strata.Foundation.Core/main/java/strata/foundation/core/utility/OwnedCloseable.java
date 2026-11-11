//////////////////////////////////////////////////////////////////////////////
// OwnedCloseable.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>
 * Wrapper around an {@link AutoCloseable} resource that records whether
 * the resource is owned or borrowed. Closing the wrapper closes the
 * underlying resource only if it is owned; a borrowed resource is left
 * open for its owner. The wrapper can be closed at most once.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * OwnedCloseable&lt;Connection&gt; owned = OwnedCloseable.ofOwned(connection);
 * OwnedCloseable&lt;Connection&gt; borrowed = OwnedCloseable.ofBorrowed(connection);
 *
 * // Usage
 * try (OwnedCloseable&lt;Connection&gt; resource = borrowed)
 * {
 *     resource.get().createStatement();
 * }
 * </pre>
 *
 * @param <T> type of wrapped closeable resource
 */
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
