//////////////////////////////////////////////////////////////////////////////
// CompletableResourceManager.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.resource;

import strata.foundation.core.concurrent.CompletedResult;

import java.util.Objects;

public
class CompletableResourceManager<R extends ICompletableResource>
    implements AutoCloseable
{
    private final R resource;

    public
    CompletableResourceManager(R r)
    {
        Objects.requireNonNull(r, "resource must not be null");
        resource = r;

        resource
            .isClosed()
            .thenAccept(result -> processIsClosed(result));
    }


    @Override
    public void
    close()
        throws Exception
    {
        resource
            .isOpen()
            .thenAccept(result -> processIsOpen(result));
    }

    protected void
    processIsClosed(CompletedResult<Boolean> result)
    {
        result
            .ifValuePresent(
                closed ->
                    {
                        if (closed)
                            resource
                                .open()
                                .thenAccept(
                                    openResult -> processCommand(openResult));

                    });
    }

    protected void
    processIsOpen(CompletedResult<Boolean> result)
    {
        result
            .ifValuePresent(
                open ->
                    {
                        if (open)
                            resource
                                .close()
                                .thenAccept(
                                    openResult -> processCommand(openResult));

                    });
    }

    protected void
    processCommand(CompletedResult<Void> result)
    {
        result.throwIfExceptionPresent();
    }

    public static <R extends ICompletableResource>
    CompletableResourceManager<R>
    of(R r)
    {
        return new CompletableResourceManager<>(r);
    }
}

//////////////////////////////////////////////////////////////////////////////
