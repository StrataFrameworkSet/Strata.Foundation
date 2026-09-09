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
 * <p>
 * <h4>Type Parameter</h4>
 * {@code <E>} - event type
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Async send result handling
 * ICompletableSendResult&lt;String&gt; result = sender.send("event");
 * result.thenAccept(sendResult -&gt;
 *     System.out.println("Sent: " + sendResult.getEvent()));
 * </pre>
 * </p>
 */
public
interface ICompletableSendResult<E>
    extends CompletionStage<SendResult<E>> {}

//////////////////////////////////////////////////////////////////////////////
