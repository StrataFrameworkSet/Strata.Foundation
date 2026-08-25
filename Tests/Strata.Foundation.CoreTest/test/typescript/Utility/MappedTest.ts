import "jest"
import {Mapped,Optional,NullPointerException} from "strata.foundation.core/Utility";

describe(
    "Mapped",
    () =>
    {
        it(
            "testSuccessCreation",
            () =>
            {
                const result: Mapped<string,string> = Mapped.of("testMethod","testInput","testOutput");

                expect(result.isSuccess()).toBeTruthy();
                expect(result.isFailure()).toBeFalsy();
                expect(result.getMethod()).toBe("testMethod");
                expect(result.getInput()).toBe("testInput");
                expect(result.getOutput().isPresent()).toBeTruthy();
                expect(result.getOutput().get()).toBe("testOutput");
                expect(result.getThrowable().isEmpty()).toBeTruthy();
            });
        it(
            "testFailureCreation",
            () =>
            {
                const error: Error = new Error("test error");
                const result: Mapped<string,string> = Mapped.of<string,string>("testMethod","testInput",error);

                expect(result.isSuccess()).toBeFalsy();
                expect(result.isFailure()).toBeTruthy();
                expect(result.getMethod()).toBe("testMethod");
                expect(result.getInput()).toBe("testInput");
                expect(result.getOutput().isEmpty()).toBeTruthy();
                expect(result.getThrowable().isPresent()).toBeTruthy();
                expect(result.getThrowable().get().message).toBe("test error");
            });
        it(
            "testNullMethod",
            () =>
            {
                try
                {
                    Mapped.of(null as unknown as string,"testInput","testOutput");
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
                    Mapped.of("testMethod",null as unknown as string,"testOutput");
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
                const result: Mapped<string,string> = Mapped.of("testMethod","testInput",null as unknown as string);

                expect(result.isSuccess()).toBeFalsy();
                expect(result.isFailure()).toBeTruthy();
                expect(result.getOutput().isEmpty()).toBeTruthy();
                expect(result.getThrowable().isPresent()).toBeTruthy();
            });
    });
