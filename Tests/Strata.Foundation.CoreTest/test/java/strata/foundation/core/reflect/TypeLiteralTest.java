/// ///////////////////////////////////////////////////////////////////////////
// TypeLiteralTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.reflect;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Tag("CommitStage")
public
class TypeLiteralTest
{
    @Test
    public void
    testTypeLiteralNonGeneric()
    {
        TypeLiteral<String> typeLiteral = new TypeLiteral<>() {};

        assertNotNull(typeLiteral.getType());
        assertNotNull(typeLiteral.getRawType());
        assertEquals(String.class, typeLiteral.getType());
        assertEquals(String.class, typeLiteral.getRawType());
        assertEquals("java.lang.String", typeLiteral.toString());
    }

    @Test
    public void
    testTypeLiteralGeneric()
    {
        TypeLiteral<Set<String>> typeLiteral = new TypeLiteral<>() {};

        assertNotNull(typeLiteral.getType());
        assertNotNull(typeLiteral.getRawType());
        assertEquals(Set.class, typeLiteral.getRawType());
        assertEquals("java.util.Set<java.lang.String>", typeLiteral.toString());
    }

}

//////////////////////////////////////////////////////////////////////////////
