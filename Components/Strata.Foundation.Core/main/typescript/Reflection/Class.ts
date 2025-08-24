import {IType} from "./IType";
import {ITypeParameter} from "./ITypeParameter";
import {IField} from "./IField";
import {IMethod} from "./IMethod";
import {Optional} from "../Utility";

export
class Class<T>
    implements IType
{
    private readonly typename: string;
    private readonly superType: IType;
    private readonly fields: Array<IField>;
    private readonly methods: Array<IMethod>;

    constructor(
        typename:  string,
        superType: IType,
        fields:    Array<IField>,
        methods:   Array<IMethod>)
    {
        this.typename = typename;
        this.superType = superType;
        this.fields = fields;
        this.methods = methods;
    }

    getTypeName(): string
    {
        return this.typename;
    }

    getFullTypeName(): string
    {
        return this.typename;
    }

    getSimpleTypeName(): string
    {
        return this.typename.split('.').pop();
    }

    getSuperType(): Optional<IType>
    {
        return Optional.ofNullable(this.superType);
    }

    getFields(): Array<IField>
    {
        return this.fields;
    }

    getMethods(): Array<IMethod>
    {
        return this.methods;
    }

    isSubTypeOf(type: IType): boolean
    {
        return false;
    }

    isSuperTypeOf(type: IType): boolean
    {
        return false;
    }

    isPrimitive(): boolean
    {
        return false;
    }

    isInterface(): boolean
    {
        return false;
    }

    isEnum(): boolean
    {
        return false;
    }

    isClass(): boolean
    {
        return true;
    }

    static of<T>(instance: T): Class<T>
    {
        return null;
    }
}