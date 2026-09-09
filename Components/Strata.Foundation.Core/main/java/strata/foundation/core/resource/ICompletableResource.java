//////////////////////////////////////////////////////////////////////////////
// ICompletableResource.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.resource;

import strata.foundation.core.concurrent.ICompletableBoolean;
import strata.foundation.core.concurrent.ICompletableVoid;

/**
 * <p>
 * Asynchronous counterpart to {@link IResource} whose lifecycle operations
 * return completable results rather than blocking the calling thread.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * ICompletableResource resource = new AsyncFileResource(path);
 * resource.open().thenAccept(result -&gt; result.throwIfExceptionPresent());
 * </pre>
 * </p>
 */
public
interface ICompletableResource
{
    ICompletableVoid
    open();

    ICompletableVoid
    close();

    ICompletableBoolean
    isOpen();

    ICompletableBoolean
    isClosed();
}

//////////////////////////////////////////////////////////////////////////////