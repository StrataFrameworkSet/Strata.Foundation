//////////////////////////////////////////////////////////////////////////////
// ICompletableVoidFactory.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

/**
 * <p>
 * Specialized {@link ICompletableResultFactory} for creating
 * {@link ICompletableVoid} instances.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Factory as constructor reference
 * ICompletableVoidFactory factory = CompletableVoid::new;
 * </pre>
 * </p>
 */
public
interface ICompletableVoidFactory
    extends
    ICompletableResultFactory<
        Void,
        CompletedResult<Void>,
        ICompletableVoid> {}

//////////////////////////////////////////////////////////////////////////////