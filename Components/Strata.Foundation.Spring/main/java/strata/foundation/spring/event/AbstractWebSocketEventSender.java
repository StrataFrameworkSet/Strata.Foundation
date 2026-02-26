//////////////////////////////////////////////////////////////////////////////
// AbstractKafkaTemplateEventSender.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;
import strata.foundation.core.event.*;
import strata.foundation.core.mapper.ObjectMapperSupplier;

import java.net.URI;

public abstract
class AbstractWebSocketEventSender<K,E>
    implements IEventSender<E>
{
    private final URI             uri;
    private final WebSocketClient client;
    private final ObjectMapper    mapper;

    protected
    AbstractWebSocketEventSender(
        URI                    u,
        WebSocketClient        c)
    {
        uri = u;
        client = c;
        mapper = new ObjectMapperSupplier().get();
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
            null;
            /*
            new CompletableSendResult<>(
                client
                    .execute(uri,session -> session.send()event)
                    .thenApply(
                        result ->
                            new SendResult<>(
                                result
                                    .getProducerRecord()
                                    .value()))
                    .exceptionally(exception -> new SendResult<>(exception)));

             */
        }

    @Override
    public boolean
    isOpen()
    {
        return client != null;
    }

    @Override
    public boolean
    isClosed()
    {
        return false;
    }
}

//////////////////////////////////////////////////////////////////////////////
