//////////////////////////////////////////////////////////////////////////////
// ConfluentProducerConfigurationProvider.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.kafka.event;

import io.confluent.kafka.schemaregistry.client.SchemaRegistryClientConfig;
import jakarta.inject.Inject;

import java.util.Map;
import java.util.Properties;

public
class ConfluentConsumerConfigurationProvider
    extends PropertiesBasedConsumerConfigurationProvider
{
    @Inject
    public
    ConfluentConsumerConfigurationProvider(Properties properties)
    {
        super(properties);
    }

    public
    ConfluentConsumerConfigurationProvider(
        Properties properties,
        String     consumerGroupKey)
    {
        super(properties,consumerGroupKey);
    }

    public
    ConfluentConsumerConfigurationProvider(
        Properties properties,
        String     bootstrapServersKey,
        String     schemaRegistryUrlKey,
        String     consumerGroupKey)
    {
        super(
            properties,
            bootstrapServersKey,
            schemaRegistryUrlKey,
            consumerGroupKey);
    }

    @Override
    public Map<String,Object>
    get()
    {
        Map<String,Object> properties = super.get();

        if (getProperties().containsKey("sasl.jaas.config"))
        {
            properties.put(
                "sasl.jaas.config",
                getProperties().get("sasl.jaas.config"));
            properties.put(
                "ssl.endpoint.identification.algorithm",
                "https");
            properties.put(
                "sasl.mechanism",
                "PLAIN");
            properties.put("security.protocol","SASL_SSL");
        }

        if (getProperties().containsKey(SchemaRegistryClientConfig.USER_INFO_CONFIG))
        {
            properties.put(
                SchemaRegistryClientConfig.SCHEMA_REGISTRY_USER_INFO_CONFIG,
                getProperties().get(SchemaRegistryClientConfig.USER_INFO_CONFIG));
            properties.put(
                SchemaRegistryClientConfig.BASIC_AUTH_CREDENTIALS_SOURCE,
                "USER_INFO");
        }

        return properties;
    }

}

//////////////////////////////////////////////////////////////////////////////
