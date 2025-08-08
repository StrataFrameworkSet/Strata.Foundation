import {IPostalAddressFormatter} from "./IPostalAddressFormatter";
import {PostalAddress} from "./PostalAddress";
import {Expendable} from "../Utility";

export
class UsPostalAddressFormatter
    implements IPostalAddressFormatter
{
    private addressee: Expendable<string>;

    public constructor(addressee?:string)
    {
        this.addressee = Expendable.of(addressee);
    }

    public format(value: PostalAddress): string
    {
        return this.addressee
            .ifPresentOrElse(
                addressee => `${addressee}\n`,
                () => "") +
            `${this.getStreetAddress(value.address,value.street)}\n` +
            `${value.city}, ${value.state} ${value.postalCode}`;
    }

    public setAddressee(addressee: string): IPostalAddressFormatter
    {
        this.addressee = Expendable.of(addressee);
        return this;
    }

    private getStreetAddress(address:string,street:string): string
    {
        if (address == null || address.length === 0)
            return street;
        else if (street == null || street.length === 0)
            return address;
        else
            return `${address} ${street}`;
    }
}