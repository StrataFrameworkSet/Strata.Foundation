//////////////////////////////////////////////////////////////////////////////
// ICompletableResultFactory.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * <p>
 * Functional factory for creating {@link ICompletableResult} instances
 * from a {@link java.util.concurrent.CompletableFuture}, enabling
 * generic construction of typed completable results.
 * </p>
 * <p>
 * <h4>Type Parameters</h4>
 * <ul>
 * <li>{@code <T>} - result value type</li>
 * <li>{@code <R>} - completed result type</li>
 * <li>{@code <C>} - completable result type</li>
 * </ul>
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Factory as constructor reference
 * ICompletableResultFactory&lt;String,CompletedResult&lt;String&gt;,
 *     CompletableResult&lt;String,CompletedResult&lt;String&gt;&gt;&gt; factory =
 *         CompletableResult::new;
 * </pre>
 * </p>
 */
public
interface ICompletableResultFactory<
    T,
    R extends CompletedResult<T>,
    C extends ICompletableResult<T,R>>
    extends Function<CompletableFuture<R>,C> {}

//////////////////////////////////////////////////////////////////////////////
