//////////////////////////////////////////////////////////////////////////////
// AbstractEventReceiver.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

import java.util.Optional;

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
