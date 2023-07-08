//////////////////////////////////////////////////////////////////////////////
// IConfiguration.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.configuration;

import strata.foundation.core.container.Pair;

import java.util.Map;
import java.util.stream.Stream;

public
interface IConfiguration
{
    String
    getProperty(String key);

    Boolean
    getBooleanProperty(String key);

    Long
    getLongProperty(String key);

    Map<String,Object>
    getProperties(String... prefixes);

    boolean
    hasProperty(String key);

    boolean
    hasBooleanProperty(String key);

    boolean
    hasLongProperty(String key);

    Stream<Pair<String,Object>>
    stream();
}

//////////////////////////////////////////////////////////////////////////////