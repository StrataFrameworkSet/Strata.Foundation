//////////////////////////////////////////////////////////////////////////////
// ICompletableBoolean.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

/**
 * <p>
 * Specialized {@link ICompletableResult} for asynchronous operations
 * that produce a Boolean outcome.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Asynchronous boolean check
 * ICompletableBoolean result =
 *     CompletableBoolean.supplyAsync(
 *         () -&gt; CompletedResult.of(true));
 *
 * Boolean value = result.join().get();
 * </pre>
 * </p>
 */
public
interface ICompletableBoolean
    extends ICompletableResult<Boolean,CompletedResult<Boolean>> {}

//////////////////////////////////////////////////////////////////////////////
