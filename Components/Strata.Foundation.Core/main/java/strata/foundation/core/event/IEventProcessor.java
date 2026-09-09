//////////////////////////////////////////////////////////////////////////////
// IEventProcessor.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

import java.util.Collection;

/**
 * <p>
 * Processes events via {@code onEvent} and handles processing
 * failures via {@code onException}.
 * </p>
 * <p>
 * <h4>Type Parameter</h4>
 * {@code <E>} - event type
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * // Event processing with error handling
 * IEventProcessor&lt;String&gt; processor = ...;
 * processor.onEvent("event-data");
 * processor.onException(new RuntimeException("processing failed"));
 * </pre>
 * </p>
 */
public
interface IEventProcessor<E>
{
    void
    process(Collection<E> events)
        throws ProcessingException;
}

//////////////////////////////////////////////////////////////////////////////