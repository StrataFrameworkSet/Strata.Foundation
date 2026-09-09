//////////////////////////////////////////////////////////////////////////////
// IClosable.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

/**
 * <p>
 * Defines an asynchronous close operation that returns an
 * {@link ICompletableVoid} to signal completion.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Asynchronous close
 * IClosable resource = ...;
 * resource.close().thenRun(() -&gt; System.out.println("closed"));
 * </pre>
 * </p>
 */
public
interface IClosable
{
    ICompletableVoid
    close();
}

//////////////////////////////////////////////////////////////////////////////