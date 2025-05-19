/// ///////////////////////////////////////////////////////////////////////////
// ThreadNameSupplier.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.inject;

import java.util.UUID;
import java.util.function.Supplier;

public
class ThreadNameSupplier
    implements Supplier<String>
{
    private final String name;

    public
    ThreadNameSupplier()
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
