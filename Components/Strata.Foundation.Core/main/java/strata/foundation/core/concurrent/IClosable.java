//////////////////////////////////////////////////////////////////////////////
// IClosable.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

/**
 * <p>
 * Defines an asynchronous close operation that returns an
 * {@link ICompletableVoid} to signal completion.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Asynchronous close
 * IClosable resource = ...;
 * resource.close().thenRun(() -&gt; System.out.println("closed"));
 * </pre>
 */
public
interface IClosable
{
    ICompletableVoid
    close();
}

//////////////////////////////////////////////////////////////////////////////
