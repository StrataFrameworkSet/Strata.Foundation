import {IPersonNameFormatter} from "./IPersonNameFormatter";
import {PersonName} from "./PersonName";
import {Expendable} from "../Utility";

export
class BasicPersonNameFormatter
    implements IPersonNameFormatter
{
    private nameSuffix: Expendable<string>;

    public constructor(nameSuffix?: string)
    {
        this.nameSuffix = Expendable.of(nameSuffix);
    }

    public format(value: PersonName): string
    {
        let output: string = "";

        if (value.title)
            output += value.title + " ";

        output += value.firstName + " ";

        if (value.middleName)
            output += value.middleName + " ";

        output += value.lastName;

        this.nameSuffix.ifPresent(suffix => output += ", " + suffix);
        return output;
    }

    public setNameSuffix(nameSuffix: string): IPersonNameFormatter
    {
        this.nameSuffix = Expendable.of(nameSuffix);
        return this;
    }
}