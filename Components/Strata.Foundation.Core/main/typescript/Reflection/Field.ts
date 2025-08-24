import {IField} from "./IField";
import {IType} from "./IType";

export
class Field
    implements IField
{
    private readonly fieldName: string;
    private readonly fieldType: IType;

    constructor(fieldName: string, fieldType: IType)
    {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

    getFieldName(): string
    {
        return this.fieldName;
    }

    getFieldType(): IType
    {
        return this.fieldType;
    }

}