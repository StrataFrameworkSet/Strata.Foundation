//////////////////////////////////////////////////////////////////////////////
// IGeoLocationMapper.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import strata.foundation.core.value.GeoLocation;
import strata.foundation.core.value.PostalAddress;

import java.util.Optional;

/**
 * <p>
 * Resolves a geographic coordinate, expressed as a {@link GeoLocation},
 * from location information such as a {@link PostalAddress}, a postal
 * code, or a city and state pair. Implementations may not be able to
 * resolve every input to a location, in which case an empty
 * {@link Optional} is returned rather than throwing.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * IGeoLocationMapper mapper = ...;
 *
 * Optional&lt;GeoLocation&gt; location = mapper.map("90210");
 * location.ifPresent(loc -&gt; System.out.println(loc));
 * </pre>
 * </p>
 */
public
interface IGeoLocationMapper
{
    Optional<GeoLocation>
    map(PostalAddress address);

    Optional<GeoLocation>
    map(String postalCode);

    Optional<GeoLocation>
    map(String city,String state);
}

//////////////////////////////////////////////////////////////////////////////