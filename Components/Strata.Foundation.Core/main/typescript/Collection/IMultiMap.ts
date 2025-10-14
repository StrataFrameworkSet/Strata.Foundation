import {MultiMapEntry} from "./MultiMapEntry";
import {MapEntry} from "./MapEntry";
import {IConsumerOrLambda} from "../Utility/LambdaConsumer";

export
interface IMultiMap<K,V>
{
    put(key:K,value:V): IMultiMap<K,V>;

    putAll(key:K,values:Array<V>): IMultiMap<K,V>;

    putAll(other:IMultiMap<K,V>): IMultiMap<K,V>;

    replace(key:K,values:Array<V>): IMultiMap<K,V>;

    replaceAll(other:IMultiMap<K,V>): IMultiMap<K,V>;

    remove(key:K): IMultiMap<K,V>;

    remove(key: K,value: V): IMultiMap<K,V>;

    removeAll(key: K,values: Array<V>): IMultiMap<K,V>;

    removeAll(other:IMultiMap<K,V>): IMultiMap<K,V>;

    clear(): IMultiMap<K,V>;

    getSize(): number;

    getKeys(): Set<K>;

    getValues(): Array<Array<V>>;

    getEntries(): Array<MultiMapEntry<K,V>>;

    flatten(): Array<MapEntry<K,V>>;

    get(key: K): Array<V>;

    getOrDefault(key: K,defaultValue: Array<V>): Array<V>;

    getAt(key: K,index: number): V;

    isEmpty(): boolean;

    containsKey(key: K): boolean;

    containsValue(key: K,value: V): boolean;

    forEach(consumer: IConsumerOrLambda<MultiMapEntry<K,V>>): void;

    makeUnionWith(other: IMultiMap<K,V>): IMultiMap<K,V>;

    makeIntersectionWith(other: IMultiMap<K,V>): IMultiMap<K,V>;

    makeSymmetricDifferenceWith(other: IMultiMap<K,V>): IMultiMap<K,V>;
}