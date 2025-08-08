import {IPersonNameFormatter} from "./IPersonNameFormatter";
import {PersonName} from "./PersonName";

export
class BasicPersonNameFormatter
    implements IPersonNameFormatter
{
    public constructor() {}

    public format(value: PersonName): string
    {
        let output: string = "";

        if (value.title)
            output += value.title + " ";

        output += value.firstName + " ";

        if (value.middleName)
            output += value.middleName + " ";

        output += value.lastName;

        if (value.suffix)
            output += ", " + value.suffix;

        return output;
    }
}