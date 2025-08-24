import {IType} from "./IType";
import {Interface} from "./Interface";
import {Class} from "./Class";
import {Optional} from "../Utility";

export
interface ITypeManager
{
    register(type: IType): ITypeManager;

    getType(typename: string): Optional<IType>;

    getPrimitive(typename: string): Optional<IType>;

    getInterface<T>(typename: string): Optional<Interface<T>>;

    getClass<T>(typename: string): Optional<Class<T>>;

    getTypes(): Array<IType>;

    getPrimitives(): Array<IType>;

    getInterfaces(): Array<Interface<any>>;

    getClasses(): Array<Class<any>>;

    hasType(typename: string): boolean;

    hasPrimitive(typename: string): boolean;

    hasInterface(typename: string): boolean;

    hasClass(typename: string): boolean;
}