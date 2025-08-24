import {ISupplier, LambdaSupplier} from "../Utility";
import {ISerializable} from "./ISerializable";
import {ISupplierOrLambda} from "../Utility/LambdaSupplier";
import {SerializationException} from "./SerializationException";

export
class SerializationManager
{
    private static suppliers: Map<string,ISupplier<ISerializable>> =
        new Map<string,ISupplier<ISerializable>>();


    static register(
        typename: string,
        supplier: ISupplierOrLambda<ISerializable>): void
    {
        SerializationManager
            .suppliers
            .set(typename,LambdaSupplier.of(supplier));
    }

    static create<T extends ISerializable>(typename: string): T
    {
        const supplier: ISupplier<ISerializable> =
            SerializationManager
                .suppliers
                .get(typename);

        if (supplier)
            return supplier.get() as T;
        else
            throw new SerializationException(
                `No supplier registered for type: ${typename}`);
    }

    static isSerializable(value: any): boolean
    {
        if (SerializationManager.isPrimitive(value))
            return true;

        if (SerializationManager.isArray(value))
            return true;

        if (SerializationManager.isMap(value))
            return true;

        return SerializationManager.implementsSerializable(value);
    }

    static isPrimitive(value: any): boolean
    {
        return (value == null) ||
            (typeof value === "string") ||
            (typeof value === "number") ||
            (typeof value === "boolean") ||
            (value instanceof Date);
    }

    static isArray(value: any): boolean
    {
        if (!SerializationManager.isMap(value))
            return Array.isArray(value);

        return false;
    }

    static isMap<K,V>(value: Map<K,V> | any): boolean
    {
        return SerializationManager.isMapWriteCase(value) ||
            SerializationManager.isMapReadCase(value);
    }

    static implementsSerializable(value: any): boolean
    {
        return (value != null) &&
            (typeof value === "object") &&
            ("getTypeName" in value) &&
            (typeof value["getTypeName"] === "function") &&
            ("writeTo" in value) &&
            (typeof value["writeTo"] === "function") &&
            ("readFrom" in value) &&
            (typeof value["readFrom"] === "function");
    }

    static isOptional(value: any): boolean
    {
        return SerializationManager.isOptionalWriteCase(value) ||
            SerializationManager.isOptionalReadCase(value);
    }

    static getTypeName(value: any): string
    {
        if (value == null)
            return "null";

        if (SerializationManager.hasTypeNameProperty(value))
            return value["typename"];

        if (SerializationManager.isPrimitive(value))
        {
            switch (typeof value)
            {
                case "string":
                    return "string";
                case "number":
                    return "number";
                case "boolean":
                    return "boolean";
                case "object":
                    if (value instanceof Date)
                        return "Date";
                    else
                        throw new SerializationException(
                            `Unknown primitive type: ${value}`);
                default:
                    throw new SerializationException(
                        `Unknown primitive type: ${value}`);
            }
        }

        if (SerializationManager.isArray(value))
            return "Array";

        if (SerializationManager.isMap(value))
            return "Map";

        if (SerializationManager.isOptional(value))
            return "Optional";

        if (SerializationManager.implementsSerializable(value))
            return (value as ISerializable).getTypeName();

        throw new SerializationException(
            `Cannot determine type name for value: ${value}`);
    }

    private static isOptionalWriteCase(value: any): boolean
    {
        return (value != null) &&
            (typeof value === "object") &&
            ("isPresent" in value) &&
            (typeof value["isPresent"] === "function") &&
            ("get" in value) &&
            (typeof value["get"] === "function") &&
            ("orElse" in value) &&
            (typeof value["orElse"] === "function");
    }

    private static isOptionalReadCase(value: any): boolean
    {
        return (value != null) &&
            (typeof value === "object") &&
            (value.hasOwnProperty("typename")) &&
            (value["typename"] === "Optional") &&
            (value.hasOwnProperty("value"));
    }

    private static isMapWriteCase<K,V>(value: Map<K,V> | any): boolean
    {
        return (value != null) && (value instanceof Map);
    }

    private static isMapReadCase(value: any): boolean
    {
        return (Array.isArray(value)) &&
            (value.every(
                item => Array.isArray(item) && item.length === 2));
    }

    private static hasTypeNameProperty(value: any): boolean
    {
        return (value != null) &&
            (typeof value === "object") &&
            (value.hasOwnProperty("typename")) &&
            (typeof value["typename"] === "string");
    }
}