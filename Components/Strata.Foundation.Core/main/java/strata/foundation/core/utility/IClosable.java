//////////////////////////////////////////////////////////////////////////////
// IClosable.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.concurrent.CompletionStage;

/**
 * <p>
 * Represents a resource that can be closed asynchronously, signaling
 * completion of the close operation via a {@link CompletionStage}.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * IClosable resource = ...;
 *
 * resource.close().thenRun(() -&gt; log("closed"));
 * </pre>
 * </p>
 */
public
interface IClosable
{
    CompletionStage<Void>
    close();
}

//////////////////////////////////////////////////////////////////////////////