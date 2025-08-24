/// ///////////////////////////////////////////////////////////////////////////
// MappingKey.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import java.util.Objects;

public
class MappingKey
    implements Comparable<MappingKey>
{
    private final Class<?> inputType;
    private final Class<?> outputType;

    public
    MappingKey(Class<?> inputType, Class<?> outputType)
    {
        this.inputType = inputType;
        this.outputType = outputType;
    }

    @Override
    public int
    hashCode()
    {
        return Objects.hash(inputType, outputType);
    }

    @Override
    public boolean
    equals(Object other)
    {
        return
            (other instanceof MappingKey mk) &&
            Objects.equals(inputType,mk.inputType) &&
            Objects.equals(outputType,mk.outputType);
    }

    @Override
    public String
    toString()
    {
        return
            String.format(
                "[%s->%s]",
                inputType.getName(),
                outputType.getName());
    }

    @Override
    public int
    compareTo(MappingKey other)
    {
        int inputComparison =
            inputType.getName().compareTo(other.inputType.getName());

        if (inputComparison != 0)
            return inputComparison;

        return outputType.getName().compareTo(other.outputType.getName());
    }

    public boolean
    matches(Class<?> inputType, Class<?> outputType)
    {
        return
            Objects.equals(this.inputType, inputType) &&
            Objects.equals(this.outputType, outputType);
    }

    public boolean
    matchesInput(Class<?> inputType)
    {
        return Objects.equals(this.inputType, inputType);
    }

    public boolean
    matchesOutput(Class<?> outputType)
    {
        return Objects.equals(this.outputType, outputType);
    }

    public static MappingKey
    of(Class<?> inputType, Class<?> outputType)
    {
        return new MappingKey(inputType, outputType);
    }
}

//////////////////////////////////////////////////////////////////////////////
