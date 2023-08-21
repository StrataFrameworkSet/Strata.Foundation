export
interface IMultiMap<K,V>
{
    put(key:K,value:V): IMultiMap<K,V>;

    remove(key:K): IMultiMap<K,V>;

    remove(key: K,value: V): IMultiMap<K,V>;

    get(key: K): Array<V>;

    getAt(key: K,index: number): V;

    getCardinality(key: K): number;

    getKeys(): Array<K>;

    getValues(): Array<Array<V>>;

    containsKey(key: K): boolean;

    containsValue(key: K,value: V): boolean;

}