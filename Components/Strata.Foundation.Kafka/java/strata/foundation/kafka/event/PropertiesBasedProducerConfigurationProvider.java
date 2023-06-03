//////////////////////////////////////////////////////////////////////////////
// PropertiesBasedTopicProvider.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.kafka.event;

import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import jakarta.inject.Inject;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public
class PropertiesBasedProducerConfigurationProvider
    implements IConfigurationProvider
{
    private final Properties itsProperties;
    private final String     itsBootstrapServersKey;
    private final String     itsSchemaRegistryUrlKey;
    private final String     itsRetriesKey;
    private final String     itsAcksKey;

    @Inject
    public
    PropertiesBasedProducerConfigurationProvider(Properties properties)
    {
        this(
            properties,
            "kafka.bootstrap.servers",
            "kafka.schema.registry.url",
            "kafka.retries",
            "kafka.acks");
    }

    public
    PropertiesBasedProducerConfigurationProvider(
        Properties properties,
        String     bootstrapServersKey,
        String     schemaRegistryUrlKey,
        String     retriesKey,
        String     acksKey)
    {
        itsProperties = properties;
        itsBootstrapServersKey = bootstrapServersKey;
        itsSchemaRegistryUrlKey = schemaRegistryUrlKey;
        itsRetriesKey = retriesKey;
        itsAcksKey = acksKey;

        if (!itsProperties.containsKey(itsBootstrapServersKey))
            throw
                new IllegalArgumentException(
                    "properties does not contain key: " + itsBootstrapServersKey);
    }

    @Override
    public Map<String,Object>
    get()
    {
        return
            new HashMap<String,Object>()
            {{
                put(
                    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                    itsProperties.getProperty(itsBootstrapServersKey));

                if (itsProperties.containsKey(itsSchemaRegistryUrlKey))
                    put(
                        KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG,
                        itsProperties.getProperty(itsSchemaRegistryUrlKey));

                put(
                    ProducerConfig.ACKS_CONFIG,
                    itsProperties.getProperty(itsAcksKey,"1"));
                put(
                    ProducerConfig.RETRIES_CONFIG,
                    itsProperties.getProperty(itsRetriesKey,"5"));
                put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG,"SSL");
            }};
    }

    protected Properties
    getProperties() { return itsProperties; }

}

//////////////////////////////////////////////////////////////////////////////
