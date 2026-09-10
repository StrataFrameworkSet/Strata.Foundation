//////////////////////////////////////////////////////////////////////////////
// IPostalCodeToCountryCodeMapper.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import java.util.Set;

/**
 * <p>
 * Resolves the set of possible ISO country codes that a given postal
 * code could belong to. A postal code's format is not always unique to
 * a single country, so implementations return a {@link Set} that may
 * contain zero, one, or several country codes depending on how
 * ambiguous the postal code's format is.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * IPostalCodeToCountryCodeMapper mapper = ...;
 *
 * Set&lt;String&gt; countryCodes = mapper.map("90210");
 * </pre>
 */
public
interface IPostalCodeToCountryCodeMapper
{
    Set<String>
    map(String postalCode);
}

//////////////////////////////////////////////////////////////////////////////
