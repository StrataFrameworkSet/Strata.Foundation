import {ITypeManager} from "./ITypeManager";
import {Class} from "./Class";
import {Interface} from "./Interface";
import {IType} from "./IType";
import {Optional} from "../Utility";

export
class TypeManager
    implements ITypeManager
{
    private readonly types: Map<string,IType>;

    private static instance: ITypeManager = new TypeManager();

    private constructor()
    {
        this.types = new Map<string,IType>();
    }

    register(type: IType): ITypeManager
    {
        if (!this.types.has(type.getTypeName()))
            this.types.set(type.getTypeName(), type);

        return this;
    }

    getType(typename: string): Optional<IType>
    {
        return Optional.ofNullable(this.types.get(typename));
    }

    getPrimitive(typename: string): Optional<IType>
    {
        const type: IType = this.types.get(typename);

        if (type && type.isPrimitive())
            return Optional.of(type);

        return Optional.empty<IType>();
    }

    getInterface<T>(typename: string): Optional<Interface<T>>
    {
        const type: IType = this.types.get(typename);

        if (type && type.isInterface())
            return Optional.of(type as Interface<T>);

        return Optional.empty<Interface<T>>();
    }

    getClass<T>(typename: string): Optional<Class<T>>
    {
        const type: IType = this.types.get(typename);

        if (type && type.isClass())
            return Optional.of(type as Class<T>);

        return Optional.empty<Class<T>>();
    }

    getTypes(): Array<IType>
    {
        return Array.from(this.types.values());
    }

    getPrimitives(): Array<IType>
    {
        return Array.from(this.types.values())
            .filter(type => type.isPrimitive());
    }

    getInterfaces(): Array<Interface<any>>
    {
        return Array.from(this.types.values())
            .filter(type => type.isInterface())
            .map(type => type as Interface<any>);
    }

    getClasses(): Array<Class<any>>
    {
        return Array.from(this.types.values())
            .filter(type => type.isClass())
            .map(type => type as Class<any>);
    }

    hasType(typename: string): boolean
    {
        return this.types.has(typename);
    }

    hasPrimitive(typename: string): boolean
    {
        const type: IType = this.types.get(typename);
        return type !== undefined && type.isPrimitive();
    }

    hasInterface(typename: string): boolean
    {
        const type: IType = this.types.get(typename);
        return type !== undefined && type.isInterface();
    }

    hasClass(typename: string): boolean
    {
        const type: IType = this.types.get(typename);
        return type !== undefined && type.isClass();
    }

    static getInstance(): ITypeManager { return TypeManager.instance; }
}