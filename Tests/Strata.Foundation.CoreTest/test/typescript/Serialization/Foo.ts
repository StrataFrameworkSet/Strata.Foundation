import {ISerializable, SerializationManager} from "strata.foundation.core/Serialization";
import {IObjectWriter} from "strata.foundation.core/Serialization";
import {IObjectReader} from "strata.foundation.core/Serialization";
import {Optional} from "strata.foundation.core/Utility";

export
class Foo
    implements ISerializable
{
    private valueA: boolean;
    private valueB: number;
    private valueC: Optional<string>;

    static
    {
        SerializationManager.register("Foo", () => new Foo());
    }

    public constructor(a?: boolean, b?: number, c?: string)
    {
        if (a === undefined)
            a = false;
        else
            this.valueA = a;

        if (b === undefined)
            b = 0;
        else
            this.valueB = b;

        if (c === undefined)
            c = "";
        else
            this.valueC = Optional.ofNullable(c);

    }

    public writeTo(writer: IObjectWriter): void
    {
        writer.writeBoolean("valueA", this.valueA);
        writer.writeNumber("valueB", this.valueB);
        writer.writeOptional("valueC", this.valueC);
    }

    public readFrom(reader: IObjectReader): void
    {
        this.valueA = reader.readBoolean("valueA");
        this.valueB = reader.readNumber("valueB");
        this.valueC = reader.readOptional("valueC");
    }

    public getTypeName(): string
    {
        return "Foo";
    }

    public getValueA(): boolean
    {
        return this.valueA;
    }

    public getValueB(): number
    {
        return this.valueB;
    }

    public getValueC(): Optional<string>
    {
        return this.valueC;
    }

}