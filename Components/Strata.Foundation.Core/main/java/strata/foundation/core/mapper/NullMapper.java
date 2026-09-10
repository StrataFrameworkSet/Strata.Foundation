//////////////////////////////////////////////////////////////////////////////
// NullMapper.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

/**
 * <p>
 * Implementation of {@link IObjectMapper} that performs no conversion
 * at all: the payload type is the same as the object type, and mapping
 * in either direction is simply an identity operation (with a cast on
 * the way back to the requested subtype). Useful as a no-op mapper
 * where an {@link IObjectMapper} is required by an API but no actual
 * transformation of the value is needed.
 * </p>
 * <br/>
 * <b>Type Parameter</b><br/>
 * {@code <T>} - the type of object passed through unchanged by this mapper
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * NullMapper&lt;Customer&gt; mapper = new NullMapper&lt;&gt;();
 *
 * Customer payload = mapper.toPayload(customer);
 * // payload == customer
 * </pre>
 */
public
class NullMapper<T>
    implements IObjectMapper<T,T>
{

    /************************************************************************
     * Creates a new JsonObjectMapper.
     *
     */
    public
    NullMapper() {}

    /************************************************************************
     * {@inheritDoc} 
     */
    @Override
    public <S extends T> T
    toPayload(S object)
    {
        return object;
    }

    /************************************************************************
     * {@inheritDoc} 
     */
    @Override
    public <S extends T> S 
    toObject(Class<S> type,T payload)
    {
        return type.cast(payload);
    }

}

//////////////////////////////////////////////////////////////////////////////
