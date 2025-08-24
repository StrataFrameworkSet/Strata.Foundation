import {IType} from "./IType";

export
interface ITypeParameter
{
    getParameterName(): string;

    getParameterType(): IType;
}