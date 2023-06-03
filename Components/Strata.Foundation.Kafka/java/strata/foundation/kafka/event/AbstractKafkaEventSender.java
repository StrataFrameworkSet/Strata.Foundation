//////////////////////////////////////////////////////////////////////////////
// KafkaAvroEventSender.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.kafka.event;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import strata.foundation.core.action.IActionQueue;
import strata.foundation.core.event.CompletableSendResult;
import strata.foundation.core.event.ICompletableSendResult;
import strata.foundation.core.event.IEventSender;
import strata.foundation.core.event.SendResult;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public abstract
class AbstractKafkaEventSender<E>
    implements IEventSender<E>
{
    private final Map<String,Object> itsProperties;
    private final IActionQueue       itsQueue;
    private final IKeyProvider<E>    itsProvider;
    private final String             itsTopic;
    private final Class<E>           itsType;
    private Producer<String,E>       itsProducer;

    protected
    AbstractKafkaEventSender(
        Map<String,Object> properties,
        IActionQueue       queue,
        IKeyProvider<E>    provider,
        Class<E>           type,
        String             topic)
    {
        itsProperties = initializeProperties(properties);
        itsQueue = queue;
        itsProvider = provider;
        itsTopic = topic;
        itsQueue.register(() -> open(),() -> close());
        itsType = type;
        itsProducer = null;
    }

    @Override
    public AbstractKafkaEventSender<E>
    open()
    {
        if (!isOpen())
        {
            itsProducer = createProducer();
        }

        return this;
    }

    @Override
    public AbstractKafkaEventSender<E>
    close()
    {
        if (isOpen())
        {
            itsProducer.flush();
            itsProducer.close();
            itsProducer = null;
        }

        return this;
    }

    @Override
    public boolean
    isOpen()
    {
        return itsProducer != null;
    }

    @Override
    public ICompletableSendResult<E>
    send(E event)
    {
        return
            toCompletableSendResult(
                getProducer()
                    .send(
                        createRecord(itsTopic,getKey(event),event),
                        (result,exception) ->
                        {
                            if (result == null)
                                exception.printStackTrace();
                        }),
                event);
    }

    protected Producer<String,E>
    getProducer()
    {
        if (!isOpen())
            throw new IllegalStateException("sender not open");

        return itsProducer;
    }

    protected String
    getKey(E event)
    {
        try
        {
            return itsProvider.getKey(event);
        }
        catch (Throwable t)
        {
            return null;
        }
    }
    protected ProducerRecord<String,E>
    createRecord(String topic,String key,E payload)
    {
        return new ProducerRecord<>(itsTopic,key,payload);
    }

    protected Producer<String,E>
    createProducer()
    {
        return new KafkaProducer<>(itsProperties);
    }

    private static Map<String,Object>
    initializeProperties(Map<String,Object> properties)
    {
        properties.put(
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
            StringSerializer.class.getName());

        if (!properties.containsKey(
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG))
            throw
                new IllegalStateException(
                    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG +
                        " must be configured");

        if (usesAvroSerializer(properties))
            if (!properties.containsKey(
                KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG))
                throw
                    new IllegalStateException(
                        KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG +
                            " must be configured");

        return properties;
    }

    private static boolean
    usesAvroSerializer(Map<String,Object> properties)
    {
        return
            properties
                .get(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG)
                .toString().equals(KafkaAvroSerializer.class.toString());
    }

    private ICompletableSendResult<E>
    toCompletableSendResult(Future<RecordMetadata> future,E event)
    {
        return
            new CompletableSendResult<>(
                CompletableFuture
                    .supplyAsync(
                        () ->
                            {
                                try {return future.get();}
                                catch (Throwable e) {throw new CompletionException(e);}
                            })
                    .thenApply(
                        ignore -> new SendResult<>(event)));
    }
}

//////////////////////////////////////////////////////////////////////////////
