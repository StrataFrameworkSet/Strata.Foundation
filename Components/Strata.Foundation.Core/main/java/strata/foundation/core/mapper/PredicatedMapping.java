//////////////////////////////////////////////////////////////////////////////
// PredicatedMapping.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p>
 * A single mapping rule consisting of a {@link MappingKey}, a
 * {@link Predicate} that decides whether the rule applies to a given
 * input, and a {@link Function} that performs the actual conversion.
 * The predicate and mapping function are stored without their generic
 * type information and are cast against the input's runtime type when
 * {@link #test(Object)} and {@link #apply(Object)} are invoked, which
 * is why both methods declare a {@link ClassCastException} for inputs
 * that do not match the key's expected type. Instances of this class
 * are typically created and registered through a
 * {@link PredicatedMappingBuilder} rather than directly.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * MappingKey key = MappingKey.of(String.class,Integer.class);
 *
 * PredicatedMapping mapping =
 *     PredicatedMapping.of(
 *         key,
 *         (Predicate&lt;String&gt;)s -&gt; s.matches("\\d+"),
 *         (Function&lt;String,Integer&gt;)Integer::parseInt);
 *
 * Integer result = mapping.apply("42");
 * </pre>
 * </p>
 */
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
