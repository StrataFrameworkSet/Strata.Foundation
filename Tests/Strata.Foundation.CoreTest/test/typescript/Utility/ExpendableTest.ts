import "jest"
import {Expendable} from "strata.foundation.core/Utility";
import {NoSuchElementException} from "strata.foundation.core/Utility";

describe(
    "Expendable",
    () =>
    {
        it(
            "testConstructors",
            () =>
            {
                const subject1: Expendable<string> = new Expendable<string>();
                const subject2: Expendable<string> = new Expendable<string>("subject2");
                const subject3: Expendable<string> = new Expendable<string>("subject3", 5);

                expect(subject1.getAllowed()).toBe(0);
                expect(subject1.getRemaining()).toBe(0);
                expect(subject1.isPresent()).toBeFalsy();
                expect(subject1.isEmpty()).toBeTruthy();

                try
                {
                    subject1.get();
                    fail("should have thrown exception");
                }
                catch (e)
                {
                    expect(e instanceof NoSuchElementException).toBeTruthy();
                }

                expect(subject2.getAllowed()).toBe(1);
                expect(subject2.getRemaining()).toBe(1);
                expect(subject2.isPresent()).toBeTruthy();
                expect(subject2.get()).toBe("subject2");
                expect(subject2.getRemaining()).toBe(0);
                expect(subject2.isPresent()).toBeFalsy();
                expect(subject2.isEmpty()).toBeTruthy();


            });
        it(
            "testOfAndEmpty",
            () =>
            {
                const subject1: Expendable<string> = Expendable.empty();
                const subject2: Expendable<string> = Expendable.of("subject2");
                const subject3: Expendable<string> = Expendable.of("subject3", 5);

                expect(subject1.getAllowed()).toBe(0);
                expect(subject1.getRemaining()).toBe(0);
                expect(subject1.isPresent()).toBeFalsy();
                expect(subject1.isEmpty()).toBeTruthy();

                try
                {
                    subject1.get();
                    fail("should have thrown exception");
                }
                catch (e)
                {
                    expect(e instanceof NoSuchElementException).toBeTruthy();
                }

                expect(subject2.getAllowed()).toBe(1);
                expect(subject2.getRemaining()).toBe(1);
                expect(subject2.isPresent()).toBeTruthy();
                expect(subject2.get()).toBe("subject2");
                expect(subject2.getRemaining()).toBe(0);
                expect(subject2.isPresent()).toBeFalsy();
                expect(subject2.isEmpty()).toBeTruthy();
            });
        it(
            "testIfPresent",
            () =>
            {
                const subject1: Expendable<string> = Expendable.empty();
                const subject2: Expendable<string> = Expendable.of("subject2");
                const subject3: Expendable<string> = Expendable.of("subject3", 5);

                expect(subject1.getAllowed()).toBe(0);
                expect(subject1.getRemaining()).toBe(0);
                expect(subject1.isPresent()).toBeFalsy();
                expect(subject1.isEmpty()).toBeTruthy();
                subject1.ifPresent(actual => fail("expected subject 1 to be empty"));

                expect(subject2.getAllowed()).toBe(1);
                expect(subject2.getRemaining()).toBe(1);
                expect(subject2.isPresent()).toBeTruthy();
                subject2.ifPresent(actual => expect(actual).toBe("subject2"));
                expect(subject2.getRemaining()).toBe(0);
                expect(subject2.isPresent()).toBeFalsy();
                expect(subject2.isEmpty()).toBeTruthy();
                subject2.ifPresent(actual => fail("expected subject 2 to be empty"));

                for (let i: number = 0; i < 5; ++i)
                {
                    console.log("i = " + i);
                    expect(subject3.isPresent()).toBeTruthy();
                    expect(subject3.isEmpty()).toBeFalsy();
                    expect(subject3.getRemaining()).toBe(5 - i);
                    subject3.ifPresent(actual => expect(actual).toBe("subject3"));
                }

                expect(subject3.isPresent()).toBeFalsy();
                expect(subject3.isEmpty()).toBeTruthy();
                expect(subject3.getRemaining()).toBe(0);
                subject3.ifPresent(actual => fail("expected subject 3 to be empty"));
            });
        it(
            "testIfPresentOrElse",
            () =>
            {
                const subject1: Expendable<string> = Expendable.empty();
                const subject2: Expendable<string> = Expendable.of("subject2");
                const subject3: Expendable<string> = Expendable.of("subject3", 5);

                expect(subject1.getAllowed()).toBe(0);
                expect(subject1.getRemaining()).toBe(0);
                expect(subject1.isPresent()).toBeFalsy();
                expect(subject1.isEmpty()).toBeTruthy();
                expect(
                    subject1.ifPresentOrElse(
                        value => value,
                        () => "subject1 is empty"))
                    .toBe("subject1 is empty");

                expect(subject2.getAllowed()).toBe(1);
                expect(subject2.getRemaining()).toBe(1);
                expect(subject2.isPresent()).toBeTruthy();
                expect(
                    subject2.ifPresentOrElse(
                        actual => actual,
                        () => "subject2 is empty"))
                    .toBe("subject2");
                expect(subject2.getRemaining()).toBe(0);
                expect(subject2.isPresent()).toBeFalsy();
                expect(subject2.isEmpty()).toBeTruthy();
                expect(
                    subject2.ifPresentOrElse(
                        actual => actual,
                        () => "subject2 is empty"))
                    .toBe("subject2 is empty");

                for (let i: number = 0; i < 5; ++i)
                {
                    console.log("i = " + i);
                    expect(subject3.isPresent()).toBeTruthy();
                    expect(subject3.isEmpty()).toBeFalsy();
                    expect(subject3.getRemaining()).toBe(5 - i);
                    expect(
                        subject3.ifPresentOrElse(
                            actual => actual,
                            () => "subject3 is empty"))
                        .toBe("subject3");
                }

                expect(subject3.isPresent()).toBeFalsy();
                expect(subject3.isEmpty()).toBeTruthy();
                expect(subject3.getRemaining()).toBe(0);
                expect(
                    subject3.ifPresentOrElse(
                        actual => actual,
                        () => "subject3 is empty"))
                    .toBe("subject3 is empty");
            });
        it(
            "testIfPresentOrThrow",
            () =>
            {
                const subject1: Expendable<string> = Expendable.empty();
                const subject2: Expendable<string> = Expendable.of("subject2");
                const subject3: Expendable<string> = Expendable.of("subject3", 5);

                expect(subject1.getAllowed()).toBe(0);
                expect(subject1.getRemaining()).toBe(0);
                expect(subject1.isPresent()).toBeFalsy();
                expect(subject1.isEmpty()).toBeTruthy();
                expect(
                    () => subject1.ifPresentOrThrow(
                        value => value,
                        new NoSuchElementException("subject1 is empty"))
                    .toThrow(new NoSuchElementException("subject1 is empty")));

                expect(subject2.getAllowed()).toBe(1);
                expect(subject2.getRemaining()).toBe(1);
                expect(subject2.isPresent()).toBeTruthy();
                expect(
                    subject2.ifPresentOrThrow(
                        actual => actual,
                        new NoSuchElementException("subject2 is empty")))
                    .toBe("subject2");
                expect(subject2.getRemaining()).toBe(0);
                expect(subject2.isPresent()).toBeFalsy();
                expect(subject2.isEmpty()).toBeTruthy();
                expect(
                    () => subject2.ifPresentOrThrow(
                        actual => actual,
                        new NoSuchElementException("subject2 is empty")))
                    .toThrow(new NoSuchElementException("subject2 is empty"));

                for (let i: number = 0; i < 5; ++i)
                {
                    console.log("i = " + i);
                    expect(subject3.isPresent()).toBeTruthy();
                    expect(subject3.isEmpty()).toBeFalsy();
                    expect(subject3.getRemaining()).toBe(5 - i);
                    expect(
                        subject3.ifPresentOrThrow(
                            actual => actual,
                            new NoSuchElementException("subject3 is empty")))
                        .toBe("subject3");
                }

                expect(subject3.isPresent()).toBeFalsy();
                expect(subject3.isEmpty()).toBeTruthy();
                expect(subject3.getRemaining()).toBe(0);
                expect(
                    () => subject3.ifPresentOrThrow(
                        actual => actual,
                        new NoSuchElementException("subject3 is empty")))
                    .toThrow(new NoSuchElementException("subject3 is empty"));
            });
        it(
            "testIfPresentOrElseNoReturn",
            () =>
            {
                const subject1: Expendable<string> = Expendable.empty();
                const subject2: Expendable<string> = Expendable.of("subject2");
                const subject3: Expendable<string> = Expendable.of("subject3", 5);

                expect(subject1.getAllowed()).toBe(0);
                expect(subject1.getRemaining()).toBe(0);
                expect(subject1.isPresent()).toBeFalsy();
                expect(subject1.isEmpty()).toBeTruthy();
                subject1.ifPresentOrElseNoReturn(
                        value => fail("expected subject 1 to be empty"),
                        () => console.log("subject1 is empty"));

                expect(subject2.getAllowed()).toBe(1);
                expect(subject2.getRemaining()).toBe(1);
                expect(subject2.isPresent()).toBeTruthy();
                subject2.ifPresentOrElseNoReturn(
                    actual => expect(actual).toBe("subject2"),
                    () => fail("subject2 is empty"));

                expect(subject2.getRemaining()).toBe(0);
                expect(subject2.isPresent()).toBeFalsy();
                expect(subject2.isEmpty()).toBeTruthy();
                subject2.ifPresentOrElseNoReturn(
                    actual => fail("subject2 should be empty"),
                    () => console.log("subject2 is empty"));

                for (let i: number = 0; i < 5; ++i)
                {
                    console.log("i = " + i);
                    expect(subject3.isPresent()).toBeTruthy();
                    expect(subject3.isEmpty()).toBeFalsy();
                    expect(subject3.getRemaining()).toBe(5 - i);
                    subject3.ifPresentOrElseNoReturn(
                        actual => expect(actual).toBe("subject3"),
                        () => fail("subject3 is empty"));
                }

                expect(subject3.isPresent()).toBeFalsy();
                expect(subject3.isEmpty()).toBeTruthy();
                expect(subject3.getRemaining()).toBe(0);
                subject3.ifPresentOrElseNoReturn(
                    actual => fail("subject3 should be empty"),
                    () => console.log("subject3 is empty"));
            });
        it(
            "testIfPresentOrThrowNoReturn",
            () =>
            {
                const subject1: Expendable<string> = Expendable.empty();
                const subject2: Expendable<string> = Expendable.of("subject2");
                const subject3: Expendable<string> = Expendable.of("subject3", 5);

                expect(subject1.getAllowed()).toBe(0);
                expect(subject1.getRemaining()).toBe(0);
                expect(subject1.isPresent()).toBeFalsy();
                expect(subject1.isEmpty()).toBeTruthy();
                expect(
                    () => subject1.ifPresentOrThrowNoReturn(
                        value => value,
                        new NoSuchElementException("subject1 is empty")))
                    .toThrow(new NoSuchElementException("subject1 is empty"));

                expect(subject2.getAllowed()).toBe(1);
                expect(subject2.getRemaining()).toBe(1);
                expect(subject2.isPresent()).toBeTruthy();
                subject2.ifPresentOrThrowNoReturn(
                    actual => expect(actual).toBe("subject2"),
                    new NoSuchElementException("subject2 is empty"));
                expect(subject2.getRemaining()).toBe(0);
                expect(subject2.isPresent()).toBeFalsy();
                expect(subject2.isEmpty()).toBeTruthy();
                expect(
                    () => subject2.ifPresentOrThrowNoReturn(
                        actual => actual,
                        new NoSuchElementException("subject2 is empty")))
                    .toThrow(new NoSuchElementException("subject2 is empty"));

                for (let i: number = 0; i < 5; ++i)
                {
                    console.log("i = " + i);
                    expect(subject3.isPresent()).toBeTruthy();
                    expect(subject3.isEmpty()).toBeFalsy();
                    expect(subject3.getRemaining()).toBe(5 - i);
                    subject3.ifPresentOrThrowNoReturn(
                        actual => expect(actual).toBe("subject3"),
                        new NoSuchElementException("subject3 is empty"));
                }

                expect(subject3.isPresent()).toBeFalsy();
                expect(subject3.isEmpty()).toBeTruthy();
                expect(subject3.getRemaining()).toBe(0);
                expect(
                    () => subject3.ifPresentOrThrowNoReturn(
                        actual => actual,
                        new NoSuchElementException("subject3 is empty")))
                    .toThrow(new NoSuchElementException("subject3 is empty"));
            });
        it(
            "testFilter",
            () =>
            {
                const subject1: Expendable<string> = Expendable.empty();
                const subject2: Expendable<string> = Expendable.of("subject2");
                const subject3: Expendable<string> = Expendable.of("subject3", 5);

                expect(subject1.filter(value => value === "subject1").isEmpty()).toBeTruthy();
                expect(subject2.filter(value => value === "subject1").isEmpty()).toBeTruthy();
                expect(subject2.filter(value => value === "subject2").isPresent()).toBeTruthy();
                expect(subject2.filter(value => value === "subject2").get()).toBe("subject2");

                for (let i: number = 0; i < 5; ++i)
                {
                    console.log("i = " + i);
                    expect(subject3.filter(value => value === "subject1").isEmpty()).toBeTruthy();
                    expect(subject3.filter(value => value === "subject3").isPresent()).toBeTruthy();
                    expect(subject3.filter(value => value === "subject3").get()).toBe("subject3");
                }

            });

        it(
            "testMap",
            () =>
            {
                const subject1: Expendable<string> = Expendable.empty();
                const subject2: Expendable<string> = Expendable.of("subject2");
                const subject3: Expendable<string> = Expendable.of("subject3", 5);

                expect(subject1.map(value => value.toUpperCase()).isEmpty()).toBeTruthy();
                expect(subject2.map(value => value.toUpperCase()).get()).toBe("SUBJECT2");

                for (let i: number = 0; i < 5; ++i)
                {
                    console.log("i = " + i);
                    expect(subject3.map(value => value.toUpperCase()).get()).toBe("SUBJECT3");
                }

                expect(subject3.isEmpty()).toBeTruthy();
            });
        it(
            "testFlatMap",
            () =>
            {
                const subject1: Expendable<string> = Expendable.empty();
                const subject2: Expendable<string> = Expendable.of("subject2");
                const subject3: Expendable<string> = Expendable.of("subject3", 5);

                expect(subject1.flatMap(value => Expendable.of(value.toUpperCase())).isEmpty()).toBeTruthy();
                expect(subject2.flatMap(value => Expendable.of(value.toUpperCase())).get()).toBe("SUBJECT2");
                expect(subject2.isEmpty()).toBeTruthy();

                for (let i: number = 0; i < 5; ++i)
                {
                    console.log("i = " + i);
                    expect(subject3.flatMap(value => Expendable.of(value.toUpperCase())).get()).toBe("SUBJECT3");
                }

                expect(subject3.isEmpty()).toBeTruthy();
            });
        it(
            "testOrElse",
            () =>
            {
                const subject1: Expendable<string> = Expendable.empty();
                const subject2: Expendable<string> = Expendable.of("subject2");
                const subject3: Expendable<string> = Expendable.of("subject3", 5);

                expect(subject1.orElse("default")).toBe("default");
                expect(subject2.orElse("default")).toBe("subject2");
                expect(subject2.orElse("default")).toBe("default");

                for (let i: number = 0; i < 5; ++i)
                {
                    console.log("i = " + i);
                    expect(subject3.orElse("default")).toBe("subject3");
                }

                expect(subject3.orElse("default")).toBe("default");
            });
        it(
            "testOrElseGet",
            () =>
            {
                const subject1: Expendable<string> = Expendable.empty();
                const subject2: Expendable<string> = Expendable.of("subject2");
                const subject3: Expendable<string> = Expendable.of("subject3", 5);

                expect(subject1.orElseGet(() => "default")).toBe("default");
                expect(subject2.orElseGet(() => "default")).toBe("subject2");
                expect(subject2.orElseGet(() => "default")).toBe("default");

                for (let i: number = 0; i < 5; ++i)
                {
                    console.log("i = " + i);
                    expect(subject3.orElseGet(() => "default")).toBe("subject3");
                }

                expect(subject3.orElseGet(() => "default")).toBe("default");
            });
        it(
            "testOrElseThrow",
            () =>
            {
                const subject1: Expendable<string> = Expendable.empty();
                const subject2: Expendable<string> = Expendable.of("subject2");
                const subject3: Expendable<string> = Expendable.of("subject3", 5);

                expect(() => subject1.orElseThrow(new NoSuchElementException("subject1 is empty")))
                    .toThrow(new NoSuchElementException("subject1 is empty"));
                expect(subject2.orElseThrow(new NoSuchElementException("subject2 is empty"))).toBe("subject2");
                expect(() => subject2.orElseThrow(new NoSuchElementException("subject2 is empty")))
                    .toThrow(new NoSuchElementException("subject2 is empty"));

                for (let i: number = 0; i < 5; ++i)
                {
                    console.log("i = " + i);
                    expect(subject3.orElseThrow(new NoSuchElementException("subject3 is empty"))).toBe("subject3");
                }

                expect(() => subject3.orElseThrow(new NoSuchElementException("subject3 is empty")))
                    .toThrow(new NoSuchElementException("subject3 is empty"));
            });
        it(
            "isExpended",
            () =>
            {
                const subject1: Expendable<string> = Expendable.empty();
                const subject2: Expendable<string> = Expendable.of("subject2");
                const subject3: Expendable<string> = Expendable.of("subject3", 5);

                expect(subject1.isExpended()).toBeTruthy();
                expect(subject2.isExpended()).toBeFalsy();

                subject2.get();
                expect(subject2.isExpended()).toBeTruthy();

                for (let i: number = 0; i < 5; ++i)
                {
                    console.log("i = " + i);
                    expect(subject3.isExpended()).toBeFalsy();
                    subject3.get();
                }

                expect(subject3.isExpended()).toBeTruthy();
            });
    });