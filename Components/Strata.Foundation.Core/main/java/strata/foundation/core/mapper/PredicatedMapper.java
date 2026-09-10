//////////////////////////////////////////////////////////////////////////////
// PredicatedMapper.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import strata.foundation.core.collection.IMultiMap;
import strata.foundation.core.collection.ListValuedMultiMap;

import java.util.*;

/**
 * <p>
 * Registry of predicated mapping rules, keyed by input and output type,
 * that maps an input value to an output value by trying each
 * registered {@link PredicatedMapping} whose type matches until one
 * whose predicate accepts the input successfully produces a non-null
 * result. Mapping rules are registered fluently via
 * {@link #beginTypeMap(Class,Class)}, which returns a
 * {@link PredicatedMappingBuilder} for adding one or more predicate and
 * mapping function pairs for a given input/output type combination.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * PredicatedMapper mapper = new PredicatedMapper();
 *
 * mapper
 *     .beginTypeMap(String.class,Integer.class)
 *     .addMapping(s -&gt; s.matches("\\d+"),Integer::parseInt);
 *
 * Optional&lt;Integer&gt; result = mapper.map("42",Integer.class);
 * </pre>
 */
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
                    .filter(
                        entry ->
                            entry
                                .getKey()
                                .matches(input.getClass(),outputType))
                    .map(entry -> entry.getValue())
                    .filter(mapping -> mapping.test(input))
                    .toList();

            for (PredicatedMapping mapping: candidates)
            {
                try
                {
                    O output = mapping.apply(input);

                    if (output != null)
                        return Optional.of(outputType.cast(output));
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
