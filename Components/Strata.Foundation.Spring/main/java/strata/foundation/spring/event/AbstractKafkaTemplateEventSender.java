//////////////////////////////////////////////////////////////////////////////
// AbstractKafkaTemplateEventSender.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.event;

import org.springframework.kafka.core.KafkaTemplate;
import strata.foundation.core.event.*;

public abstract
class AbstractKafkaTemplateEventSender<K,E>
    implements IEventSender<E>
{
    private final String                 topic;
    private final KafkaTemplate<K,E>     sender;
    private final IEventKeySelector<K,E> keySelector;

    protected
    AbstractKafkaTemplateEventSender(
        String                 t,
        KafkaTemplate<K,E>     s,
        IEventKeySelector<K,E> k)
    {
        topic = t;
        sender = s;
        keySelector = k;
    }

    @Override
    public IEventSender<E>
    open()
    {
        return this;
    }

    @Override
    public IEventSender<E>
    close()
    {
        return this;
    }

    @Override
    public ICompletableSendResult<E>
    send(E event)
    {
        return
            new CompletableSendResult<>(
                sender
                    .send(topic,keySelector.get(event),event)
                    .thenApply(
                        result ->
                            new SendResult<>(
                                result
                                    .getProducerRecord()
                                    .value()))
                    .exceptionally(exception -> new SendResult<>(exception)));
        }

    @Override
    public boolean
    isOpen()
    {
        return sender != null;
    }

    @Override
    public boolean
    isClosed()
    {
        return false;
    }
}

//////////////////////////////////////////////////////////////////////////////
