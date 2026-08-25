import "jest"
import {Executed,Optional,NullPointerException} from "strata.foundation.core/Utility";

describe(
    "Executed",
    () =>
    {
        it(
            "testSuccessCreation",
            () =>
            {
                const result: Executed = Executed.of("testMethod");

                expect(result.isSuccess()).toBeTruthy();
                expect(result.isFailure()).toBeFalsy();
                expect(result.getMethod()).toBe("testMethod");
                expect(result.getException().isEmpty()).toBeTruthy();
            });
        it(
            "testFailureCreation",
            () =>
            {
                const error: Error = new Error("test error");
                const result: Executed = Executed.of("testMethod",error);

                expect(result.isSuccess()).toBeFalsy();
                expect(result.isFailure()).toBeTruthy();
                expect(result.getMethod()).toBe("testMethod");
                expect(result.getException().isPresent()).toBeTruthy();
                expect(result.getException().get().message).toBe("test error");
            });
        it(
            "testNullMethodOnSuccess",
            () =>
            {
                try
                {
                    Executed.of(null as unknown as string);
                    fail("should have thrown exception");
                }
                catch (e)
                {
                    expect(e instanceof NullPointerException).toBeTruthy();
                }
            });
        it(
            "testNullMethodOnFailure",
            () =>
            {
                try
                {
                    Executed.of(null as unknown as string,new Error("error"));
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
                const result: Executed = Executed.of("testMethod",null as unknown as Error);

                expect(result.isSuccess()).toBeFalsy();
                expect(result.isFailure()).toBeTruthy();
                expect(result.getException().isPresent()).toBeTruthy();
            });
    });
