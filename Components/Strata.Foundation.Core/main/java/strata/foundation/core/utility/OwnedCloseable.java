//////////////////////////////////////////////////////////////////////////////
// OwnedCloseable.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>
 * Wrapper around an {@link AutoCloseable} resource that records whether
 * the resource is owned or borrowed. Closing the wrapper closes the
 * underlying resource only if it is owned; a borrowed resource is left
 * open for its owner. The wrapper can be closed at most once; repeated
 * calls to {@link #close()} have no further effect.
 * </p>
 * <p>
 * <b>Querying state</b><br/>
 * {@link #isOwner()} reports whether the wrapper owns the resource, and
 * therefore whether closing the wrapper closes the resource.<br/>
 * {@link #isReferenceClosed()} reports whether {@link #close()} has been
 * called on this wrapper. It becomes {@code true} as soon as the call begins and
 * remains {@code true} even if closing an owned resource throws. It does
 * not report the state of the resource: a closed wrapper around a borrowed
 * resource leaves that resource open, and the wrapper cannot observe a
 * resource closed elsewhere, such as by its owner or through {@link #get()}.
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

    public boolean
    isOwner() { return owner; }

    public boolean
    isReferenceClosed() { return closed.get(); }

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
