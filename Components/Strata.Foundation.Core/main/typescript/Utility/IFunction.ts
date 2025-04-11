export
interface IFunction<I,O>
{
    apply(input:I): O;
}