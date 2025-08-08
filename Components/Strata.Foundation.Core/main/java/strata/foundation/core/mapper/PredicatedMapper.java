/// ///////////////////////////////////////////////////////////////////////////
// PredicatedMapper.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public
class PredicatedMapper<I,O>
    implements Supplier<Optional<O>>
{
    private Optional<I> input;
    private final Map<Predicate<I>,Function<I,O>> mappings;

    public
    PredicatedMapper()
    {
        this(null);
    }

    public
    PredicatedMapper(I input)
    {
        this.input = Optional.ofNullable(input);
        this.mappings = new HashMap<>();
    }


    public PredicatedMapper<I,O>
    addMapping(Predicate<I> predicate,Function<I,O> mapper)
        throws NullPointerException
    {
        Objects.requireNonNull(predicate,"predicate cannot be null");
        Objects.requireNonNull(mapper,"mapper cannot be null");

        mappings.put(predicate,mapper);
        return this;
    }

    public PredicatedMapper<I,O>
    clearMappings()
    {
        mappings.clear();
        return this;
    }

    @Override

    public Optional<O>
    get()
    {
        return map();
    }

    public Optional<O>
    map()
    {
        if (input.isPresent())
        {
            I value = input.get();

            for (Map.Entry<Predicate<I>,Function<I,O>> entry: mappings.entrySet())
            {
                Predicate<I> predicate = entry.getKey();
                Function<I,O> mapper = entry.getValue();

                if (predicate.test(value))
                    return Optional.ofNullable(mapper.apply(value));
            }
        }

        return Optional.empty();
    }

    public Optional<O>
    map(I input)
    {
        this.input = Optional.ofNullable(input);
        return map();
    }

    public static <I,O> PredicatedMapper<I,O>
    of()
    {
        return new PredicatedMapper<>();
    }

    public static <I,O> PredicatedMapper<I,O>
    of(Class<I> inputType,Class<O> outputType)
    {
        return of();
    }

    public static <I,O> PredicatedMapper<I,O>
    of(I input)
    {
        return new PredicatedMapper<>(input);
    }

    public static <I,O> PredicatedMapper<I,O>
    of(I input,Class<O> outputType)
    {
        return of(input);
    }
}

//////////////////////////////////////////////////////////////////////////////
