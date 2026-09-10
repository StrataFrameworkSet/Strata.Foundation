//////////////////////////////////////////////////////////////////////////////
// IteratedEventListener.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

import java.util.Collection;

/**
 * <p>
 * Abstract {@link IEventListener} that iterates over a collection
 * derived from the event, invoking a handler for each element.
 * </p>
 * <br/>
 * <b>Type Parameter</b><br/>
 * {@code <E>} - event type
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Subclass that processes each item in an event's collection
 * IteratedEventListener&lt;OrderEvent&gt; listener =
 *     new IteratedEventListener&lt;&gt;()
 *     {
 *         protected Collection&lt;?&gt; getCollection(OrderEvent event)
 *         { return event.getItems(); }
 *
 *         protected void onElement(Object item)
 *         { process(item); }
 *     };
 * </pre>
 */
public abstract
class IteratedEventListener<E>
    implements IEventListener<E>
{
    @Override
    public void
    onStart() throws StartException {}

    @Override
    public void onStop() {}

    @Override
    public void
    onEvents(Collection<E> events)
    {
        events.forEach(this::doOnEvent);
    }

    protected abstract void
    onEvent(E event);

    @Override
    public abstract void
    onException(Exception exception);

    protected void
    doOnEvent(E event)
    {
        try
        {
            onEvent(event);
        }
        catch (Exception e)
        {
            onException(e);
        }
    }
}

//////////////////////////////////////////////////////////////////////////////
