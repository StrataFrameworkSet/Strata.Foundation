//////////////////////////////////////////////////////////////////////////////
// IReceiver.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * <p>
 * Consumer-based message receiver that manages a consumer lifecycle
 * for processing incoming messages asynchronously.
 * See: <a href="https://en.wikipedia.org/wiki/Producer%E2%80%93consumer_problem">Producer-consumer problem (Wikipedia)</a>
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Setup and start consuming
 * IReceiver&lt;String,Consumer&lt;String&gt;&gt; receiver = ...;
 * receiver.startConsuming(msg -&gt; System.out.println(msg));
 *
 * // Stop consuming
 * receiver.stopConsuming();
 * </pre>
 *
 * @param <T> - message type
 * @param <C> - consumer type
 */
public
interface IReceiver<T,C extends Consumer<T>>
{
    IReceiver<T,C>
    setConsumer(C consumer);

    Optional<C>
    getConsumer();

    boolean
    hasConsumer();

    void
    startConsuming(C consumer) throws StartFailedException;

    void
    startConsuming() throws StartFailedException;

    void
    stopConsuming();

    boolean
    isConsuming();
}

//////////////////////////////////////////////////////////////////////////////
