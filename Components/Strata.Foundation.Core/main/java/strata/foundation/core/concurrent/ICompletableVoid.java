//////////////////////////////////////////////////////////////////////////////
// ICompletableVoid.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

/**
 * <p>
 * Specialized {@link ICompletableResult} for asynchronous operations
 * that produce no return value.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Asynchronous void operation
 * ICompletableVoid result =
 *     CompletableVoid.runAsync(() -&gt; System.out.println("done"));
 *
 * result.join();
 * </pre>
 */
public
interface ICompletableVoid
    extends ICompletableResult<Void,CompletedResult<Void>> {}

//////////////////////////////////////////////////////////////////////////////
