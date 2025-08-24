import {IMethod} from "./IMethod";

export
class Method
    implements IMethod
{
    private readonly methodName: string;
    private readonly methodAsFunction: Function;

    constructor(
        methodName:       string,
        methodAsFunction: Function)
    {
        this.methodName = methodName;
        this.methodAsFunction = methodAsFunction;
    }

    getMethodName(): string
    {
        return this.methodName;
    }

    getMethodAsFunction(): Function
    {
        return this.methodAsFunction;
    }

}