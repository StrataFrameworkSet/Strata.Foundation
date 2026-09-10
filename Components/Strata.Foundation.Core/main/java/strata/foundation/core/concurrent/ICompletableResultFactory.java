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
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Factory as constructor reference
 * ICompletableResultFactory&lt;String,CompletedResult&lt;String&gt;,
 *     CompletableResult&lt;String,CompletedResult&lt;String&gt;&gt;&gt; factory =
 *         CompletableResult::new;
 * </pre>
 *
 * @param <T> - result value type
 * @param <R> - completed result type
 * @param <C> - completable result type
 */
public
interface ICompletableResultFactory<
    T,
    R extends CompletedResult<T>,
    C extends ICompletableResult<T,R>>
    extends Function<CompletableFuture<R>,C> {}

//////////////////////////////////////////////////////////////////////////////
