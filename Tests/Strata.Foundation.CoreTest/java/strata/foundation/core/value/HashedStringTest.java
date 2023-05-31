//////////////////////////////////////////////////////////////////////////////
// HashedStringTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public
class HashedStringTest
{
    private HashedString foo;
    private HashedString bar;

    @BeforeEach
    public void
    setUp()
    {
        foo = new HashedString("XXXXXXX");
        bar = new HashedString("YYYYYYY");
    }

    @Test
    public void
    testMatches()
    {
        assertTrue(foo.matches("XXXXXXX"));
        assertFalse(foo.matches("XXXXXXY"));
        assertFalse(bar.matches("XXXXXXX"));
    }

    @Disabled
    @Test
    public void
    testToString()
    {
        assertEquals(foo.toString(),"");
    }

}

//////////////////////////////////////////////////////////////////////////////
