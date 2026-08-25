//////////////////////////////////////////////////////////////////////////////
// ConsumedTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Tag("CommitStage")
public
class ConsumedTest
{
    private Consumed<String> success;
    private Consumed<String> failure;

    @BeforeEach
    public void
    setUp()
    {
        success = Consumed.of("testMethod","testInput");
        failure = Consumed.of("testMethod","testInput",new RuntimeException("test error"));
    }

    @Test
    public void
    testSuccessCreation()
    {
        assertTrue(success.isSuccess());
        assertFalse(success.isFailure());
        assertEquals("testMethod",success.getMethod());
        assertEquals("testInput",success.getInput());
        assertFalse(success.getThrowable().isPresent());
    }

    @Test
    public void
    testFailureCreation()
    {
        assertFalse(failure.isSuccess());
        assertTrue(failure.isFailure());
        assertEquals("testMethod",failure.getMethod());
        assertEquals("testInput",failure.getInput());
        assertTrue(failure.getThrowable().isPresent());
        assertEquals("test error",failure.getThrowable().get().getMessage());
    }

    @Test
    public void
    testNullMethod()
    {
        try
        {
            Consumed.of(null,"testInput");
            fail("Should have thrown NullPointerException");
        }
        catch (NullPointerException e) {}
    }

    @Test
    public void
    testNullInput()
    {
        try
        {
            Consumed.of("testMethod",(String)null);
            fail("Should have thrown NullPointerException");
        }
        catch (NullPointerException e) {}
    }

    @Test
    public void
    testNullThrowable()
    {
        Consumed<String> result = Consumed.of("testMethod","testInput",(Throwable)null);

        assertFalse(result.isSuccess());
        assertTrue(result.isFailure());
        assertTrue(result.getThrowable().isPresent());
    }
}

//////////////////////////////////////////////////////////////////////////////
