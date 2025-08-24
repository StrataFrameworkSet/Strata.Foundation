import {ISerializable} from "./ISerializable";
import {SerializableType} from "./SerializableType";
import {Optional} from "../Utility";

export
interface IObjectWriter
{
    write<T extends ISerializable>(value: T): IObjectWriter;

    writeBoolean(key: string, value: boolean): void;

    writeString(key: string, value: string): void;

    writeNumber(key: string, value: number): void;

    writeDate(key: string, value: Date): void;

    writeArray<T extends SerializableType>(key: string, values: T[]): void;

    writeMap<K extends SerializableType,V extends SerializableType>(
        key: string, values: Map<K,V>): void;

    writeObject<T extends ISerializable>(key: string, value: T): void;

    writeOptional<T extends SerializableType>(
        key: string, value: Optional<T>): void;
}