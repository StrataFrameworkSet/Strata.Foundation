import {PostalAddress} from "./PostalAddress";

export
class PostalAddressBuilder
{
    private address: string;
    private street: string;
    private city: string;
    private state: string;
    private countryCode: string;
    private postalCode: string;

    constructor()
    {
        this.address = null;
        this.street = null;
        this.city = null;
        this.state = null;
        this.countryCode = null;
        this.postalCode = null;
    }

    setAddress(address: string): PostalAddressBuilder
    {
        this.address = address;
        return this;
    }

    setStreet(street: string): PostalAddressBuilder
    {
        this.street = street;
        return this;
    }

    setCity(city: string): PostalAddressBuilder
    {
        this.city = city;
        return this;
    }

    setState(state: string): PostalAddressBuilder
    {
        this.state = state;
        return this;
    }

    setCountryCode(countryCode: string): PostalAddressBuilder
    {
        this.countryCode = countryCode;
        return this;
    }

    setPostalCode(postalCode: string): PostalAddressBuilder
    {
        this.postalCode = postalCode;
        return this;
    }

    build(): PostalAddress
    {
        if (!this.address) throw Error("Address must be non-null");
        if (!this.street) throw Error("Street must be non-null");
        if (!this.city) throw Error("City must be non-null");
        if (!this.state) throw Error("State must be non-null");
        if (!this.countryCode) throw Error("CountryCode must be non-null");
        if (!this.postalCode) throw Error("PostalCode must be non-null");

        return {
            address: this.address,
            street: this.street,
            city: this.city,
            state: this.state,
            countryCode: this.countryCode,
            postalCode: this.postalCode
        };
    }
}