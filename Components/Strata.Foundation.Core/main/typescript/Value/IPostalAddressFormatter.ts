import {PostalAddress} from "./PostalAddress";
import {IFormatter} from "./IFormatter";

export
interface IPostalAddressFormatter
    extends IFormatter<PostalAddress,string> {}