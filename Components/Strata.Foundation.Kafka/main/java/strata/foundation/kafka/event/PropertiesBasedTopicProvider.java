//////////////////////////////////////////////////////////////////////////////
// PropertiesBasedTopicProvider.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.kafka.event;

import jakarta.inject.Inject;

import java.util.Properties;

public
class PropertiesBasedTopicProvider
    implements ITopicProvider
{
    private final Properties itsProperties;
    private final String     itsTopicKey;

    @Inject
    public
    PropertiesBasedTopicProvider(Properties properties)
    {
        this(properties,"kafka.topic");
    }

    public
    PropertiesBasedTopicProvider(Properties properties,String topicKey)
    {
        itsProperties = properties;
        itsTopicKey   = topicKey;

        if (!itsProperties.containsKey(itsTopicKey))
            throw
                new IllegalArgumentException(
                    "properties does not contain key: " + itsTopicKey);
    }

    @Override
    public String
    get()
    {
        return itsProperties.getProperty(itsTopicKey);
    }
}

//////////////////////////////////////////////////////////////////////////////
