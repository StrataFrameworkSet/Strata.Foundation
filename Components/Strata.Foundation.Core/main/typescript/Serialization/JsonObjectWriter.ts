import {AbstractObjectWriter} from "./AbstractObjectWriter";
import {ISerializable} from "./ISerializable";

export
class JsonObjectWriter
    extends AbstractObjectWriter
{
    public constructor()
    {
        super();
    }

    write<T extends ISerializable>(value: T): JsonObjectWriter
    {
        return super.write(value) as JsonObjectWriter;
    }

    public toJson(): string
    {
        return JSON.stringify(this.getRoot());
    }
}