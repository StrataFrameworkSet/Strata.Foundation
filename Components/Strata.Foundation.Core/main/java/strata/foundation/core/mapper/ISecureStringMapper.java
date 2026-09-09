//////////////////////////////////////////////////////////////////////////////
// ISecureStringMapper.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

/**
 * <p>
 * An {@link ISecureMapper} specialized for {@link String} values,
 * where the encrypted and decrypted forms are both plain strings and
 * no intermediate serialization step is needed.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * ISecureStringMapper mapper = ...;
 *
 * String encrypted = mapper.mapEncrypted("sensitive-value");
 * String decrypted = mapper.mapDecrypted(encrypted);
 * </pre>
 * </p>
 */
public
interface ISecureStringMapper
    extends ISecureMapper<String> {}

//////////////////////////////////////////////////////////////////////////////