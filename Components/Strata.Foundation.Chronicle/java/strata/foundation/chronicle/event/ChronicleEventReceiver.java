//////////////////////////////////////////////////////////////////////////////
// ChronicleEventReceiver.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.chronicle.event;

import strata.foundation.core.event.AbstractEventReceiver;
import strata.foundation.core.event.IEventListener;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptTailer;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public
class ChronicleEventReceiver<E,L extends IEventListener<E>>
    extends AbstractEventReceiver<E,L>
{
    private Class<E>                 eventType;
    private String                   name;
    private ChronicleQueue           queue;
    private Optional<ExcerptTailer>  tailer;
    private ExecutorService          executor;
    private AtomicBoolean            listening;

    public
    ChronicleEventReceiver(Class<E> type,String nm,ChronicleQueue q)
    {
        eventType = type;
        name = nm;
        queue = q;
        executor = Executors.newSingleThreadExecutor();
        listening = new AtomicBoolean(false);

        if (queue.isClosed())
            throw new IllegalStateException("Cannot receive on a closed queue");
    }

    @Override
    public void
    startListening()
    {
        if (isListening())
            return;

        if (!hasListener())
            throw new IllegalStateException("No listener.");

        if (queue.isClosed())
            throw new IllegalStateException("queue is closed");

        tailer = Optional.of(queue.createTailer(name));
        listening.set(true);
        executor.submit(this::runLoop);
    }

    @Override
    public void
    stopListening()
    {
        if (isListening())
        {
            listening.set(false);
            tailer.ifPresent(t -> t.close());
            tailer = Optional.empty();
        }
    }

    @Override
    public boolean
    isListening()
    {
        return listening.get();
    }

    private String
    getName() { return name; }

    public ChronicleQueue
    getQueue() { return queue; }

    private void
    runLoop()
    {
        while (listening.get())
        {
            tailer.ifPresentOrElse(
                t -> readDocument(t),
                () ->
                {
                    throw new IllegalStateException("excerpt tailer not created");
                });
        }
    }

    private void
    readDocument(ExcerptTailer t)
    {
        t.readDocument(
            reader ->
                getListener()
                    .ifPresent(
                        listener ->
                            listenToEvent(
                                listener,
                                reader
                                    .read()
                                    .object(eventType))));
    }

    private void
    listenToEvent(L listener,E event)
    {
        try
        {
            listener.onEvent(event);
        }
        catch (Exception e)
        {
            listener.onException(e);
        }
    }
}

//////////////////////////////////////////////////////////////////////////////
