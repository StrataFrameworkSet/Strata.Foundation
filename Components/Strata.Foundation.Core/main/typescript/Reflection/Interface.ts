import {IType} from "./IType";
import {ITypeParameter} from "./ITypeParameter";
import {IField} from "./IField";
import {IMethod} from "./IMethod";
import {Optional} from "../Utility";
import {Method} from "./Method";

export
class Interface<T>
    implements IType
{
    private readonly typename: string;
    private readonly superType: IType;
    private readonly methods: Array<IMethod>;

    constructor(
        typename:  string,
        methods:   Array<string>,
        superType: string = null)
    {
        this.typename = typename;
        this.methods = this.initializeMethods(methods);
        this.superType = null;
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
        return true;
    }

    isEnum(): boolean
    {
        return false;
    }

    isClass(): boolean
    {
        return false;
    }

    static of<T>(instance: T): Interface<T>
    {
        return null;
    }

    private initializeMethods(methodNames:Array<string>): Array<IMethod>
    {
        return methodNames.map(
            name => new Method(name, null));
    }
}