/// ///////////////////////////////////////////////////////////////////////////
// BasicPersonNameFormatter.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import strata.foundation.core.utility.Expendable;

public
class BasicPersonNameFormatter
    implements IPersonNameFormatter
{
    private Expendable<String> nameSuffix;

    public
    BasicPersonNameFormatter()
    {
        this.nameSuffix = Expendable.empty();
    }

    public
    BasicPersonNameFormatter(String nameSuffix)
    {
        this.nameSuffix = Expendable.of(nameSuffix);
    }

    @Override
    public String
    format(PersonName personName)
    {
        StringBuilder builder = new StringBuilder();

        builder.append(personName.toString());
        nameSuffix.ifPresent(suffix -> builder.append(", ").append(suffix));

        return builder.toString();
    }

    public BasicPersonNameFormatter
    setNameSuffix(String nameSuffix)
    {
        this.nameSuffix = Expendable.of(nameSuffix);
        return this;
    }
}

//////////////////////////////////////////////////////////////////////////////
