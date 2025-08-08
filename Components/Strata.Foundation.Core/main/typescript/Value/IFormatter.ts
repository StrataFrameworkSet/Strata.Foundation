export
interface IFormatter<V,O>
{
    format(value:  V): O;
}