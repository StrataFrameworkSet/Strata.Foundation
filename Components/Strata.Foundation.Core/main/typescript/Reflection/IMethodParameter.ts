import {IType} from "./IType";

export
interface IMethodParameter
{
    getParameterName(): string;

    getParameterType(): IType;
}