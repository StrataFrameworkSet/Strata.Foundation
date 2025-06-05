/// ///////////////////////////////////////////////////////////////////////////
// AbstractReceiver.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.util.Optional;
import java.util.function.Consumer;

public abstract
class AbstractReceiver<T,C extends Consumer<T>>
    implements IReceiver<T,C>
{
    private Optional<C> consumer;

    protected
    AbstractReceiver()
    {
        consumer = Optional.empty();
    }

    @Override
    public IReceiver<T,C>
    setConsumer(C consumer)
    {
        this.consumer = Optional.ofNullable(consumer);
        return this;
    }

    @Override
    public Optional<C>
    getConsumer()
    {
        return consumer;
    }

    @Override
    public boolean
    hasConsumer()
    {
        return consumer.isPresent();
    }

    @Override
    public void
    startConsuming(C consumer)
        throws StartFailedException
    {
        setConsumer(consumer);
        startConsuming();
    }
}

//////////////////////////////////////////////////////////////////////////////
