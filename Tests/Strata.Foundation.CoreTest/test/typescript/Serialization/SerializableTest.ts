import "jest"
import {FooBar} from "./FooBar";
import {Foo} from "./Foo";
import {
    JsonObjectReader,
    JsonObjectWriter,
    SerializationManager
} from "strata.foundation.core/Serialization";
import {Optional} from "strata.foundation.core/Utility";


describe(
    "SerializableTest",
    () =>
    {
        it(
            "testWriteAndRead",
            () =>
            {
                const expected: FooBar =
                    new FooBar(
                        new Foo(true,3,"this is a string"),
                        ["this is a bar","this too"],
                        new Map(
                            [
                                ["one",new Foo(true,1,"one")],
                                ["two",new Foo(false,2,"two")],
                                ["three",new Foo(true,3,"three")]]));
                const actualJson: string =
                    new JsonObjectWriter()
                        .write(expected)
                        .toJson();
                console.log("actual json = " + actualJson);

                const actual: FooBar = new JsonObjectReader(actualJson).read<FooBar>();

                expect(actual.getFoo().getValueA()).toBe(expected.getFoo().getValueA());
                expect(actual.getFoo().getValueB()).toBe(expected.getFoo().getValueB());
                expect(actual.getFoo().getValueC()).toStrictEqual(expected.getFoo().getValueC());
                expect(actual.getBar()).toStrictEqual(expected.getBar());
                expect(actual).toStrictEqual(expected);

            });
        it(
            "testJsonParse",
            () =>
            {
                const json: string =
                    `{
                        "typename": "FooBar",
                        "foo": {
                            "type": "Foo",
                            "a": true,
                            "b": 3,
                            "c": "this is a string"
                        },
                        "bar": "this is a bar"
                    }`;
                const actual: any = JSON.parse(json);

                console.log("debug 1: " + actual);
                console.log("debug 2: " + Object.keys(actual));
                console.log("debug 3: actual['typename'] = " + actual["typename"]);
            });
        it(
            "testMapToJson",
            () =>
            {
                const map: Map<string,number> = new Map<string,number>();

                map.set("one",1);
                map.set("two",2);
                map.set("three",3);

                const json: string = JSON.stringify(Array.from(map.entries()));
                console.log(json);

                const parsedMap: Map<string,number> =
                    new Map<string,number>(JSON.parse(json));
                expect(parsedMap.get("one")).toBe(1);
                expect(parsedMap.get("two")).toBe(2);
                expect(parsedMap.get("three")).toBe(3);

                console.log("initial parsed output = " + JSON.parse(json));
            });
        it(
            "testSerializationManagerGetTypeName",
            () =>
            {
                const foo: Foo = new Foo(true,3,"this is a string");
                const fooBar: FooBar =
                    new FooBar(
                        foo,
                        ["this is a bar","this too"],
                        new Map(
                            [
                                ["one",new Foo(true,1,"one")],
                                ["two",new Foo(false,2,"two")],
                                ["three",new Foo(true,3,"three")]]));
                const optional: Optional<FooBar> = Optional.of(fooBar);
                const anyOptional: any = {
                    typename: "Optional",
                    value: "some value"
                }

                expect(SerializationManager.getTypeName(null)).toBe("null");
                expect(SerializationManager.getTypeName(123)).toBe("number");
                expect(SerializationManager.getTypeName("some string")).toBe("string");
                expect(SerializationManager.getTypeName(true)).toBe("boolean");
                expect(SerializationManager.getTypeName(new Date())).toBe("Date");
                expect(SerializationManager.getTypeName(foo)).toBe("Foo");
                expect(SerializationManager.getTypeName(fooBar)).toBe("FooBar");
                expect(SerializationManager.getTypeName(optional)).toBe("Optional");
                expect(SerializationManager.getTypeName(optional.get())).toBe("FooBar");
                expect(SerializationManager.getTypeName(fooBar.getBar())).toBe("Array");
                expect(SerializationManager.getTypeName(fooBar.getBaz())).toBe("Map");
                expect(SerializationManager.getTypeName(anyOptional)).toBe("Optional");
            });
    });

