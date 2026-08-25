//////////////////////////////////////////////////////////////////////////////
// ExecutedTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Tag("CommitStage")
public
class ExecutedTest
{
    private Executed success;
    private Executed failure;

    @BeforeEach
    public void
    setUp()
    {
        success = Executed.of("testMethod");
        failure = Executed.of("testMethod",new RuntimeException("test error"));
    }

    @Test
    public void
    testSuccessCreation()
    {
        assertTrue(success.isSuccess());
        assertFalse(success.isFailure());
        assertEquals("testMethod",success.getMethod());
        assertFalse(success.getException().isPresent());
    }

    @Test
    public void
    testFailureCreation()
    {
        assertFalse(failure.isSuccess());
        assertTrue(failure.isFailure());
        assertEquals("testMethod",failure.getMethod());
        assertTrue(failure.getException().isPresent());
        assertEquals("test error",failure.getException().get().getMessage());
    }

    @Test
    public void
    testNullMethodOnSuccess()
    {
        try
        {
            Executed.of(null);
            fail("Should have thrown NullPointerException");
        }
        catch (NullPointerException e) {}
    }

    @Test
    public void
    testNullMethodOnFailure()
    {
        try
        {
            Executed.of(null,new RuntimeException("error"));
            fail("Should have thrown NullPointerException");
        }
        catch (NullPointerException e) {}
    }

    @Test
    public void
    testNullThrowable()
    {
        Executed result = Executed.of("testMethod",(Throwable)null);

        assertFalse(result.isSuccess());
        assertTrue(result.isFailure());
        assertTrue(result.getException().isPresent());
    }
}

//////////////////////////////////////////////////////////////////////////////
