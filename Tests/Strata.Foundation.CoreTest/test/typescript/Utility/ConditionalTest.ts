import "jest"
import {Conditional}  from "strata.foundation.core/Utility/Conditional";
import {Holder, NoSuchElementException} from "strata.foundation.core/Utility";

describe(
    "Conditional",
    () =>
    {
        it(
            "testIfTrueSupplier",
            () =>
            {
                const trueCondition: Conditional = Conditional.TRUE;
                const falseCondition: Conditional = Conditional.FALSE;
                const expected: string = "condition is true";

                trueCondition
                    .ifTrue(() => expected)
                    .ifPresentOrElseNoReturn(
                        actual => expect(actual).toBe(expected),
                        () => {throw new NoSuchElementException("");});
                falseCondition
                    .ifTrue(() => expected)
                    .ifPresentOrElseNoReturn(
                        actual => {throw new Error(actual);},
                        () => {});
            });
        it(
            "testIfFalseSupplier",
            () =>
            {
                const trueCondition: Conditional = Conditional.TRUE;
                const falseCondition: Conditional = Conditional.FALSE;
                const expected: string = "condition is true";

                falseCondition
                    .ifFalse(() => expected)
                    .ifPresentOrElseNoReturn(
                        actual => expect(actual).toBe(expected),
                        () => {throw new NoSuchElementException("");});
                trueCondition
                    .ifFalse(() => expected)
                    .ifPresentOrElseNoReturn(
                        actual => {throw new Error(actual);},
                        () => {});
            });
        it(
            "testIfTrueRunnable",
            () =>
            {
                const trueCondition: Conditional = Conditional.TRUE;
                const falseCondition: Conditional = Conditional.FALSE;
                const expected: string = "condition is true";
                const actual: Holder<string> = new Holder<string>();

                trueCondition
                    .ifTrueNoReturn(() => actual.accept(expected));

                expect(actual.get()).toBe(expected);

                falseCondition
                    .ifTrueNoReturn(() => {throw new Error("should not execute")});
            });
        it(
            "testIfFalseRunnable",
            () =>
            {
                const trueCondition: Conditional = Conditional.TRUE;
                const falseCondition: Conditional = Conditional.FALSE;
                const expected: string = "condition is true";
                const actual: Holder<string> = new Holder<string>();

                falseCondition
                    .ifFalseNoReturn(() => actual.accept(expected));

                expect(actual.get()).toBe(expected);

                trueCondition
                    .ifFalseNoReturn(() => {throw new Error("should not execute")});
            });
        it(
            "testIfTrueOrElseSupplier",
            () =>
            {
                const trueCondition: Conditional = Conditional.TRUE;
                const falseCondition: Conditional = Conditional.FALSE;
                const expected: string = "condition is true";
                const error: string = "error";

                expect(
                    trueCondition
                        .ifTrueOrElse(() => expected,() => error))
                    .toBe(expected);

                expect(
                    falseCondition
                        .ifTrueOrElse(() => error,() => expected))
                    .toBe(expected);

            });
        it(
            "testIfTrueOrElseRunnable",
            () =>
            {
                const trueCondition: Conditional = Conditional.TRUE;
                const falseCondition: Conditional = Conditional.FALSE;

                trueCondition.ifTrueOrElseNoReturn(
                    () => {},
                    () => {throw new Error();});

                falseCondition.ifTrueOrElseNoReturn(
                    () => {throw new Error();},
                    () => {});
            });
        it(
            "testIfTrueOrThrowSupplier",
            () =>
            {
                const trueCondition: Conditional = Conditional.TRUE;
                const falseCondition: Conditional = Conditional.FALSE;
                const expected: string = "condition is true";

                expect(
                    trueCondition
                        .ifTrueOrThrow(() => expected,new Error()))
                    .toBe(expected);

                try
                {
                    falseCondition
                        .ifTrueOrThrow(() => expect, new Error());

                    fail("should have thrown exception");
                }
                catch (e) {}
            });
        it(
            "testIfTrueOrThrowRunnable",
            () =>
            {
                const trueCondition: Conditional = Conditional.TRUE;
                const falseCondition: Conditional = Conditional.FALSE;

                trueCondition.ifTrueOrThrowNoReturn(
                    () => {},
                    new Error());

                try
                {
                    falseCondition.ifTrueOrThrowNoReturn(
                        () => {
                        },
                        new Error());

                    fail("should have thrown exception");
                }
                catch (e) {}
            });
        it(
            "testAnd",
            () =>
            {
                const andTrue: Conditional   = Conditional.TRUE.and(Conditional.TRUE);
                const andFalse1: Conditional = Conditional.FALSE.and(Conditional.FALSE);
                const andFalse2: Conditional = Conditional.TRUE.and(Conditional.FALSE);
                const andFalse3: Conditional = Conditional.FALSE.and(Conditional.TRUE);

                expect(andTrue.get()).toBeTruthy();
                expect(andFalse1.get()).toBeFalsy();
                expect(andFalse2.get()).toBeFalsy();
                expect(andFalse3.get()).toBeFalsy();
            });
        it(
            "testOr",
            () =>
            {
                const orTrue1: Conditional = Conditional.TRUE.or(Conditional.TRUE);
                const orTrue2: Conditional = Conditional.FALSE.or(Conditional.TRUE);
                const orTrue3: Conditional = Conditional.TRUE.or(Conditional.FALSE);
                const orFalse: Conditional = Conditional.FALSE.or(Conditional.FALSE);

                expect(orTrue1.get()).toBeTruthy();
                expect(orTrue2.get()).toBeTruthy();
                expect(orTrue3.get()).toBeTruthy();
                expect(orFalse.get()).toBeFalsy();
            });
        it(
            "testXor",
            () =>
            {
                const xorTrue1: Conditional  = Conditional.TRUE.xor(Conditional.FALSE);
                const xorTrue2: Conditional  = Conditional.FALSE.xor(Conditional.TRUE);
                const xorFalse1: Conditional = Conditional.TRUE.xor(Conditional.TRUE);
                const xorFalse2: Conditional = Conditional.FALSE.xor(Conditional.FALSE);

                expect(xorTrue1.get()).toBeTruthy();
                expect(xorTrue2.get()).toBeTruthy();
                expect(xorFalse1.get()).toBeFalsy();
                expect(xorFalse2.get()).toBeFalsy();
            });
        it(
            "testNot",
            () =>
            {
                const isTrue: Conditional  = Conditional.FALSE.not();
                const isFalse: Conditional  = Conditional.TRUE.not();

                expect(isTrue.get()).toBeTruthy();
                expect(isFalse.get()).toBeFalsy();
            });

    });