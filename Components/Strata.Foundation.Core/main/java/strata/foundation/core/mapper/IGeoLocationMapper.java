//////////////////////////////////////////////////////////////////////////////
// IGeoLocationMapper.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import strata.foundation.core.value.GeoLocation;
import strata.foundation.core.value.PostalAddress;

import java.util.Optional;

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