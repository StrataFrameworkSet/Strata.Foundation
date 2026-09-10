//////////////////////////////////////////////////////////////////////////////
// CompletableActivity.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.resource;

import strata.foundation.core.concurrent.CompletedResult;
import strata.foundation.core.concurrent.CompletionException;

import java.util.Objects;

/**
 * <p>
 * Ensures an {@link ICompletableResource} is opened before use and closed
 * afterward, the asynchronous counterpart to {@link OpenExtent} for
 * resources whose lifecycle operations complete asynchronously.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * try (CompletableActivity&lt;MyResource&gt; activity = CompletableActivity.of(resource))
 * {
 *     // use resource once it is confirmed open
 * }
 * </pre>
 *
 * @param <R> the type of {@link ICompletableResource} being managed
 */
public
class CompletableActivity<R extends ICompletableResource>
    implements AutoCloseable
{
    private final R resource;

    public
    CompletableActivity(R r)
        throws CompletionException
    {
        Objects.requireNonNull(r,"resource must not be null");
        resource = r;

        resource
            .isClosed()
            .thenAccept(result -> openIfNeeded(result));
    }

    @Override
    public void
    close()
        throws CompletionException
    {
        resource
            .isOpen()
            .thenAccept(result -> closeIfNeeded(result));
    }

    protected void
    openIfNeeded(CompletedResult<Boolean> result)
        throws CompletionException
    {
        Boolean closed = result.get();

        if (closed)
            resource
                .open()
                .thenAccept(
                    openResult -> throwIfNeeded(openResult));
    }

    protected void
    closeIfNeeded(CompletedResult<Boolean> result)
        throws CompletionException
    {
        Boolean open = result.get();

        if (open)
            resource
                .close()
                .thenAccept(
                    openResult -> throwIfNeeded(openResult));
    }

    protected void
    throwIfNeeded(CompletedResult<Void> result)
        throws CompletionException
    {
        result.throwIfExceptionPresent();
    }

    public static <R extends ICompletableResource>
    CompletableActivity<R>
    of(R r)
    {
        return new CompletableActivity<>(r);
    }
}

//////////////////////////////////////////////////////////////////////////////
