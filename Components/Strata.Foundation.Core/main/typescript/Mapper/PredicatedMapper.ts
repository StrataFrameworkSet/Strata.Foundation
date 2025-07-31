import {IPredicateOrLambda, LambdaPredicate} from "../Utility/LambdaPredicate";
import {ISupplier} from "../Utility/ISupplier";
import {Optional} from "../Utility/Optional";
import {IFunctionOrLambda, LambdaFunction} from "../Utility/LambdaFunction";

export
class PredicatedMapper<I,O>
    implements ISupplier<Optional<O>>
{
    private input: Optional<I>;
    private mappings: Map<LambdaPredicate<I>,LambdaFunction<I,O>>;

    constructor(input?: I)
    {
        this.input = Optional.ofNullable(input);
        this.mappings = new Map<LambdaPredicate<I>,LambdaFunction<I,O>>();
    }

    addMapping(predicate: IPredicateOrLambda<I>, mapper: IFunctionOrLambda<I,O>):
        PredicatedMapper<I,O>
    {
        this.mappings.set(
            LambdaPredicate.of(predicate),
            LambdaFunction.of(mapper));

        return this;
    }

    clearMappings(): PredicatedMapper<I,O>
    {
        this.mappings.clear();
        return this;
    }

    get(): Optional<O>
    {
        if (this.input.isPresent())
        {
            const input: I = this.input.get();

            for (const [predicate,mapper] of this.mappings.entries())
                if (predicate.test(input))
                    return Optional.of(mapper.apply(input));
        }

        return Optional.empty();
    }

    map(input?: I): Optional<O>
    {
        const newInput: Optional<I> = Optional.ofNullable(input);

        if (newInput.isPresent())
            this.input = newInput;

        return this.get();
    }

    static of<I,O>(input?: I): PredicatedMapper<I,O>
    {
        return new PredicatedMapper<I, O>(input);
    }

}