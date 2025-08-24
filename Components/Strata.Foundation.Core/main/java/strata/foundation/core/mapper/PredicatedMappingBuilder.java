/// ///////////////////////////////////////////////////////////////////////////
// PredicatedMappingBuilder.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public
class PredicatedMappingBuilder<I,O>
{
    private final MappingKey       key;
    private final PredicatedMapper parent;

    public PredicatedMappingBuilder(
        Class<I>         inputType,
        Class<O>         outputType,
        PredicatedMapper parent)
    {
        this.key = MappingKey.of(inputType,outputType);
        this.parent = parent;
    }

    public <Ix,Ox> PredicatedMappingBuilder<Ix,Ox>
    beginTypeMap(Class<Ix> inputType,Class<Ox> outputType)
    {
        return new PredicatedMappingBuilder<>(inputType,outputType,parent);
    }

    public PredicatedMappingBuilder<I,O>
    addMapping(Predicate<I> predicate,Function<I,O> mapper)
    {
        Objects.requireNonNull(predicate,"predicate cannot be null");
        Objects.requireNonNull(mapper,"mapper cannot be null");

        PredicatedMapping mapping = new PredicatedMapping(key,predicate,mapper);

        parent
            .getMappings()
            .put(key,mapping);
        return this;
    }

    public PredicatedMapper
    toMapper()
    {
        return parent;
    }
}

//////////////////////////////////////////////////////////////////////////////
