//////////////////////////////////////////////////////////////////////////////
// ICompletableVoidFactory.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

/**
 * <p>
 * Specialized {@link ICompletableResultFactory} for creating
 * {@link ICompletableVoid} instances.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Factory as constructor reference
 * ICompletableVoidFactory factory = CompletableVoid::new;
 * </pre>
 */
public
interface ICompletableVoidFactory
    extends
    ICompletableResultFactory<
        Void,
        CompletedResult<Void>,
        ICompletableVoid> {}

//////////////////////////////////////////////////////////////////////////////
