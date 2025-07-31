import "jest"
import {Optional}  from "strata.foundation.core/Utility";
import {Holder, NoSuchElementException} from "strata.foundation.core/Utility";

describe(
    "Optional",
    () =>
    {
        it(
            "testGet",
            () =>
            {
                const presentIsTrue: Optional<string> = Optional.of("optional is present");
                const presentIsFalse: Optional<string> = Optional.empty();
                const expected: string = "optional is present";

                expect(presentIsTrue.get()).toBe(expected);

                try
                {
                    presentIsFalse.get();
                    fail("should have thrown exception");
                }
                catch (e)
                {
                    expect(e instanceof NoSuchElementException).toBeTruthy();
                }
            });
        it(
            "testIfPresent",
            () =>
            {
                const presentIsTrue: Optional<string> = Optional.of("optional is present");
                const presentIsFalse: Optional<string> = Optional.empty();
                const expected: string = "optional is present";

                expect(presentIsTrue.isPresent()).toBeTruthy();
                expect(presentIsFalse.isPresent()).toBeFalsy();
                expect(presentIsTrue.isEmpty()).toBeFalsy();
                expect(presentIsFalse.isEmpty()).toBeTruthy();

                presentIsTrue
                    .ifPresent(actual => expect(actual).toBe(expected));

                presentIsFalse
                    .ifPresent(actual => {throw new Error(actual);});

            });
        it(
            "testIfPresentOrElse",
            () =>
            {
                const presentIsTrue: Optional<string> = Optional.of("optional is present");
                const presentIsFalse: Optional<string> = Optional.empty();
                const expectedPresent: string = "optional is present";
                const expectedNotPresent: string = "optional is not present";

                expect(
                    presentIsTrue
                        .ifPresentOrElse(
                            actual => actual,
                            () => expectedNotPresent))
                    .toBe(expectedPresent);

                expect(
                    presentIsFalse
                        .ifPresentOrElse(
                            actual => expectedPresent,
                            () => expectedNotPresent))
                    .toBe(expectedNotPresent);
            });
        it(
            "testIfPresentOrElseNoReturn",
            () =>
            {
                const presentIsTrue: Optional<string> = Optional.of("optional is present");
                const presentIsFalse: Optional<string> = Optional.empty();
                const expectedPresent: string = "optional is present";

                presentIsTrue
                    .ifPresentOrElseNoReturn(
                        actual => expect(actual).toBe(expectedPresent),
                        () => {throw new NoSuchElementException("");});

                presentIsFalse
                    .ifPresentOrElseNoReturn(
                        actual => {throw new Error(actual);},
                        () => {});
            });
        it(
            "testOr",
            () =>
            {
                const presentIsTrue: Optional<string> = Optional.of("expected value A");
                const presentIsFalse: Optional<string> = Optional.empty();
                const expectedA: string = "expected value A";
                const expectedB: string = "expected value B";

                presentIsTrue
                    .or(() => Optional.of(expectedB))
                    .ifPresent(actual => expect(actual).toBe(expectedA));

                presentIsFalse
                    .or(() => Optional.of(expectedB))
                    .ifPresent(actual => expect(actual).toBe(expectedB));
            });
        it(
            "testOrElse",
            () =>
            {
                const presentIsTrue: Optional<string> = Optional.of("expected value A");
                const presentIsFalse: Optional<string> = Optional.empty();
                const expectedA: string = "expected value A";
                const expectedB: string = "expected value B";

                expect(presentIsTrue.orElse(expectedB)).toBe(expectedA);
                expect(presentIsFalse.orElse(expectedB)).toBe(expectedB);
            });
        it(
            "testOrElseGet",
            () =>
            {
                const presentIsTrue: Optional<string> = Optional.of("expected value A");
                const presentIsFalse: Optional<string> = Optional.empty();
                const expectedA: string = "expected value A";
                const expectedB: string = "expected value B";

                expect(presentIsTrue.orElseGet(() => expectedB)).toBe(expectedA);
                expect(presentIsFalse.orElseGet(() => expectedB)).toBe(expectedB);
            });
        it(
            "testOrElseThrow",
            () =>
            {
                const presentIsTrue: Optional<string> = Optional.of("expected value A");
                const presentIsFalse: Optional<string> = Optional.empty();
                const expectedA: string = "expected value A";
                const expectedB: string = "expected value B";

                expect(presentIsTrue.orElseThrow(new Error(expectedA))).toBe(expectedA);

                try
                {
                    expect(presentIsFalse.orElseThrow(new Error(expectedA))).toBe(expectedB);
                    fail("should have thrown exception");
                }
                catch (e) {}

            });
        it(
            "testOrElseGetThrow",
            () =>
            {
                const presentIsTrue: Optional<string> = Optional.of("expected value A");
                const presentIsFalse: Optional<string> = Optional.empty();
                const expectedA: string = "expected value A";
                const expectedB: string = "expected value B";

                expect(presentIsTrue.orElseGetThrow(() => new Error(expectedA))).toBe(expectedA);

                try
                {
                    expect(presentIsFalse.orElseGetThrow(() => new Error(expectedA))).toBe(expectedB);
                    fail("should have thrown exception");
                }
                catch (e) {}

            });
        it(
            "testFilter",
            () =>
            {
                const presentIsTrue: Optional<string> = Optional.of("optional is present");
                const presentIsFalse: Optional<string> = Optional.empty();
                const expected: string = "optional is present";

                expect(
                    presentIsTrue
                        .filter(actual => actual === expected)
                        .get())
                    .toBe(expected);

                try
                {
                    presentIsTrue
                        .filter(actual => actual !== expected)
                        .get();
                    fail("should have thrown exception");
                }
                catch (e) {}

                expect(presentIsFalse.filter(actual => actual === expected).isEmpty()).toBeTruthy();
            });
        it(
            "testMap",
            () =>
            {
                const presentIsTrue: Optional<string> = Optional.of("optional is present");
                const presentIsFalse: Optional<string> = Optional.empty();
                const expected: string = "optional is present";

                expect(
                    presentIsTrue
                        .map(actual => actual.toUpperCase())
                        .get())
                    .toBe(expected.toUpperCase());

                try
                {
                    presentIsFalse
                        .map(actual => actual.toUpperCase())
                        .get();
                    fail("should have thrown exception");
                }
                catch (e) {}
            });
        it(
            "testFlatMap",
            () =>
            {
                const presentIsTrue: Optional<string> = Optional.of("optional is present");
                const presentIsFalse: Optional<string> = Optional.empty();
                const expected: string = "optional is present";

                expect(
                    presentIsTrue
                        .flatMap(actual => Optional.of(actual.toUpperCase()))
                        .get())
                    .toBe(expected.toUpperCase());

                try
                {
                    presentIsFalse
                        .flatMap(actual => Optional.of(actual.toUpperCase()))
                        .get();
                    fail("should have thrown exception");
                }
                catch (e) {}
            });
        it(
            "testOfNullable",
            () =>
            {
                const presentIsTrue: Optional<string> = Optional.ofNullable("optional is present");
                const presentIsFalse1: Optional<string> = Optional.ofNullable(null);
                const presentIsFalse2: Optional<string> = Optional.ofNullable(undefined);
                const expected: string = "optional is present";

                expect(presentIsTrue.isPresent()).toBeTruthy();
                expect(presentIsFalse1.isPresent()).toBeFalsy();
                expect(presentIsFalse2.isPresent()).toBeFalsy();
                expect(presentIsTrue.isEmpty()).toBeFalsy();
                expect(presentIsFalse1.isEmpty()).toBeTruthy();
                expect(presentIsFalse2.isEmpty()).toBeTruthy();

                presentIsTrue
                    .ifPresent(actual => expect(actual).toBe(expected));

                presentIsFalse1
                    .ifPresent(actual => {throw new Error(actual);});

                presentIsFalse2
                    .ifPresent(actual => {throw new Error(actual);});

            });

    });