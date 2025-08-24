import {IObjectWriter} from "./IObjectWriter";
import {ISerializable} from "./ISerializable";
import {SerializationException} from "./SerializationException";
import {SerializableType} from "./SerializableType";
import {Optional} from "../Utility";

export
abstract class AbstractObjectWriter
    implements IObjectWriter
{
    private          stack: any[]
    private          root: any;

    protected constructor()
    {
        this.stack = [];
        this.root = null;
    }

    write<T extends ISerializable>(value: T): AbstractObjectWriter
    {
        if (value == null)
            throw new SerializationException("Value is null");

        this.push({typename: "",value: {}});
        this.writeString("typename", value.getTypeName());
        this.push(this.root["value"]);
        value.writeTo(this);
        this.pop();
        this.pop();
        return this;
    }

    writeBoolean(key: string, value: boolean): void
    {
        this.root[key] = value;
    }

    writeString(key: string, value: string): void
    {
        this.root[key] = value;
    }

    writeNumber(key: string, value: number): void
    {
        this.root[key] = value;
    }

    writeDate(key: string, value: Date): void
    {
        if (value instanceof Date)
            this.root[key] = value.toISOString();
        else
            throw new SerializationException("Value is not a Date instance");
    }

    writeArray<T extends SerializableType>(key: string, values: T[]): void
    {
        this.root[key] =
            values.map(value => this.toPrimitiveOrSimpleObject(value));
    }

    writeMap<K extends SerializableType,V extends SerializableType>(
        key: string,
        values: Map<K,V>): void
    {
        const map: Map<any,any> = new Map<any,any>();

        Array
            .from(values.entries())
            .forEach(
                ([k,v]) => {
                    console.log("writeMap(" + key + "): " + k + " = " + v);
                    map.set(k,this.toPrimitiveOrSimpleObject(v));
                    });

        this.root[key] = Array.from(map.entries());
        console.log("writeMap(" + key + "): map size = " + map.size);
        console.log("writeMap(" + key + "): " + JSON.stringify(map));
    }

    writeObject<T extends ISerializable>(key: string, value: T): void
    {
        if (value != null)
        {
            this.root[key] = {typename: "", value: {}};
            this.push(this.root[key]);
            this.writeString("typename", value.getTypeName());
            this.push(this.root["value"]);
            value.writeTo(this);
            this.pop();
            this.pop();
        }
        else
            this.root[key] = null;
    }

    writeOptional<T extends SerializableType>(key: string, value: Optional<T>)
    {
        if (value != null)
            this.root[key] =
                value.ifPresentOrElse(
                    v => this.toPrimitiveOrSimpleObject(v),
                    () => null);
        else
            this.root[key] = null;
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

    protected getRoot(): any
    {
        return this.root;
    }

    protected toPrimitiveOrSimpleObject(value: SerializableType): any
    {
        if (value == null)
            return null;

        if (typeof value === "string" || typeof value === "number" || typeof value === "boolean")
            return value;

        if (value instanceof Date)
            return value.toISOString();

        if (Array.isArray(value))
            return value.map(v => this.toPrimitiveOrSimpleObject(v));

        if (value instanceof Map)
        {
            const input: Map<SerializableType,SerializableType> =
                value as Map<SerializableType,SerializableType>;
            const output: Map<any,any> = new Map<any,any>();

            Array
                .from(input.entries())
                .forEach(
                    ([k,v]) =>
                        output.set(
                            this.toPrimitiveOrSimpleObject(k),
                            this.toPrimitiveOrSimpleObject(v)));

            return output;
        }

        if (this.isSerializable(value))
            return new BasicObjectWriter().write(value as ISerializable).getRoot();

        throw new SerializationException("Unsupported SerializableType");
    }

    protected isSerializable(value: any): boolean
    {
        return value != null
            && typeof value === "object"
            && "getTypeName" in value
            && typeof value["getTypeName"] === "function"
            && "writeTo" in value
            && typeof value["writeTo"] === "function";
    }
}

class BasicObjectWriter
    extends AbstractObjectWriter
{
    public constructor()
    {
        super();
    }

    write<T extends ISerializable>(value: T): BasicObjectWriter
    {
        return super.write(value) as BasicObjectWriter;
    }
}