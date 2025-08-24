import {IObjectWriter} from "./IObjectWriter";
import {IObjectReader} from "./IObjectReader";

export
interface ISerializable
{
    writeTo(writer: IObjectWriter): void;

    readFrom(reader: IObjectReader): void;

    getTypeName(): string;
}