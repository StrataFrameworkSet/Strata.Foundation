//////////////////////////////////////////////////////////////////////////////
// ICompletableVoid.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

/**
 * <p>
 * Specialized {@link ICompletableResult} for asynchronous operations
 * that produce no return value.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Asynchronous void operation
 * ICompletableVoid result =
 *     CompletableVoid.runAsync(() -&gt; System.out.println("done"));
 *
 * result.join();
 * </pre>
 * </p>
 */
public
interface ICompletableVoid
    extends ICompletableResult<Void,CompletedResult<Void>> {}

//////////////////////////////////////////////////////////////////////////////
