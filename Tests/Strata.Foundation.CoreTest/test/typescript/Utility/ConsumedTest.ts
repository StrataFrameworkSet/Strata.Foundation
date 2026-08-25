import "jest"
import {Consumed,Optional,NullPointerException} from "strata.foundation.core/Utility";

describe(
    "Consumed",
    () =>
    {
        it(
            "testSuccessCreation",
            () =>
            {
                const result: Consumed<string> = Consumed.of("testMethod","testInput");

                expect(result.isSuccess()).toBeTruthy();
                expect(result.isFailure()).toBeFalsy();
                expect(result.getMethod()).toBe("testMethod");
                expect(result.getInput()).toBe("testInput");
                expect(result.getThrowable().isEmpty()).toBeTruthy();
            });
        it(
            "testFailureCreation",
            () =>
            {
                const error: Error = new Error("test error");
                const result: Consumed<string> = Consumed.of("testMethod","testInput",error);

                expect(result.isSuccess()).toBeFalsy();
                expect(result.isFailure()).toBeTruthy();
                expect(result.getMethod()).toBe("testMethod");
                expect(result.getInput()).toBe("testInput");
                expect(result.getThrowable().isPresent()).toBeTruthy();
                expect(result.getThrowable().get().message).toBe("test error");
            });
        it(
            "testNullMethod",
            () =>
            {
                try
                {
                    Consumed.of(null as unknown as string,"testInput");
                    fail("should have thrown exception");
                }
                catch (e)
                {
                    expect(e instanceof NullPointerException).toBeTruthy();
                }
            });
        it(
            "testNullInput",
            () =>
            {
                try
                {
                    Consumed.of("testMethod",null as unknown as string);
                    fail("should have thrown exception");
                }
                catch (e)
                {
                    expect(e instanceof NullPointerException).toBeTruthy();
                }
            });
        it(
            "testNullThrowable",
            () =>
            {
                const result: Consumed<string> = Consumed.of("testMethod","testInput",null as unknown as Error);

                expect(result.isSuccess()).toBeFalsy();
                expect(result.isFailure()).toBeTruthy();
                expect(result.getThrowable().isPresent()).toBeTruthy();
            });
    });
