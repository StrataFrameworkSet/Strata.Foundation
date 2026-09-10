//////////////////////////////////////////////////////////////////////////////
// ICompletableSendResult.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

import java.util.concurrent.CompletionStage;

/**
 * <p>
 * A {@link java.util.concurrent.CompletionStage} that produces a
 * {@link SendResult} indicating the outcome of an asynchronous
 * event send operation.
 * </p>
 * <br/>
 * <b>Type Parameter</b><br/>
 * {@code <E>} - event type
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Async send result handling
 * ICompletableSendResult&lt;String&gt; result = sender.send("event");
 * result.thenAccept(sendResult -&gt;
 *     System.out.println("Sent: " + sendResult.getEvent()));
 * </pre>
 */
public
interface ICompletableSendResult<E>
    extends CompletionStage<SendResult<E>> {}

//////////////////////////////////////////////////////////////////////////////
