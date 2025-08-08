import {PersonName} from "./PersonName";
import {IFormatter} from "./IFormatter";

export
interface IPersonNameFormatter
    extends IFormatter<PersonName,string> {}