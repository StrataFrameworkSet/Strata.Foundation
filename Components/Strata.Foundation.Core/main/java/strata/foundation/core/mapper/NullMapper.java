// ##########################################################################
// # File Name:	JsonObjectMapper.java
// ##########################################################################

package strata.foundation.core.mapper;

/****************************************************************************
 * 
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

// ##########################################################################
