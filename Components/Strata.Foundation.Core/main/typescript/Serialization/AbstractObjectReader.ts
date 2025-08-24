import {IObjectReader} from "./IObjectReader";
import {ISerializable} from "./ISerializable";
import {ISupplier, Optional} from "../Utility";
import {SerializationException} from "./SerializationException";
import {SerializationManager} from "./SerializationManager";
import {SerializableType} from "./SerializableType";

export
abstract class AbstractObjectReader
    implements IObjectReader
{
    private static suppliers: Map<string,ISupplier<ISerializable>> =
        new Map<string,ISupplier<ISerializable>>();

    private stack: any[];
    private root: any;

    protected constructor(root: any)
    {
        this.stack = [];
        this.root = null;
        this.push(root);
    }

    read<T extends ISerializable>(): T
    {
        console.log("read(): " + this.root);
        const output: T =
            AbstractObjectReader.create<T>(
                this.readString("typename"));

        if (output != null)
        {
            this.push(this.root["value"]);
            output.readFrom(this);
            this.pop();
            this.pop();
            return output;
        }

        return null;
    }

    readBoolean(key: string): boolean
    {
        return this.root[key] as boolean;
    }

    readString(key: string): string
    {
        return this.root[key] as string;
    }

    readDate(key: string): Date
    {
        return this.root[key] as Date;
    }

    readNumber(key: string): number
    {
        return this.root[key] as number;
    }

    readArray<T extends SerializableType>(key: string): T[]
    {
        const values: any[] = this.root[key] as any[];
        const output: T[] = [];

        if (values != null)
            return values.map(value => this.fromAny(value));

        return [];
    }

    readMap<K extends SerializableType,V extends  SerializableType>(key: string):
        Map<K,V>
    {
        const values: Map<any,any> = new Map(this.root[key] as [any,any][]);
        const output: Map<K,V> = new Map<K,V>();

        console.log("readMap(" + key + "): " + values);
        if (values != null)
            Array
                .from(values.entries())
                .forEach(
                    ([k, v]) =>
                        output.set(this.fromAny(k), this.fromAny(v)));

        return output;
    }

    readObject<T extends ISerializable>(key: string): T
    {
        this.push(this.root[key]);
        console.log("readObject(" + key + "): " + this.root);

        const output: T = AbstractObjectReader.create<T>(
            this.readString("typename"));

        if (output != null)
        {
            this.push(this.root["value"]);
            output.readFrom(this);
            this.pop();
            this.pop();
        }

        return output;
    }

    readOptional<T extends SerializableType>(key: string): Optional<T>
    {
        const value: any = this.root[key];

        if (value == null)
            return Optional.empty<T>();

        return Optional.ofNullable(this.fromAny(value) as T);
    }

    protected static create<T extends ISerializable>(typename: string): T
    {
        return SerializationManager.create<T>(typename);
    }

    protected setRoot(root: any): void
    {
        this.root = root;
    }

    protected push(root: any): void
    {
        this.stack.push(root);
        this.root = root;
    }

    protected pop(): void
    {
        this.root = this.stack.pop();
    }

    protected fromAny<T extends SerializableType>(value: any): T
    {
        if (value == null)
            return null;

        if (this.isPrimitive(value))
            return value as T;

        if (this.isSerializable(value))
            return new BasicObjectReader(value).read<ISerializable>() as T;

        if (this.isMap(value))
        {
            const input: Map<any,any> = new Map(value);
            const output: Map<SerializableType,SerializableType> =
                new Map<SerializableType,SerializableType>();

            Array
                .from(input.entries())
                .forEach(
                    ([k, v]) =>
                        output.set(this.fromAny(k), this.fromAny(v)));
            return output as T;
        }

        if (this.isArray(value))
            return this.readArray(value) as T;

        throw new SerializationException(
            "Cannot convert value to SerializableType: " + value);
    }

    protected isPrimitive(value: any): boolean
    {
        return value == null ||
            typeof value === "string" ||
            typeof value === "number" ||
            typeof value === "boolean" ||
            value instanceof Date;
    }

    protected isArray(value: any): boolean
    {
        return Array.isArray(value);
    }

    protected isMap(value: any): boolean
    {
        const arrayCheck: boolean =  Array.isArray(value);
        const entryCheck: boolean = value.every(
            item => Array.isArray(item) && item.length === 2);
        return arrayCheck && entryCheck;
    }

    protected isSerializable(value: any): boolean
    {
        return value != null && typeof value === "object";
    }
}

class BasicObjectReader
    extends AbstractObjectReader
{
    constructor(source: any)
    {
        super(source);
    }

}