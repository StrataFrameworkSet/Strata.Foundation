import {IMultiMap} from "./IMultiMap";

export
class MultiMap<K,V>
    implements IMultiMap<K,V>
{
    private imp: Map<K,Array<V>>;

    constructor()
    {
        this.imp = new Map<K,Array<V>>();
    }

    put(key: K, value: V): IMultiMap<K,V>
    {
        if ( key == null )
            throw new Error("key is null");

        if ( !this.imp.has( key ))
            this.imp.set( key,new Array<V>() );

        this.imp.get( key ).push( value );
        return this;
    }

    remove(key: K): IMultiMap<K,V>;
    remove(key: K, value: V): IMultiMap<K,V>;
    remove(key: K, value?: V): IMultiMap<K,V>
    {
        if (value)
        {
            if ( this.imp.has( key ))
            {
                let values: Array<V> = this.imp.get(key);

                values.slice(values.indexOf(value),1);
            }
        }
        else
        {
            if (this.imp.has(key))
                this.imp.delete(key);
        }

        return this;
    }

    get(key: K): Array<V>
    {
        return this.imp.get(key);
    }

    getAt(key: K, index: number): V
    {
        return this.imp.get(key)[index];
    }

    getCardinality(key: K): number
    {
        return this.imp.get(key).length;
    }

    getKeys(): Array<K>
    {
        return Array.from(this.imp.keys());
    }

    getValues(): Array<Array<V>>
    {
        return Array.from(this.imp.values());
    }

    containsKey(key: K): boolean
    {
        return this.imp.has(key);
    }

    containsValue(key: K, value: V): boolean
    {
        if (this.containsKey(key))
            return this.get(key).includes(value);

        return false;
    }

}