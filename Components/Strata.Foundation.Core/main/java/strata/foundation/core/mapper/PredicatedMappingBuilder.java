//////////////////////////////////////////////////////////////////////////////
// PredicatedMappingBuilder.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p>
 * Fluent builder for registering one or more predicated mapping rules,
 * for a single input/output type combination, with a
 * {@link PredicatedMapper}. Instances are obtained from
 * {@link PredicatedMapper#beginTypeMap(Class,Class)} or from
 * {@link #beginTypeMap(Class,Class)} to chain into a new type
 * combination, and {@link #addMapping(Predicate,Function)} may be
 * called any number of times to register additional predicate and
 * mapping function pairs before returning to the parent
 * {@link PredicatedMapper} via {@link #toMapper()}.
 * </p>
 * <h4>Type Parameters</h4>
 * <ul>
 * <li>{@code <I>} - the input type this builder's mapping rules accept</li>
 * <li>{@code <O>} - the output type this builder's mapping rules produce</li>
 * </ul>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * PredicatedMapper mapper =
 *     new PredicatedMapper()
 *         .beginTypeMap(String.class,Integer.class)
 *         .addMapping(s -&gt; s.matches("\\d+"),Integer::parseInt)
 *         .toMapper();
 * </pre>
 * </p>
 */
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
