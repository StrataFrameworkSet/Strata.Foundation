//////////////////////////////////////////////////////////////////////////////
// MapperRegistry.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public
class MapperRegistry
{
    private static Map<MappingKey,IMapper<?,?>> mappers =
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
