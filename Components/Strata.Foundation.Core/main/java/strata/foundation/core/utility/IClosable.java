//////////////////////////////////////////////////////////////////////////////
// IClosable.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.util.concurrent.CompletionStage;

/**
 * <p>
 * Represents a resource that can be closed asynchronously, signaling
 * completion of the close operation via a {@link CompletionStage}.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * IClosable resource = ...;
 *
 * resource.close().thenRun(() -&gt; log("closed"));
 * </pre>
 */
public
interface IClosable
{
    CompletionStage<Void>
    close();
}

//////////////////////////////////////////////////////////////////////////////
