//////////////////////////////////////////////////////////////////////////////
// MappedTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Tag("CommitStage")
public
class MappedTest
{
    private Mapped<String,String> success;
    private Mapped<String,String> failure;

    @BeforeEach
    public void
    setUp()
    {
        success = Mapped.of("testMethod","testInput","testOutput");
        failure = Mapped.of("testMethod","testInput",new RuntimeException("test error"));
    }

    @Test
    public void
    testSuccessCreation()
    {
        assertTrue(success.isSuccess());
        assertFalse(success.isFailure());
        assertEquals("testMethod",success.getMethod());
        assertEquals("testInput",success.getInput());
        assertTrue(success.getOutput().isPresent());
        assertEquals("testOutput",success.getOutput().get());
        assertFalse(success.getException().isPresent());
    }

    @Test
    public void
    testFailureCreation()
    {
        assertFalse(failure.isSuccess());
        assertTrue(failure.isFailure());
        assertEquals("testMethod",failure.getMethod());
        assertEquals("testInput",failure.getInput());
        assertFalse(failure.getOutput().isPresent());
        assertTrue(failure.getException().isPresent());
        assertEquals("test error",failure.getException().get().getMessage());
    }

    @Test
    public void
    testNullMethod()
    {
        try
        {
            Mapped.of(null,"testInput","testOutput");
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
            Mapped.of("testMethod",(String)null,"testOutput");
            fail("Should have thrown NullPointerException");
        }
        catch (NullPointerException e) {}
    }

    @Test
    public void
    testNullOutput()
    {
        Mapped<String,String> result = Mapped.of("testMethod","testInput",(String)null);

        assertFalse(result.isSuccess());
        assertTrue(result.isFailure());
        assertFalse(result.getOutput().isPresent());
        assertTrue(result.getException().isPresent());
    }

    @Test
    public void
    testNullThrowable()
    {
        Mapped<String,String> result = Mapped.of("testMethod","testInput",(Throwable)null);

        assertFalse(result.isSuccess());
        assertTrue(result.isFailure());
        assertTrue(result.getException().isPresent());
    }
}

//////////////////////////////////////////////////////////////////////////////
