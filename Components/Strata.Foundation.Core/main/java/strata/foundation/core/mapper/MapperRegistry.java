//////////////////////////////////////////////////////////////////////////////
// MapperRegistry.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * Thread-safe registry of {@link IMapper} instances keyed by destination
 * and source type. Mappers can be registered, looked up, and unregistered
 * by their destination/source type pair.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation
 * MapperRegistry registry = MapperRegistry.of();
 *
 * // Registration
 * registry.register(String.class,Integer.class,source -&gt; String.valueOf(source));
 *
 * // Lookup
 * boolean exists = registry.has(String.class,Integer.class);
 * Optional&lt;IMapper&lt;String,Integer&gt;&gt; mapper =
 *     registry.get(String.class,Integer.class);
 *
 * // Removal
 * registry.unregister(String.class,Integer.class);
 * registry.unregisterAll();
 * </pre>
 */
public
class MapperRegistry
{
    private final Map<MappingKey,IMapper<?,?>> mappers =
        new ConcurrentHashMap<>();

    public
    MapperRegistry() {}

    public <D,S> MapperRegistry
    register(Class<D> destType,Class<S> srcType,IMapper<D,S> mapper)
    {
        mappers.put(MappingKey.of(srcType,destType), mapper);
        return this;
    }

    public <D,S> MapperRegistry
    unregister(Class<D> destType,Class<S> srcType)
    {
        mappers.remove(MappingKey.of(srcType,destType));
        return this;
    }

    public MapperRegistry
    unregisterAll()
    {
        mappers.clear();
        return this;
    }

    public <D,S> boolean
    has(Class<D> destType,Class<S> srcType)
    {
        return mappers.containsKey(MappingKey.of(srcType,destType));
    }

    public <D,S> Optional<IMapper<D,S>>
    get(Class<D> destType,Class<S> srcType)
    {
        return
            Optional.ofNullable(
                (IMapper<D,S>)
                    mappers.get(MappingKey.of(srcType,destType)));
    }

    public static MapperRegistry
    of() { return new MapperRegistry(); }
}

//////////////////////////////////////////////////////////////////////////////
