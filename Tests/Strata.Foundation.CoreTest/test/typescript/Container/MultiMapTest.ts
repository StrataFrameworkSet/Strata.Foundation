import "jest"
import {IMultiMap} from 'strata.foundation.core';
import {MultiMap} from 'strata.foundation.core';

describe(
    'MultiMap.put',
    () =>
    {
        it(
            'element put into MultiMap',
            () =>
            {
                let target: IMultiMap<string,string> = new MultiMap<string,string>();

                target
                    .put("foo","Foo")
                    .put("bar","Bar");

                expect(target.containsKey("foo")).toBe(true);
                expect(target.containsKey("bar")).toBe(true);
                expect(target.getCardinality("foo")).toBe(1);
                expect(target.getCardinality("bar")).toBe(1);
                expect(target.getAt("foo",0)).toBe("Foo");
                expect(target.getAt("bar",0)).toBe("Bar");
            });
    });

describe(
    'MultiMap.put',
    () =>
    {
        it(
            'elements put into MultiMap',
            () =>
            {
                let target: IMultiMap<string,string> = new MultiMap<string,string>();

                target
                    .put("foobar","Foo")
                    .put("foobar","Bar");

                expect(target.containsKey("foobar")).toBe(true);
                expect(target.getCardinality("foobar")).toBe(2);
                expect(target.getAt("foobar",0)).toBe("Foo");
                expect(target.getAt("foobar",1)).toBe("Bar");
            });
    });

describe(
    'MultiMap.put',
    () =>
    {
        it(
            'multiple elements with same key put into MultiMap',
            () =>
            {
                let actual: string = "foobar";

                expect(actual).toBe("foobar");
                //let target: IMultiMap<string,string> = new MultiMap<string,string>();

                //let result = Calculator.Sum(5, 2);
                //expect(result).equal(7);
            });
    });