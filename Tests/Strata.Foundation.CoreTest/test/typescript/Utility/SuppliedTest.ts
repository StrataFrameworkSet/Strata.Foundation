import "jest"
import {Supplied,Optional,NullPointerException} from "strata.foundation.core/Utility";

describe(
    "Supplied",
    () =>
    {
        it(
            "testSuccessCreation",
            () =>
            {
                const result: Supplied<string> = Supplied.of("testMethod","testOutput");

                expect(result.isSuccess()).toBeTruthy();
                expect(result.isFailure()).toBeFalsy();
                expect(result.getMethod()).toBe("testMethod");
                expect(result.getOutput().isPresent()).toBeTruthy();
                expect(result.getOutput().get()).toBe("testOutput");
                expect(result.getException().isEmpty()).toBeTruthy();
            });
        it(
            "testFailureCreation",
            () =>
            {
                const error: Error = new Error("test error");
                const result: Supplied<string> = Supplied.of<string>("testMethod",error);

                expect(result.isSuccess()).toBeFalsy();
                expect(result.isFailure()).toBeTruthy();
                expect(result.getMethod()).toBe("testMethod");
                expect(result.getOutput().isEmpty()).toBeTruthy();
                expect(result.getException().isPresent()).toBeTruthy();
                expect(result.getException().get().message).toBe("test error");
            });
        it(
            "testNullMethod",
            () =>
            {
                try
                {
                    Supplied.of(null as unknown as string,"testOutput");
                    fail("should have thrown exception");
                }
                catch (e)
                {
                    expect(e instanceof NullPointerException).toBeTruthy();
                }
            });
        it(
            "testNullOutput",
            () =>
            {
                const result: Supplied<string> = Supplied.of("testMethod",null as unknown as string);

                expect(result.isSuccess()).toBeFalsy();
                expect(result.isFailure()).toBeTruthy();
                expect(result.getOutput().isEmpty()).toBeTruthy();
                expect(result.getException().isPresent()).toBeTruthy();
            });
    });
