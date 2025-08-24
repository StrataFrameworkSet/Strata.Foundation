import type {ISerializable} from "./ISerializable";

export type SerializableType =
    ISerializable | boolean | string | number | Date |
    SerializableType[] | Map<SerializableType,SerializableType>;

