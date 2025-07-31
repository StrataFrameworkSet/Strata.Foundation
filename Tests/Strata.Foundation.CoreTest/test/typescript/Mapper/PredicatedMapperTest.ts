import "jest"
import {Optional}  from "strata.foundation.core/Utility";
import {PredicatedMapper} from "strata.foundation.core/Mapper";

describe(
    "PredicatedMapperTest",
    () =>
    {
        it(
            "testGet",
            () =>
            {
                const subject: PredicatedMapper<string,string> =
                    PredicatedMapper
                        .of<string,string>("input X")
                        .addMapping(
                            (input) => input == "input A",
                            (input) => "mapped A")
                        .addMapping(
                            (input) => input == "input B",
                            (input) => "mapped B")
                        .addMapping(
                            (input) => input == "input X",
                            (input) => "mapped X")
                        .addMapping(
                            (input) => input == "input C",
                            (input) => "mapped C");

                const expected: string = "mapped X";
                const actual: Optional<string> = subject.get();

                expect(actual.isPresent()).toBeTruthy();
                expect(actual.get()).toBe(expected);

            });
        it(
            "testGetWhenEmpty",
            () =>
            {
                const subject: PredicatedMapper<string,string> =
                    PredicatedMapper
                        .of<string,string>("input X")
                        .addMapping(
                            (input) => input == "input A",
                            (input) => "mapped A")
                        .addMapping(
                            (input) => input == "input B",
                            (input) => "mapped B")
                        .addMapping(
                            (input) => input == "input C",
                            (input) => "mapped C");

                const expected: string = "mapped X";
                const actual: Optional<string> = subject.get();

                expect(actual.isPresent()).toBeFalsy();
            });
        it(
            "testMapInputFromConstructor",
            () =>
            {
                const subject: PredicatedMapper<string,string> =
                    PredicatedMapper
                        .of<string,string>("input X")
                        .addMapping(
                            (input) => input == "input A",
                            () => "mapped A")
                        .addMapping(
                            (input) => input == "input B",
                            () => "mapped B")
                        .addMapping(
                            (input) => input == "input X",
                            () => "mapped X")
                        .addMapping(
                            (input) => input == "input C",
                            () => "mapped C");

                const expected: string = "mapped X";
                const actual: Optional<string> = subject.map();

                expect(actual.isPresent()).toBeTruthy();
                expect(actual.get()).toBe(expected);

            });
        it(
            "testMapWhenEmptyInputFromConstructor",
            () =>
            {
                const subject: PredicatedMapper<string,string> =
                    PredicatedMapper
                        .of<string,string>("input X")
                        .addMapping(
                            (input) => input == "input A",
                            () => "mapped A")
                        .addMapping(
                            (input) => input == "input B",
                            () => "mapped B")
                        .addMapping(
                            (input) => input == "input C",
                            () => "mapped C");

                const expected: string = "mapped X";
                const actual: Optional<string> = subject.map();

                expect(actual.isPresent()).toBeFalsy();
            });
        it(
            "testMap",
            () =>
            {
                const subject: PredicatedMapper<string,string> =
                    PredicatedMapper
                        .of<string,string>()
                        .addMapping(
                            (input) => input == "input A",
                            () => "mapped A")
                        .addMapping(
                            (input) => input == "input B",
                            () => "mapped B")
                        .addMapping(
                            (input) => input == "input X",
                            () => "mapped X")
                        .addMapping(
                            (input) => input == "input C",
                            () => "mapped C");

                const expected: string = "mapped X";
                const actual: Optional<string> = subject.map("input X");

                expect(actual.isPresent()).toBeTruthy();
                expect(actual.get()).toBe(expected);

            });
        it(
            "testMapWhenEmpty",
            () =>
            {
                const subject: PredicatedMapper<string,string> =
                    PredicatedMapper
                        .of<string,string>()
                        .addMapping(
                            (input) => input == "input A",
                            () => "mapped A")
                        .addMapping(
                            (input) => input == "input B",
                            () => "mapped B")
                        .addMapping(
                            (input) => input == "input C",
                            () => "mapped C");

                const expected: string = "mapped X";
                const actual: Optional<string> = subject.map("input X");

                expect(actual.isPresent()).toBeFalsy();
            });

    });