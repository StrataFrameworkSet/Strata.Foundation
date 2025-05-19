/// ///////////////////////////////////////////////////////////////////////////
// Annotated.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.inject;

import jakarta.inject.Named;
import org.springframework.beans.factory.annotation.Qualifier;

public
class Annotated
{
    public static Qualifier
    qualifier(String value)
    {
        return new BasicQualifier(value);
    }

    public static Named
    named(String value)
    {
        return new BasicNamed(value);
    }
}

//////////////////////////////////////////////////////////////////////////////
