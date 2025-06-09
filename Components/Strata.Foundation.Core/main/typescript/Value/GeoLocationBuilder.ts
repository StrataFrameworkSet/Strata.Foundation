import {GeoLocation} from "./GeoLocation";

export
class GeoLocationBuilder
{
    private latitude: number;
    private longitude: number;

    constructor()
    {
        this.latitude = 0;
        this.longitude = 0;
    }

    setLatitude(latitude: number): GeoLocationBuilder
    {
        this.latitude = latitude;
        return this;
    }

    setLongitude(longitude: number): GeoLocationBuilder
    {
        this.longitude = longitude;
        return this;
    }

    build(): GeoLocation
    {
        return {latitude: this.latitude, longitude: this.longitude};
    }
}