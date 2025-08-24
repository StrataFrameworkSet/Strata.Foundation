//////////////////////////////////////////////////////////////////////////////
// PredicatedMapping.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import java.util.function.Function;
import java.util.function.Predicate;

public
class PredicatedMapping
{
    private final MappingKey    key;
    private final Predicate<?>  predicate;
    private final Function<?,?> mapping;

    public
    PredicatedMapping(
        MappingKey    key,
        Predicate<?>  predicate,
        Function<?,?> mapping)
    {
        this.key       = key;
        this.predicate = predicate;
        this.mapping   = mapping;
    }

    public <I> boolean
    test(I input)
        throws ClassCastException
    {
        if (key.matchesInput(input.getClass()))
        {
            Predicate<I> p = (Predicate<I>)predicate;
            return p.test(input);
        }

        throw new ClassCastException("input is not of the expected type");
    }

    public <I,O> O
    apply(I input)
        throws ClassCastException
    {
        if (key.matchesInput(input.getClass()))
        {
            Function<I,O> m = (Function<I,O>)mapping;
            O output = m.apply(input);

            if (key.matchesOutput(output.getClass()))
                return output;
        }

        throw new ClassCastException("input is not of the expected type");
    }

    public static PredicatedMapping
    of(
        MappingKey    key,
        Predicate<?>  predicate,
        Function<?,?> mapping)
    {
        return new PredicatedMapping(key,predicate,mapping);
    }
}

//////////////////////////////////////////////////////////////////////////////
