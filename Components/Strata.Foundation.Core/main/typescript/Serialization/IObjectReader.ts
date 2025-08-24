import {ISerializable} from "./ISerializable";
import {SerializableType} from "./SerializableType";
import {Optional} from "../Utility";

export
interface IObjectReader
{
    read<T extends ISerializable>(): T;

    readBoolean(key: string): boolean;

    readString(key: string): string;

    readNumber(key: string): number;

    readDate(key: string): Date;

    readArray<T extends SerializableType>(key: string): T[];

    readMap<K extends SerializableType,V extends SerializableType>(key: string):
        Map<K,V>;

    readObject<T extends ISerializable>(key: string): T;

    readOptional<T extends SerializableType>(key: string): Optional<T>;
}