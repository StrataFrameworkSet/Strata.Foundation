//////////////////////////////////////////////////////////////////////////////
// GeoLocation.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import strata.foundation.core.utility.HashCodeBuilder;

import java.io.Serializable;

/**
 * <p>
 * An immutable value type representing a geographic coordinate as a
 * {@code latitude}/{@code longitude} pair. See
 * <a href="https://en.wikipedia.org/wiki/Geographic_coordinate_system">
 * Geographic coordinate system</a> for background on how latitude and
 * longitude describe a position on Earth.
 * </p>
 * <p>
 * Equality compares both coordinates within a small epsilon to tolerate
 * floating-point imprecision, while ordering via {@link
 * java.lang.Comparable} compares {@code latitude} first and then
 * {@code longitude}, using exact {@link java.lang.Double#compare(double,
 * double)} semantics. Instances implement {@link java.io.Serializable}.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * GeoLocation location = GeoLocation.of(39.7817, -89.6501);
 *
 * double latitude = location.getLatitude();
 * double longitude = location.getLongitude();
 *
 * int order = location.compareTo(GeoLocation.of(34.0522, -118.2437));
 * </pre>
 */
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
