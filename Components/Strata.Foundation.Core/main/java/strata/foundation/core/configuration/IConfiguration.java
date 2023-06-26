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

    Map<String,Object>
    getProperties(String... prefixes);

    boolean
    hasProperty(String key);

    Stream<Pair<String,Object>>
    stream();
}

//////////////////////////////////////////////////////////////////////////////