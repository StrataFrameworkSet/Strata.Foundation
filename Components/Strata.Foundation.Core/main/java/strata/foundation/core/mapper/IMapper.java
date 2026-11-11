//////////////////////////////////////////////////////////////////////////////
// IMapper.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

/**
 * <p>
 * Maps a source object of type {@code S} to a destination object of
 * type {@code D}.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * // Creation using lambda
 * IMapper&lt;String,Integer&gt; mapper = source -&gt; String.valueOf(source);
 *
 * // Mapping
 * String result = mapper.map(42);
 * </pre>
 *
 * @param <D> destination type
 * @param <S> source type
 */
public 
interface IMapper<D,S>
{
    D
    map(S source);
}

//////////////////////////////////////////////////////////////////////////////