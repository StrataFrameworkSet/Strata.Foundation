//////////////////////////////////////////////////////////////////////////////
// IConfiguration.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.configuration;

import strata.foundation.core.collection.Pair;

import java.util.Map;
import java.util.stream.Stream;

/**
 * <p>
 * Key-value configuration abstraction providing typed property access
 * (String, Boolean, Long), prefix-based filtering via
 * {@link java.util.Map}, existence checks, and streaming over
 * configuration entries as {@link java.util.stream.Stream} of
 * key-value pairs.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Property access
 * IConfiguration config = ...;
 * String  url     = config.getProperty("db.url");
 * String  fallback = config.getProperty("db.url","jdbc:h2:mem:test");
 * Boolean enabled = config.getBooleanProperty("feature.enabled");
 * Long    timeout = config.getLongProperty("connection.timeout");
 *
 * // Prefix-based filtering
 * Map&lt;String,Object&gt; dbProps = config.getProperties("db.");
 *
 * // Streaming
 * config.stream().forEach(pair -&gt;
 *     System.out.println(pair.getFirst() + "=" + pair.getSecond()));
 * </pre>
 */
public
interface IConfiguration
{
    String
    getProperty(String key);

    String
    getProperty(String key, String defaultValue);

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
