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
 * <h4>Type Parameters</h4>
 * <ul>
 * <li>{@code <T>} - the upper bound of the object types this mapper converts</li>
 * <li>{@code <P>} - the payload type produced and consumed by this mapper</li>
 * </ul>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * IObjectMapper&lt;Customer,String&gt; mapper = ...;
 *
 * String payload = mapper.toPayload(customer);
 * Customer customer = mapper.toObject(Customer.class,payload);
 * </pre>
 * </p>
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
