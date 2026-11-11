//////////////////////////////////////////////////////////////////////////////
// CountedCloseable.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * Reference-counted wrapper around an {@link AutoCloseable} resource.
 * All instances that wrap the same resource share a single reference
 * count, and the underlying resource is closed exactly once, when that
 * count drops to zero. Each instance can be closed at most once; repeated
 * calls to {@link #close()} on the same instance have no further effect.
 * </p>
 * <p>
 * <b>Counted instances</b><br/>
 * A counted instance, created with {@link #ofCounted(AutoCloseable)},
 * holds one reference to the resource. The creator owns that reference
 * and must close the instance to release it. Every copy, created with
 * {@link #copy()} or {@link #ofCopy(CountedCloseable)}, is also counted
 * and holds its own reference. The resource is closed when the creator
 * and all copies have been closed, in any order.
 * </p>
 * <p>
 * <b>Uncounted instances</b><br/>
 * An uncounted instance, created with {@link #ofUncounted(AutoCloseable)},
 * holds no reference. It lets a creator open a resource and then hand
 * responsibility for its lifetime to the instances it copies: each copy
 * is counted, and the resource is closed when the last copy is closed,
 * without the creator taking part. Closing the uncounted instance only
 * stops it from producing further copies; if no copies are open at that
 * moment, the resource is closed immediately.
 * </p>
 * <p>
 * Once the resource is closed, copying any instance throws
 * {@link IllegalStateException}, as does copying an instance that is
 * itself closed. In particular, after every copy of an uncounted instance
 * has been closed, the resource is closed and the uncounted instance can
 * no longer be copied.
 * </p>
 * <p>
 * <b>Querying state</b><br/>
 * {@link #isReferenceClosed()} reports whether {@link #close()} has been
 * called on this instance. It becomes {@code true} as soon as the call
 * begins and remains {@code true} even if closing the resource throws; it
 * says nothing about the shared resource.<br/>
 * {@link #isResourceClosed()} reports whether the shared resource has been
 * closed, and returns the same value from every instance that wraps it. A
 * {@code true} result is permanent. A {@code false} result may be stale as
 * soon as it is returned, so callers that want a copy should call
 * {@link #copy()} and handle {@link IllegalStateException} rather than
 * checking first. The result becomes {@code true} just before the
 * resource's own {@code close()} is invoked, so another thread may observe
 * {@code true} while that call is still in progress.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Counted: the creator holds a reference and must close it
 * CountedCloseable&lt;Connection&gt; first = CountedCloseable.ofCounted(connection);
 * CountedCloseable&lt;Connection&gt; second = CountedCloseable.ofCopy(first);
 *
 * Connection shared = second.get();
 *
 * first.close();
 * second.close();  // connection is closed here
 *
 * // Uncounted: the creator hands responsibility for the count to its copies
 * CountedCloseable&lt;Connection&gt; source = CountedCloseable.ofUncounted(connection);
 *
 * reader.attach(source.copy());
 * writer.attach(source.copy());
 * source.close();  // creator is done; connection stays open
 *
 * // later, inside reader and writer
 * readerHandle.close();
 * writerHandle.close();  // connection is closed here
 * </pre>
 *
 * @param <T> type of wrapped closeable resource
 */
public
class CountedCloseable<T extends AutoCloseable>
    implements ICopyable,AutoCloseable
{
    private final T             closeable;
    private final boolean       counted;
    private final AtomicInteger count;
    private final AtomicBoolean closed;

    public
    CountedCloseable(T closeable)
        throws NullPointerException
    {
        this(closeable,true);
    }

    public
    CountedCloseable(T closeable,boolean counted)
        throws NullPointerException
    {
        this.closeable = Require.notNull(closeable);
        this.counted = counted;
        this.count = new AtomicInteger(counted ? 1 : 0);
        this.closed = new AtomicBoolean(false);
    }

    public
    CountedCloseable(CountedCloseable<T> other)
        throws IllegalStateException
    {
        Require.isFalse(
            other.isReferenceClosed(),
            () -> new IllegalStateException("source is already closed"));
        this.closeable = other.closeable;
        this.counted = true;
        this.count = other.count;
        this.closed = new AtomicBoolean(false);
        acquire();
    }

    @Override
    public CountedCloseable<T>
    copy()
        throws IllegalStateException
    {
        return new CountedCloseable<>(this);
    }

    @Override
    public void
    close() throws Exception
    {
        if (closed.compareAndSet(false,true) && decrementIfCounted() == 0 && count.compareAndSet(0,-1))
            closeable.close();
    }

    public T
    get() { return closeable; }

    public boolean
    isCounted() { return counted; }

    public boolean
    isReferenceClosed() { return closed.get(); }

    public boolean
    isResourceClosed() { return count.get() < 0; }

    public static <T extends AutoCloseable> CountedCloseable<T>
    ofCounted(T closeable)
        throws NullPointerException
    {
        return new CountedCloseable<T>(closeable);
    }

    public static <T extends AutoCloseable> CountedCloseable<T>
    ofUncounted(T closeable)
        throws NullPointerException
    {
        return new CountedCloseable<T>(closeable,false);
    }

    public static <T extends AutoCloseable> CountedCloseable<T>
    ofCopy(CountedCloseable<T> closeable)
        throws IllegalStateException
    {
        return closeable.copy();
    }

    private void
    acquire()
        throws IllegalStateException
    {
        int current;

        do
        {
            current = count.get();
            Require.isFalse(
                current < 0,
                () -> new IllegalStateException("resource is already closed"));
        }
        while (!count.compareAndSet(current,current + 1));
    }

    private int
    decrementIfCounted()
    {
        return
            counted
                ? count.decrementAndGet()
                : count.get();
    }
}

//////////////////////////////////////////////////////////////////////////////
