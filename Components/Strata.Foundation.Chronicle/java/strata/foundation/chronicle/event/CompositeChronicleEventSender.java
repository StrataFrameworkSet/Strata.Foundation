//////////////////////////////////////////////////////////////////////////////
// PartitionedChronicleEventSender.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.chronicle.event;

import strata.foundation.core.event.IEventSender;
import net.openhft.chronicle.queue.ChronicleQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract
class CompositeChronicleEventSender<E,S extends IEventSender<E>>
    implements IEventSender<E>,ICompositeChronicleEventSender
{
    private final List<S> senders;

    protected
    CompositeChronicleEventSender(Set<S> sndrs)
    {
        validateSenders(sndrs.stream());
        senders = new ArrayList<>(sndrs);
    }

    @Override
    public IEventSender<E>
    open() throws Exception
    {
        forEachSender(sender -> sender.open());
        return this;
    }

    @Override
    public IEventSender<E>
    close() throws Exception
    {
        forEachSender(sender -> sender.close());
        return this;
    }

    @Override
    public boolean
    isOpen()
    {
        return
            mapSenders(sender -> sender.isOpen())
                .allMatch(result -> result == true);
    }

    @Override
    public boolean
    isClosed()
    {
        return
            mapSenders(sender -> sender.isClosed())
                .allMatch(result -> result == true);
    }

    @Override
    public Stream<ChronicleQueue>
    getQueues()
    {
        return
            Stream.concat(
                senders
                    .stream()
                    .filter(sender -> sender instanceof ISimpleChronicleEventSender)
                    .map(sender -> (ISimpleChronicleEventSender)sender)
                    .map(sender -> sender.getQueue()),
                senders
                    .stream()
                    .filter(sender -> sender instanceof ICompositeChronicleEventSender)
                    .map(sender -> (ICompositeChronicleEventSender)sender)
                    .flatMap(sender -> sender.getQueues()));
    }

    protected void
    validateSenders(Stream<S> sndrs)
    {
        if (!sndrs.allMatch(sender -> isChronicleType(sender)))
            throw new IllegalArgumentException(
                "only senders derived from ChronicleEventSender, CompositeChronicleEventSender are allowed");
    }

    protected boolean
    isChronicleType(S sender)
    {
        return
            sender instanceof ISimpleChronicleEventSender ||
            sender instanceof ICompositeChronicleEventSender;
    }

    protected List<S>
    getSenders() { return senders; }

    protected void
    forEachSender(IAction<S> action) throws RuntimeException
    {
        CompositeException exception = new CompositeException();

        senders
            .stream()
            .forEach(
                sender ->
                {
                    try { action.performOn(sender); }
                    catch (Exception e) { exception.setCause(e); }
                });

        if (exception.hasCauses())
            throw exception;
    }

    protected <R> Stream<R>
    mapSenders(Function<S,R> action)
    {
        return
            senders
                .stream()
                .map(sender -> action.apply(sender));
    }
}

//////////////////////////////////////////////////////////////////////////////
