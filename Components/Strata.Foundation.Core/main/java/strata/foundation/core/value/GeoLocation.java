//////////////////////////////////////////////////////////////////////////////
// GeoLocation.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import strata.foundation.core.utility.HashCodeBuilder;

import java.io.Serializable;

public
class GeoLocation
    implements Serializable, Comparable<GeoLocation>
{
    private final double latitude;
    private final double longitude;

    @JsonCreator
    public
    GeoLocation(
        @JsonProperty("latitude") double  latitude,
        @JsonProperty("longitude") double longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public int
    hashCode()
    {
        return
            new HashCodeBuilder()
                .append(latitude)
                .append(longitude)
                .hashCode();
    }

    @Override
    public boolean
    equals(Object other)
    {
        return
            other instanceof GeoLocation location
                && equals(latitude,location.latitude)
                && equals(longitude,location.longitude);
    }

    @Override
    public String
    toString()
    {
        return
            String.format(
                "{latitude=%s,longitude=%s}",latitude,longitude);
    }

    @Override
    public int
    compareTo(GeoLocation other)
    {
        return
            Double.compare(latitude, other.latitude) != 0
                ? Double.compare(latitude, other.latitude)
                : Double.compare(longitude, other.longitude);
    }

    @JsonProperty("latitude")
    public double
    getLatitude()
    {
        return latitude;
    }

    @JsonProperty("longitude")
    public double
    getLongitude()
    {
        return longitude;
    }

    public static GeoLocation
    of(double latitude, double longitude)
    {
        return new GeoLocation(latitude, longitude);
    }

    private static boolean
    equals(double a,double b)
    {
        double epsilon = 0.0000000000001;

        return Math.abs(a - b) < epsilon;
    }
}

//////////////////////////////////////////////////////////////////////////////
