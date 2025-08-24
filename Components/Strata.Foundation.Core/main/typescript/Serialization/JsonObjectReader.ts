import {AbstractObjectReader} from "./AbstractObjectReader";

export
class JsonObjectReader
    extends AbstractObjectReader
{
    private source: string;

    public constructor(source: string)
    {
        super(JSON.parse(source));
        this.source = source;
    }

}