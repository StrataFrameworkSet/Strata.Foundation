//////////////////////////////////////////////////////////////////////////////
// ICompletableResult.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.util.concurrent.CompletionStage;

/**
 * <p>
 * A {@link java.util.concurrent.CompletionStage} that produces a
 * {@link CompletedResult} containing either a value or an exception,
 * enabling type-safe asynchronous result handling.
 * </p>
 * <p>
 * <h4>Type Parameters</h4>
 * <ul>
 * <li>{@code <T>} - result value type</li>
 * <li>{@code <R>} - completed result type</li>
 * </ul>
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Asynchronous execution
 * ICompletableResult&lt;String,CompletedResult&lt;String&gt;&gt; result =
 *     CompletableResult.supplyAsync(
 *         CompletableResult::new,
 *         () -&gt; CompletedResult.of("done"));
 *
 * // Accessing the result
 * CompletedResult&lt;String&gt; completed = result.join();
 * String value = completed.get();
 * </pre>
 * </p>
 */
public
interface ICompletableResult<T,R extends CompletedResult<T>>
    extends CompletionStage<R>
{
    R
    join();
}

//////////////////////////////////////////////////////////////////////////////