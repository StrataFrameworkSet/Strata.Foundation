//////////////////////////////////////////////////////////////////////////////
// ChronicleEventSender.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.chronicle.event;

import strata.foundation.core.event.IEventSender;
import strata.foundation.core.event.SendResult;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptAppender;
import strata.foundation.core.utility.OptionalExtension;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public
class ChronicleEventSender<E>
    implements IEventSender<E>,ISimpleChronicleEventSender
{
    private ChronicleQueue            queue;
    private Optional<ExcerptAppender> appender;

    public
    ChronicleEventSender(ChronicleQueue q)
    {
        queue = q;
        appender = Optional.empty();

        if (queue.isClosed())
            throw new IllegalStateException("Cannot send on a closed queue");
    }

    @Override
    public IEventSender<E>
    open() throws Exception
    {
        appender = Optional.of(queue.acquireAppender());
        return this;
    }

    @Override
    public IEventSender<E>
    close() throws Exception
    {
        appender.ifPresent(a -> a.close());
        appender = Optional.empty();
        return this;
    }

    @Override
    public CompletionStage<SendResult<E>>
    send(E event)
    {
        return
            OptionalExtension
                .ifPresentOrElse(
                    appender,
                    a ->
                        CompletableFuture.supplyAsync(() -> appendToQueue(a,event)),
                    () ->
                        CompletableFuture.completedFuture(
                            new SendResult<>(
                                new IllegalStateException("sender is not open"))));
    }

    @Override
    public boolean
    isOpen()
    {
        return !isClosed();
    }

    @Override
    public boolean
    isClosed()
    {
        return
            appender
                .map(a -> a.isClosed())
                .orElse(true);
    }

    @Override
    public ChronicleQueue
    getQueue() { return queue; }

    @SuppressWarnings("unchecked")
    private SendResult<E>
    appendToQueue(ExcerptAppender a,E event)
    {
        try
        {
            a.writeDocument(w -> w.write().object(event));
            return new SendResult(event);
        }
        catch (Exception e)
        {
            return new SendResult(e);
        }
    }
}

//////////////////////////////////////////////////////////////////////////////
