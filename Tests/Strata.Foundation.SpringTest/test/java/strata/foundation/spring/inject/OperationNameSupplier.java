/// ///////////////////////////////////////////////////////////////////////////
// ThreadNameSupplier.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.inject;

import java.util.UUID;
import java.util.function.Supplier;

public
class OperationNameSupplier
    implements Supplier<String>
{
    private final String name;

    public OperationNameSupplier()
    {
        this.name = UUID.randomUUID().toString();
    }

    @Override
    public String
    get()
    {
        return name + "-" + Thread.currentThread().getName();
    }
}

//////////////////////////////////////////////////////////////////////////////
