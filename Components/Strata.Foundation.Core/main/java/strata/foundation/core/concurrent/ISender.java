//////////////////////////////////////////////////////////////////////////////
// ISender.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import strata.foundation.core.event.SendResult;

/**
 * <p>
 * Message sender that delivers elements and returns a
 * {@link SendResult} indicating the outcome.
 * See: <a href="https://en.wikipedia.org/wiki/Producer%E2%80%93consumer_problem">Producer-consumer problem (Wikipedia)</a>
 * </p>
 * <p>
 * <h4>Type Parameter</h4>
 * {@code <T>} - message type
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Sending a message
 * ISender&lt;String&gt; sender = new BlockingQueueSender&lt;&gt;(queue);
 * SendResult&lt;String&gt; result = sender.send("hello");
 * </pre>
 * </p>
 */
public
interface ISender<T>
{
    SendResult<T>
    send(T element);
}

//////////////////////////////////////////////////////////////////////////////