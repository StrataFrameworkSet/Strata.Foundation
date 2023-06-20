//////////////////////////////////////////////////////////////////////////////
// HashedStringTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.value;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("CommitStage")
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
        Assertions.assertTrue(foo.matches("XXXXXXX"));
        Assertions.assertFalse(foo.matches("XXXXXXY"));
        Assertions.assertFalse(bar.matches("XXXXXXX"));
    }

    @Disabled
    @Test
    public void
    testToString()
    {
        Assertions.assertEquals(foo.toString(),"");
    }

}

//////////////////////////////////////////////////////////////////////////////
