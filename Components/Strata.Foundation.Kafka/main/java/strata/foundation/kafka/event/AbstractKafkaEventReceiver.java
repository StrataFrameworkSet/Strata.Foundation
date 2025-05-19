//////////////////////////////////////////////////////////////////////////////
// KafkaAvroEventReceiver.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.kafka.event;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import strata.foundation.core.event.AbstractEventReceiver;
import strata.foundation.core.event.IEventListener;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract
class AbstractKafkaEventReceiver<E,L extends IEventListener<E>>
    extends AbstractEventReceiver<E,L>
{
    private final Map<String,Object> itsProperties;
    private final Class<E>           itsType;
    private final String             itsTopic;
    private Consumer<String,E>       itsConsumer;
    private final ExecutorService    itsExecutor;
    private final AtomicBoolean      itsListening;
    private final Logger             itsLogger;

    public
    AbstractKafkaEventReceiver(
        Map<String,Object> p,
        Class<E>           t,
        String             topic)
    {
        itsProperties = initializeProperties(p,t);
        itsType       = t;
        itsTopic      = topic;
        itsConsumer   = null;
        itsExecutor   = Executors.newSingleThreadExecutor();
        itsListening  = new AtomicBoolean(false);
        itsLogger     = LogManager.getLogger(getClass());
    }

    @Override
    public void
    startListening()
    {
        if (isListening())
        {
            itsLogger.warn("Already listening for events.");
            return;
        }

        if (!hasListener())
        {
            itsLogger.error("Cannot start listening for events: no listener.");
            throw
                new IllegalStateException(
                    "Cannot start listening for events: no listener.");
        }

        itsLogger.info("Started listening for events.");
        itsListening.set(true);
        itsConsumer = createConsumer();
        itsExecutor.execute(this::runListeningLoop);
     }

    @Override
    public void
    stopListening()
    {
        itsListening.set(false);

        if (itsConsumer != null)
            itsConsumer.wakeup();

        itsLogger.info("Stopped listening for events.");
    }

    @Override
    public boolean
    isListening()
    {
        return (itsListening.get()) && (itsConsumer != null);
    }

    protected KafkaConsumer<String,E>
    createConsumer()
    {
        return new KafkaConsumer<>(itsProperties);
    }

    protected void
    runListeningLoop()
    {
        try
        {
            itsLogger.debug("Entering event listening loop.");
            itsConsumer.subscribe(Collections.singletonList(itsTopic));
            getListener()
                .ifPresent(listener -> listener.onStart());

            while (itsListening.get())
            {
                ConsumerRecords<String,E> records =
                    itsConsumer.poll(Duration.ofMillis(100));

                itsLogger.info("Received {} events.",records.count());

                for (ConsumerRecord<String,E> record: records)
                {
                    try
                    {
                        getListener()
                            .ifPresent(
                                listener -> listener.onEvent(record.value()));
                    }
                    catch (Exception exception)
                    {
                        getListener()
                            .ifPresent(
                                listener -> listener.onException(exception));
                    }
                }
            }
        }
        catch (WakeupException wakeup) {}
        catch (Throwable exception)
        {
            itsLogger.error("Exception in event listening loop",exception);
        }
        finally
        {
            getListener()
                .ifPresent(listener -> listener.onStop());

            itsLogger.debug("Exiting event listening loop.");

            if (itsConsumer != null)
                itsConsumer.close();

            itsConsumer = null;
        }
    }

    private static <E> Map<String,Object>
    initializeProperties(Map<String,Object> properties,Class<E> type)
    {
        new ClassBasedGroupIdSupplier<>(type,properties)
            .get()
            .ifPresent(
                groupId ->
                    properties.put(
                        ConsumerConfig.GROUP_ID_CONFIG,
                        groupId));

        properties.put(
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
            StringDeserializer.class.getName());

        properties.put(
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,
            true);

        if (!properties.containsKey(
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG))
            throw
                new IllegalStateException(
                    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG +
                        " must be configured");

        if (usesAvroDeserializer(properties))
        {
            properties.put(
                KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG,
                true);

            if (
                !properties.containsKey(
                    KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG))
                throw
                    new IllegalStateException(
                        KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG +
                            " must be configured");
        }

        return properties;
    }

    private static boolean
    usesAvroDeserializer(Map<String,Object> properties)
    {
        return
            properties
                .get(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG)
                .toString().equals(KafkaAvroDeserializer.class.getName());
    }

}

//////////////////////////////////////////////////////////////////////////////
