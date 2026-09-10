//////////////////////////////////////////////////////////////////////////////
// ISecureMapper.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import java.io.Serializable;

/**
 * <p>
 * Encrypts a value of type {@code T} to an opaque {@link String} and
 * decrypts it back to its original value. Implementations are
 * responsible for whatever encoding, serialization, and cryptographic
 * transformation is needed to safely round-trip the value.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * ISecureMapper&lt;Customer&gt; mapper = ...;
 *
 * String encrypted = mapper.mapEncrypted(customer);
 * Customer decrypted = mapper.mapDecrypted(encrypted);
 * </pre>
 *
 * @param <T> - the {@link Serializable} type of value being encrypted and decrypted
 */
public
interface ISecureMapper<T extends Serializable>
{
    String
    mapEncrypted(T value);

    T
    mapDecrypted(String value);
}

//////////////////////////////////////////////////////////////////////////////
