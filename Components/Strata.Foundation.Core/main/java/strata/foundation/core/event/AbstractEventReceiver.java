//////////////////////////////////////////////////////////////////////////////
// AbstractEventReceiver.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

import java.util.Optional;

/**
 * <p>
 * Abstract base implementation of {@link IEventReceiver} providing
 * listener set management (attach, detach, has) while leaving
 * event delivery lifecycle (start, stop listening) to subclasses.
 * See: <a href="https://en.wikipedia.org/wiki/Observer_pattern">Observer pattern (Wikipedia)</a>
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Subclass usage
 * IEventReceiver&lt;String,IEventListener&lt;String&gt;&gt; receiver =
 *     new MyEventReceiver();
 * receiver.startListening(event -&gt; process(event));
 * </pre>
 *
 * @param <E> - event type
 * @param <L> - listener type
 */
public abstract
class AbstractEventReceiver<E,L extends IEventListener<E>>
    implements IEventReceiver<E,L>
{
    private Optional<L> itsListener;

    protected
    AbstractEventReceiver()
    {
        itsListener = Optional.empty();
    }

    @Override
    public AbstractEventReceiver<E,L>
    setListener(L listener)
    {
        itsListener = Optional.of(listener);
        return this;
    }

    @Override
    public Optional<L>
    getListener()
    {
        return itsListener;
    }

    @Override
    public boolean
    hasListener()
    {
        return itsListener.isPresent();
    }

    @Override
    public void
    startListening(L listener)
    {
        setListener(listener);
        startListening();
    }

    @Override
    public abstract void
    startListening();

    @Override
    public abstract void
    stopListening();

    @Override
    public abstract boolean
    isListening();
}

//////////////////////////////////////////////////////////////////////////////
