import {ITypeParameter} from "./ITypeParameter";
import {IField} from "./IField";
import {IMethod} from "./IMethod";
import {Optional} from "../Utility";

export
interface IType
{
    getTypeName(): string;

    getFullTypeName(): string;

    getSimpleTypeName(): string;

    isSubTypeOf(type: IType): boolean;

    isSuperTypeOf(type: IType): boolean;

    isPrimitive(): boolean;

    isInterface(): boolean;

    isEnum(): boolean;

    isClass(): boolean;

}