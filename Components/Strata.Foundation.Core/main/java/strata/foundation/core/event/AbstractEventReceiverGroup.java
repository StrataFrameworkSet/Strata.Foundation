//////////////////////////////////////////////////////////////////////////////
// AbstractEventReceiverGroup.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

import java.util.*;
import java.util.stream.Stream;

/**
 * <p>
 * Abstract base implementation of {@link IEventReceiverGroup} providing
 * receiver set management while leaving group coordination to subclasses.
 * </p>
 * <br/>
 * <b>Type Parameters</b><br/>
 * <ul>
 * <li>{@code <E>} - event type</li>
 * <li>{@code <L>} - listener type</li>
 * <li>{@code <R>} - receiver type</li>
 * </ul>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Access grouped receivers from a subclass
 * IEventReceiverGroup&lt;String,IEventListener&lt;String&gt;,
 *     IEventReceiver&lt;String,IEventListener&lt;String&gt;&gt;&gt; group = ...;
 * Set&lt;?&gt; receivers = group.get();
 * </pre>
 */
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
