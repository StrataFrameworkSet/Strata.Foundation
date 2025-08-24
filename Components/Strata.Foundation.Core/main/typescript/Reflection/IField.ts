import {IType} from "./IType";

export
interface IField
{
    getFieldName(): string;

    getFieldType(): IType;
}