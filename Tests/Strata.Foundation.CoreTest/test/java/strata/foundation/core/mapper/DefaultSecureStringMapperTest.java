//////////////////////////////////////////////////////////////////////////////
// DefaultSecureStringMapperTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("CommitStage")
public
class DefaultSecureStringMapperTest
{
    private ISecureStringMapper target;

    @BeforeEach
    public void
    setUp()
    {
        target = new DefaultSecureStringMapper();
    }

    @AfterEach
    public void
    tearDown()
    {
        target = null;
    }

    @Test
    public void
    testMapEncryptedProducesNonNullResult()
    {
        String encrypted = target.mapEncrypted("hello world");

        assertNotNull(encrypted);
        assertFalse(encrypted.isEmpty());
    }

    @Test
    public void
    testMapEncryptedProducesEncryptedOutput()
    {
        String input     = "sensitive data";
        String encrypted = target.mapEncrypted(input);

        assertNotEquals(input,encrypted);
        assertFalse(encrypted.contains(input));
    }

    @Test
    public void
    testMapDecryptedRecoversSameValue()
    {
        String input     = "hello world";
        String encrypted = target.mapEncrypted(input);
        String decrypted = target.mapDecrypted(encrypted);

        assertNotNull(decrypted);
        assertEquals(input,decrypted);
    }

    @Test
    public void
    testMapEncryptedDecryptedRoundTripWithSpecialCharacters()
    {
        String input     = "p@$$w0rd!#%^&*()";
        String encrypted = target.mapEncrypted(input);
        String decrypted = target.mapDecrypted(encrypted);

        assertEquals(input,decrypted);
    }

    @Test
    public void
    testMapEncryptedDecryptedRoundTripWithEmptyString()
    {
        String input     = "";
        String encrypted = target.mapEncrypted(input);
        String decrypted = target.mapDecrypted(encrypted);

        assertEquals(input,decrypted);
    }

    @Test
    public void
    testMapEncryptedDecryptedRoundTripWithUnicode()
    {
        String input     = "こんにちは世界 🌍";
        String encrypted = target.mapEncrypted(input);
        String decrypted = target.mapDecrypted(encrypted);

        assertEquals(input,decrypted);
    }

    @Test
    public void
    testMapEncryptedDecryptedRoundTripWithLongString()
    {
        String input     = "A".repeat(10000);
        String encrypted = target.mapEncrypted(input);
        String decrypted = target.mapDecrypted(encrypted);

        assertEquals(input,decrypted);
    }

    @Test
    public void
    testMapEncryptedProducesUniqueOutputPerCall()
    {
        String input      = "same input";
        String encrypted1 = target.mapEncrypted(input);
        String encrypted2 = target.mapEncrypted(input);

        assertNotEquals(encrypted1,encrypted2,
            "Each encryption should produce unique ciphertext");
    }

    @Test
    public void
    testMapDecryptedWithDifferentCiphertextsProducesSameValue()
    {
        String input      = "same input";
        String encrypted1 = target.mapEncrypted(input);
        String encrypted2 = target.mapEncrypted(input);
        String decrypted1 = target.mapDecrypted(encrypted1);
        String decrypted2 = target.mapDecrypted(encrypted2);

        assertEquals(decrypted1,decrypted2);
    }

    @Test
    public void
    testMapEncryptedWithNullInputReturnsNull()
    {
        assertNull(target.mapEncrypted(null));
    }

    @Test
    public void
    testMapDecryptedWithNullInputReturnsNull()
    {
        assertNull(target.mapDecrypted(null));
    }

    @Test
    public void
    testMapDecryptedWithInvalidInputThrows()
    {
        assertThrows(RuntimeException.class,
            () -> target.mapDecrypted("not-valid-encrypted-text"));
    }

    @Test
    public void
    testConstructorWithExplicitKey()
    {
        ISecureStringMapper mapper =
            new DefaultSecureStringMapper("encryption-key");

        String input     = "test with explicit key";
        String encrypted = mapper.mapEncrypted(input);
        String decrypted = mapper.mapDecrypted(encrypted);

        assertEquals(input,decrypted);
    }
}

//////////////////////////////////////////////////////////////////////////////

