//////////////////////////////////////////////////////////////////////////////
// OwnedCloseableTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@Tag("CommitStage")
public
class OwnedCloseableTest
{
    private AtomicInteger closeCount;
    private AutoCloseable resource;

    @BeforeEach
    public void
    setUp()
    {
        closeCount = new AtomicInteger(0);
        resource = closeCount::incrementAndGet;
    }

    @Test
    public void
    testConstructorOwnerClosesResource() throws Exception
    {
        OwnedCloseable<AutoCloseable> subject = new OwnedCloseable<>(resource,true);

        subject.close();

        assertEquals(1,closeCount.get());
    }

    @Test
    public void
    testConstructorNonOwnerDoesNotCloseResource() throws Exception
    {
        OwnedCloseable<AutoCloseable> subject = new OwnedCloseable<>(resource,false);

        subject.close();

        assertEquals(0,closeCount.get());
    }

    @Test
    public void
    testOfOwnedClosesResource() throws Exception
    {
        OwnedCloseable<AutoCloseable> subject = OwnedCloseable.ofOwned(resource);

        subject.close();

        assertEquals(1,closeCount.get());
    }

    @Test
    public void
    testOfBorrowedDoesNotCloseResource() throws Exception
    {
        OwnedCloseable<AutoCloseable> subject = OwnedCloseable.ofBorrowed(resource);

        subject.close();

        assertEquals(0,closeCount.get());
    }

    @Test
    public void
    testGetReturnsWrappedCloseableWhenOwned()
    {
        assertSame(resource,OwnedCloseable.ofOwned(resource).get());
    }

    @Test
    public void
    testGetReturnsWrappedCloseableWhenBorrowed()
    {
        assertSame(resource,OwnedCloseable.ofBorrowed(resource).get());
    }

    @Test
    public void
    testGetReturnsNullWhenWrappingNull()
    {
        assertNull(OwnedCloseable.ofOwned(null).get());
        assertNull(OwnedCloseable.ofBorrowed(null).get());
    }

    @Test
    public void
    testCloseOwnedNullDoesNotThrow()
    {
        assertDoesNotThrow(() -> OwnedCloseable.ofOwned(null).close());
    }

    @Test
    public void
    testCloseBorrowedNullDoesNotThrow()
    {
        assertDoesNotThrow(() -> OwnedCloseable.ofBorrowed(null).close());
    }

    @Test
    public void
    testCloseOwnedRethrowsResourceException()
    {
        Exception                     expected = new Exception("failure");
        OwnedCloseable<AutoCloseable> subject  =
            OwnedCloseable.ofOwned(() -> { throw expected; });

        Exception actual = assertThrows(Exception.class,subject::close);

        assertSame(expected,actual);
    }

    @Test
    public void
    testCloseBorrowedIgnoresFailingResource()
    {
        OwnedCloseable<AutoCloseable> subject =
            OwnedCloseable.ofBorrowed(() -> { throw new Exception("failure"); });

        assertDoesNotThrow(subject::close);
    }

    @Test
    public void
    testBorrowedRepeatedCloseNeverClosesResource() throws Exception
    {
        OwnedCloseable<AutoCloseable> subject = OwnedCloseable.ofBorrowed(resource);

        subject.close();
        subject.close();

        assertEquals(0,closeCount.get());
    }

    @Test
    public void
    testTryWithResourcesOwned() throws Exception
    {
        try (OwnedCloseable<AutoCloseable> subject = OwnedCloseable.ofOwned(resource))
        {
            assertSame(resource,subject.get());
        }

        assertEquals(1,closeCount.get());
    }

    @Test
    public void
    testTryWithResourcesBorrowed() throws Exception
    {
        try (OwnedCloseable<AutoCloseable> subject = OwnedCloseable.ofBorrowed(resource))
        {
            assertSame(resource,subject.get());
        }

        assertEquals(0,closeCount.get());
    }
}

//////////////////////////////////////////////////////////////////////////////
