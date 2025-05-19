/// ///////////////////////////////////////////////////////////////////////////
// AbstractEventReceiverGroup.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

import java.util.*;
import java.util.stream.Stream;

public abstract
class AbstractEventReceiverGroup<
    E,
    L extends IEventListener<E>,
    R extends IEventReceiver<E,L>>
    implements IEventReceiverGroup<E,L,R>
{
    private final Set<R> receivers;

    protected
    AbstractEventReceiverGroup()
    {
        this(new HashSet<>());
    }

    protected
    AbstractEventReceiverGroup(Set<R> receivers)
    {
        this.receivers = new HashSet<>(receivers);
    }

    @Override
    public IEventReceiverGroup<E,L,R>
    insert(R receiver)
    {
        Objects.requireNonNull(receiver,"Receiver cannot be null");
        receivers.add(receiver);
        return this;
    }

    @Override
    public IEventReceiverGroup<E,L,R>
    remove(R receiver)
    {
        Objects.requireNonNull(receiver,"Receiver cannot be null");
        receivers.remove(receiver);
        return this;
    }

    @Override
    public IEventReceiverGroup<E,L,R>
    clear()
    {
        receivers.clear();
        return this;
    }

    @Override
    public Set<R>
    get()
    {
        return new HashSet<>(receivers);
    }

    @Override
    public boolean
    contains(R receiver)
    {
        return receivers.contains(receiver);
    }

    @Override
    public int
    size()
    {
        return receivers.size();
    }

    @Override
    public void
    startListening()
    {
        stream().forEach(IEventReceiver::startListening);
    }

    @Override
    public void
    stopListening()
    {
        stream().forEach(IEventReceiver::stopListening);
    }

    @Override
    public boolean
    isListening()
    {
        return stream().allMatch(IEventReceiver::isListening);
    }

    @Override
    public Stream<R>
    stream()
    {
        return receivers.stream();
    }
}

//////////////////////////////////////////////////////////////////////////////
