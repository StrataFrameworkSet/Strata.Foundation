import {IClassFactory} from "./IClassFactory";
import {Class} from "./Class";
import {IType} from "./IType";
import {IField} from "./IField";
import {Field} from "./Field";
import {IMethod} from "./IMethod";
import {Method} from "./Method";

export
class ClassFactory
    implements IClassFactory
{
    create<T>(instance: T): Class<T>
    {
        const proto: any = Object.getPrototypeOf(instance);
        return this.createFromPrototype<T>(proto);
    }

    protected createFromPrototype<T>(prototype: any): Class<T>
    {
        return new Class<T>(
            this.getClassNameFromPrototype(prototype),
            this.getSuperTypeFromPrototype(prototype),
            this.getFieldsFromPrototype(prototype),
            this.getMethodsFromPrototype(prototype)
        );
    }

    protected getClassNameFromPrototype(prototype: any): string
    {
        return prototype?.constructor?.name || typeof prototype || 'Unknown';
    }

    protected getSuperTypeFromPrototype(prototype: any): IType
    {
        const parentPrototype: any = Object.getPrototypeOf(prototype);

        if (parentPrototype && parentPrototype.constructor && parentPrototype.constructor !== Object)
            return this.createFromPrototype(parentPrototype);

        return null;
    }

    protected getFieldsFromPrototype(prototype: any): Array<IField>
    {
        return Object.getOwnPropertyNames(prototype)
            .map(prop => Object.getOwnPropertyDescriptor(prototype, prop))
            .filter(descriptor => descriptor !== undefined)
            .filter(descriptor => descriptor.value !== 'function')
            .map(descriptor => Object.getPrototypeOf(descriptor.value))
            .filter(fieldPrototype => fieldPrototype !== undefined)
            .map(fieldPrototype =>
                new Field(
                    this.getClassNameFromPrototype(fieldPrototype),
                    this.createFromPrototype(fieldPrototype)));
    }

    protected getMethodsFromPrototype(prototype: any): Array<IMethod>
    {
        return Object.getOwnPropertyNames(prototype)
            .map(prop =>
                {
                    const pair: any =  {
                        property: prop,
                        descriptor: Object.getOwnPropertyDescriptor(prototype, prop)
                    };

                    return pair;

                })
            .filter(pair => pair.descriptor !== undefined)
            .filter(pair => typeof pair.descriptor.value === 'function')
            .map(pair =>
                new Method(
                    pair.property,
                    pair.descriptor.value as Function));
    }
}