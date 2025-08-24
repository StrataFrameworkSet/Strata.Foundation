/// ///////////////////////////////////////////////////////////////////////////
// PredicatedMapper.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import strata.foundation.core.collection.IMultiMap;
import strata.foundation.core.collection.ListValuedMultiMap;

import java.util.*;

public
class PredicatedMapper
{
    private final IMultiMap<MappingKey,PredicatedMapping> mappings;

    public
    PredicatedMapper()
    {
        this(new ListValuedMultiMap<>());
    }

    public
    PredicatedMapper(IMultiMap<MappingKey,PredicatedMapping> mappings)
    {
        this.mappings = mappings;
    }

    public <I,O> PredicatedMappingBuilder<I,O>
    beginTypeMap(Class<I> inputType,Class<O> outputType)
    {
        return new PredicatedMappingBuilder<>(inputType,outputType,this);
    }

    public PredicatedMapper
    clear()
    {
        mappings.clear();
        return this;
    }

    public PredicatedMapper
    clear(MappingKey key)
    {
        mappings.remove(key);
        return this;
    }

    public <I,O> Optional<O>
    map(I input,Class<O> outputType)
    {
        if (input != null)
        {
            List<PredicatedMapping> candidates =
                mappings
                    .flatten()
                    .stream()
                    .filter(m -> m.getKey().matches(input.getClass(),outputType))
                    .map(entry -> entry.getValue())
                    .filter(mapping -> mapping.test(input))
                    .toList();

            for (PredicatedMapping mapping: candidates)
            {
                try
                {
                    O output = mapping.apply(input);

                    if (output != null)
                        return Optional.of(output);
                }
                catch (ClassCastException ex) {}
            }
        }

        return Optional.empty();
    }

    public IMultiMap<MappingKey,PredicatedMapping>
    getMappings()
    {
        return mappings;
    }
}

//////////////////////////////////////////////////////////////////////////////