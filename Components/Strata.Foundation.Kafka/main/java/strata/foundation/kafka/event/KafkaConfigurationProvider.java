/// ///////////////////////////////////////////////////////////////////////////
// KafkaConfigurationProvider.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.kafka.event;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import jakarta.inject.Inject;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import strata.foundation.core.configuration.IConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public
class KafkaConfigurationProvider
    implements IKafkaConfigurationProvider
{
    public static final String CONFIGURATION_KIND_KEY = "KAFKA_CONFIGURATION_KIND";
    public static final String BOOTSTRAP_SERVERS_KEY = "kafka.bootstrap.servers";
    public static final String SCHEMA_REGISTRY_URL_KEY = "kafka.schema.registry.url";
    public static final String SECURITY_PROTOCOL_KEY = "kafka.security.protocol";
    public static final String API_KEY_KEY = "kafka.api.key";
    public static final String API_SECRET_KEY = "kafka.api.secret";
    public static final String SCHEMA_REGISTRY_API_KEY = "kafka.schema.registry.api.key";
    public static final String SCHEMA_REGISTRY_API_SECRET = "kafka.schema.registry.api.secret";
    public static final String KEY_SERIALIZER_KEY = "kafka.key.serializer";
    public static final String VALUE_SERIALIZER_KEY = "kafka.value.serializer";
    public static final String KEY_DESERIALIZER_KEY = "kafka.key.deserializer";
    public static final String VALUE_DESERIALIZER_KEY = "kafka.value.deserializer";
    public static final String RETRIES_KEY = "kafka.retries";
    public static final String ACKS_KEY = "kafka.acks";
    public static final String CLIENT_ID_KEY = "kafka.client.id";
    public static final String GROUP_ID_KEY = "kafka.group.id";

    private final IConfiguration     source;
    private final Map<String,Object> configuration;

    @Inject
    public
    KafkaConfigurationProvider(IConfiguration source)
    {
        this(source,KafkaConfigurationKind.EVENT_SENDER_AND_EVENT_RECEIVER);
    }

    public
    KafkaConfigurationProvider(
        IConfiguration         source,
        KafkaConfigurationKind kind)
    {
        this.source = source;
        this.configuration = new HashMap<>();

        initializeCommon(kind);

        switch(kind)
        {
            case EVENT_SENDER:
                initializeSender();
                break;
            case EVENT_RECEIVER:
                initializeReceiver();
                break;
            case EVENT_SENDER_AND_EVENT_RECEIVER:
                initializeSender();
                initializeReceiver();
                break;
            default:
                break;
        }
    }

    @Override
    public Map<String,Object>
    get()
    {
        return new HashMap<>(configuration);
    }

    protected void
    initializeCommon(KafkaConfigurationKind kind)
    {
        configuration.put(
            CONFIGURATION_KIND_KEY,
            kind.name());

        validateRequired();

        configuration.put(
            CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG,
            source.getProperty(BOOTSTRAP_SERVERS_KEY));
        configuration.put(
            KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG,
            source.getProperty(SCHEMA_REGISTRY_URL_KEY));

        configuration.put(
            CommonClientConfigs.SECURITY_PROTOCOL_CONFIG,
            source.getProperty(
                SECURITY_PROTOCOL_KEY,
                "SASL_SSL"));
        configuration.put(
            SaslConfigs.SASL_MECHANISM,
            "PLAIN");
        configuration.put(
            SaslConfigs.SASL_JAAS_CONFIG,
            new StringBuilder()
                .append("org.apache.kafka.common.security.plain.PlainLoginModule required ")
                .append("username='")
                .append(source.getProperty(API_KEY_KEY))
                .append("' ")
                .append("password='")
                .append(source.getProperty(API_SECRET_KEY))
                .append("';")
                .toString());
        configuration.put(
            AbstractKafkaAvroSerDeConfig.BASIC_AUTH_CREDENTIALS_SOURCE,
            "USER_INFO");
        configuration.put(
            AbstractKafkaAvroSerDeConfig.USER_INFO_CONFIG,
            new StringBuilder()
                .append(source.getProperty(SCHEMA_REGISTRY_API_KEY))
                .append(":")
                .append(source.getProperty(SCHEMA_REGISTRY_API_SECRET))
                .toString());
        configuration.put(
            CommonClientConfigs.CLIENT_ID_CONFIG,
            source.getProperty(
                CLIENT_ID_KEY,
                "strata-client-" + UUID.randomUUID().toString()));
    }

    protected void
    initializeSender()
        throws IllegalArgumentException
    {

        configuration.put(
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
            source.getProperty(
                KEY_SERIALIZER_KEY,
                "org.apache.kafka.common.serialization.StringSerializer"));
        configuration.put(
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
            source.getProperty(
                VALUE_SERIALIZER_KEY,
                "io.confluent.kafka.serializers.KafkaAvroSerializer"));
        configuration.put(
            ProducerConfig.ACKS_CONFIG,
            source.getProperty(ACKS_KEY,"all"));
        configuration.put(
            ProducerConfig.RETRIES_CONFIG,
            source.getProperty(RETRIES_KEY,"5"));
    }

    protected void
    initializeReceiver()
        throws IllegalArgumentException
    {
        if (source.hasProperty(GROUP_ID_KEY))
            configuration.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                source.getProperty(GROUP_ID_KEY));

        configuration.put(
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
            source.getProperty(
                KEY_DESERIALIZER_KEY,
                "org.apache.kafka.common.serialization.StringDeserializer"));
        configuration.put(
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            source.getProperty(
                VALUE_DESERIALIZER_KEY,
                "io.confluent.kafka.serializers.KafkaAvroDeserializer"));
    }


    protected void
    validateRequired()
        throws IllegalArgumentException
    {
        if (!source.hasProperty(BOOTSTRAP_SERVERS_KEY))
            throw
                new IllegalArgumentException(
                    "properties does not contain key: " + BOOTSTRAP_SERVERS_KEY);

        if (!source.hasProperty(API_KEY_KEY))
            throw
                new IllegalArgumentException(
                    "properties does not contain key: " + API_KEY_KEY);

        if (!source.hasProperty(API_SECRET_KEY))
            throw
                new IllegalArgumentException(
                    "properties does not contain key: " + API_SECRET_KEY);

        if (!source.hasProperty(SCHEMA_REGISTRY_API_KEY))
            throw
                new IllegalArgumentException(
                    "properties does not contain key: " + SCHEMA_REGISTRY_API_KEY);

        if (!source.hasProperty(SCHEMA_REGISTRY_API_SECRET))
            throw
                new IllegalArgumentException(
                    "properties does not contain key: " + SCHEMA_REGISTRY_API_SECRET);
    }
}

//////////////////////////////////////////////////////////////////////////////
