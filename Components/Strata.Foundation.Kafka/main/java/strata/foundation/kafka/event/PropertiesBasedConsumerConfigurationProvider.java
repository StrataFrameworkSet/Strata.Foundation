//////////////////////////////////////////////////////////////////////////////
// PropertiesBasedTopicProvider.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.kafka.event;

import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import jakarta.inject.Inject;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public
class PropertiesBasedConsumerConfigurationProvider
    implements IConfigurationProvider
{
    private final Properties itsProperties;
    private final String     itsBootstrapServersKey;
    private final String     itsSchemaRegistryUrlKey;
    private final String     itsConsumerGroupKey;

    @Inject
    public
    PropertiesBasedConsumerConfigurationProvider(Properties properties)
    {
        this(
            properties,
            "kafka.bootstrap.servers",
            "kafka.schema.registry.url",
            "kafka.consumer.group");
    }

    public
    PropertiesBasedConsumerConfigurationProvider(
        Properties properties,
        String     consumerGroupKey)
    {
        this(
            properties,
            "kafka.bootstrap.servers",
            "kafka.schema.registry.url",
            consumerGroupKey);
    }

    public
    PropertiesBasedConsumerConfigurationProvider(
        Properties properties,
        String     bootstrapServersKey,
        String     schemaRegistryUrlKey,
        String     consumerGroupKey)
    {
        itsProperties = properties;
        itsBootstrapServersKey = bootstrapServersKey;
        itsSchemaRegistryUrlKey = schemaRegistryUrlKey;
        itsConsumerGroupKey = consumerGroupKey;

        if (!itsProperties.containsKey(itsBootstrapServersKey))
            throw
                new IllegalArgumentException(
                    "properties does not contain key: " + itsBootstrapServersKey);

        if (!itsProperties.containsKey(itsConsumerGroupKey))
            throw
                new IllegalArgumentException(
                    "properties does not contain key: " + itsConsumerGroupKey);
    }

    @Override
    public Map<String,Object>
    get()
    {
        return
            new HashMap<String,Object>()
            {{
                put(
                    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                    itsProperties.getProperty(itsBootstrapServersKey));

                if (itsProperties.containsKey(itsSchemaRegistryUrlKey))
                    put(
                        KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG,
                        itsProperties.getProperty(itsSchemaRegistryUrlKey));

                put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG,"SSL");
                put(
                    ConsumerConfig.GROUP_ID_CONFIG,
                    itsProperties.getProperty(itsConsumerGroupKey));
            }};
    }

    protected Properties
    getProperties() { return itsProperties; }
}

//////////////////////////////////////////////////////////////////////////////
