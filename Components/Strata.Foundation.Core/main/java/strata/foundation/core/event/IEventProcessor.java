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
 * <br/>
 * <b>Type Parameter</b><br/>
 * {@code <E>} - event type
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Event processing with error handling
 * IEventProcessor&lt;String&gt; processor = ...;
 * processor.onEvent("event-data");
 * processor.onException(new RuntimeException("processing failed"));
 * </pre>
 */
public
interface IEventProcessor<E>
{
    void
    process(Collection<E> events)
        throws ProcessingException;
}

//////////////////////////////////////////////////////////////////////////////
