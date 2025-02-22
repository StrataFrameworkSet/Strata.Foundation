//////////////////////////////////////////////////////////////////////////////
// ConfluentProducerConfigurationProvider.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.kafka.event;

import io.confluent.kafka.schemaregistry.client.SchemaRegistryClientConfig;
import jakarta.inject.Inject;

import java.util.Map;
import java.util.Properties;

public
class ConfluentProducerConfigurationProvider
    extends PropertiesBasedProducerConfigurationProvider
{
    @Inject
    public
    ConfluentProducerConfigurationProvider(Properties properties)
    {
        super(properties);
    }

    public
    ConfluentProducerConfigurationProvider(
        Properties properties,
        String     bootstrapServersKey,
        String     schemaRegistryUrlKey,
        String     retriesKey,
        String     acksKey)
    {
        super(
            properties,
            bootstrapServersKey,
            schemaRegistryUrlKey,
            retriesKey,
            acksKey);
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
