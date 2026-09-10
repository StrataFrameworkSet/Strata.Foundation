//////////////////////////////////////////////////////////////////////////////
// IObjectMapper.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

/**
 * <p>
 * Converts objects of type {@code T} (or its subtypes) to and from a
 * payload representation of type {@code P}, such as a {@link String} of
 * JSON or a {@code byte[]} of serialized bytes.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * IObjectMapper&lt;Customer,String&gt; mapper = ...;
 *
 * String payload = mapper.toPayload(customer);
 * Customer customer = mapper.toObject(Customer.class,payload);
 * </pre>
 *
 * @param <T> - the upper bound of the object types this mapper converts
 * @param <P> - the payload type produced and consumed by this mapper
 */
public
interface IObjectMapper<T,P>
{
    <S extends T> P
    toPayload(S object);

    <S extends T> S
    toObject(Class<S> type,P payload);
}

//////////////////////////////////////////////////////////////////////////////
