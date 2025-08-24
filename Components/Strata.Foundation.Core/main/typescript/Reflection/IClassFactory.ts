import {Class} from "./Class";

export
interface IClassFactory
{
    create<T>(instance: any): Class<T>
}