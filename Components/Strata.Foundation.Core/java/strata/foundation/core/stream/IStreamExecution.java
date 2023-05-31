//////////////////////////////////////////////////////////////////////////////
// IStreamExecution.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.stream;

import java.io.Serializable;
import java.util.concurrent.CompletionStage;

public
interface IStreamExecution
    extends Serializable
{
    CompletionStage<Void>
    cancel();

    CompletionStage<StreamExecutionStatus>
    getStatus();

    CompletionStage<IExecutionResult>
    getResult();
}

//////////////////////////////////////////////////////////////////////////////