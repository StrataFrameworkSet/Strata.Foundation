//////////////////////////////////////////////////////////////////////////////
// SuppliedTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Tag("CommitStage")
public
class SuppliedTest
{
    private Supplied<String> success;
    private Supplied<String> failure;

    @BeforeEach
    public void
    setUp()
    {
        success = Supplied.of("testMethod","testOutput");
        failure = Supplied.of("testMethod",new RuntimeException("test error"));
    }

    @Test
    public void
    testSuccessCreation()
    {
        assertTrue(success.isSuccess());
        assertFalse(success.isFailure());
        assertEquals("testMethod",success.getMethod());
        assertTrue(success.getOutput().isPresent());
        assertEquals("testOutput",success.getOutput().get());
        assertFalse(success.getThrowable().isPresent());
    }

    @Test
    public void
    testFailureCreation()
    {
        assertFalse(failure.isSuccess());
        assertTrue(failure.isFailure());
        assertEquals("testMethod",failure.getMethod());
        assertFalse(failure.getOutput().isPresent());
        assertTrue(failure.getThrowable().isPresent());
        assertEquals("test error",failure.getThrowable().get().getMessage());
    }

    @Test
    public void
    testNullMethod()
    {
        try
        {
            Supplied.of(null,"testOutput");
            fail("Should have thrown NullPointerException");
        }
        catch (NullPointerException e) {}
    }

    @Test
    public void
    testNullOutput()
    {
        Supplied<String> result = Supplied.of("testMethod",(String)null);

        assertFalse(result.isSuccess());
        assertTrue(result.isFailure());
        assertFalse(result.getOutput().isPresent());
        assertTrue(result.getThrowable().isPresent());
    }

    @Test
    public void
    testNullThrowable()
    {
        Supplied<String> result = Supplied.of("testMethod",(Throwable)null);

        assertFalse(result.isSuccess());
        assertTrue(result.isFailure());
        assertTrue(result.getThrowable().isPresent());
    }
}

//////////////////////////////////////////////////////////////////////////////
