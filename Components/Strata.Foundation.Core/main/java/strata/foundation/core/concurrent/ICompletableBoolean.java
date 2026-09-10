//////////////////////////////////////////////////////////////////////////////
// ICompletableBoolean.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

/**
 * <p>
 * Specialized {@link ICompletableResult} for asynchronous operations
 * that produce a Boolean outcome.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Asynchronous boolean check
 * ICompletableBoolean result =
 *     CompletableBoolean.supplyAsync(
 *         () -&gt; CompletedResult.of(true));
 *
 * Boolean value = result.join().get();
 * </pre>
 */
public
interface ICompletableBoolean
    extends ICompletableResult<Boolean,CompletedResult<Boolean>> {}

//////////////////////////////////////////////////////////////////////////////
