import {ISerializable, SerializationManager} from "strata.foundation.core/Serialization";
import {Foo} from "./Foo";
import {IObjectReader} from "strata.foundation.core/Serialization";
import {IObjectWriter} from "strata.foundation.core/Serialization";

export
class FooBar
    implements ISerializable
{
    private foo: Foo;
    private bar: string[];
    private baz: Map<string,Foo>;

    static
    {
        SerializationManager.register("FooBar", () => new FooBar());
    }

    public constructor(foo?: Foo,bar?: string[], baz?: Map<string,Foo>)
    {
        if (foo === undefined)
            foo = new Foo();
        else
            this.foo = foo;

        if (bar === undefined)
            bar = [];
        else
            this.bar = bar;

        if (baz === undefined)
            baz = new Map<string,Foo>();
        else
            this.baz = baz;
    }

    public writeTo(writer: IObjectWriter): void
    {
        writer.writeObject("foo",this.foo);
        writer.writeArray("bar", this.bar);
        writer.writeMap("baz", this.baz);
    }

    public readFrom(reader: IObjectReader): void
    {
        this.foo = reader.readObject<Foo>("foo");
        this.bar = reader.readArray("bar");
        this.baz = reader.readMap<string,Foo>("baz");
    }

    public getTypeName(): string { return "FooBar"; }

    public getFoo(): Foo {return this.foo; }

    public getBar(): string[] { return this.bar; }

    public getBaz(): Map<string,Foo> { return this.baz; }
}