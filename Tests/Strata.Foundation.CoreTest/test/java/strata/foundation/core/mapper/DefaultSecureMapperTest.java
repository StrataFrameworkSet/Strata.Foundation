//////////////////////////////////////////////////////////////////////////////
// DefaultSecureMapperTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import strata.foundation.core.value.PersonName;

import static org.junit.jupiter.api.Assertions.*;

@Tag("CommitStage")
public
class DefaultSecureMapperTest
{
    private ISecureMapper<PersonName> target;

    @BeforeEach
    public void
    setUp()
    {
        target = new DefaultSecureMapper<>(PersonName.class);
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
        PersonName input = new PersonName("John","Doe");
        String     encrypted = target.mapEncrypted(input);

        assertNotNull(encrypted);
        assertFalse(encrypted.isEmpty());
    }

    @Test
    public void
    testMapEncryptedProducesEncryptedOutput()
    {
        PersonName input = new PersonName("John","Doe");
        String     encrypted = target.mapEncrypted(input);

        assertNotEquals("John",encrypted);
        assertNotEquals("Doe",encrypted);
        assertFalse(encrypted.contains("John"));
        assertFalse(encrypted.contains("Doe"));
    }

    @Test
    public void
    testMapDecryptedRecoversSameValue()
    {
        PersonName input     = new PersonName("John","Doe");
        String     encrypted = target.mapEncrypted(input);
        PersonName decrypted = target.mapDecrypted(encrypted);

        assertNotNull(decrypted);
        assertEquals(input,decrypted);
        assertEquals(input.getFirstName(),decrypted.getFirstName());
        assertEquals(input.getLastName(),decrypted.getLastName());
    }

    @Test
    public void
    testMapEncryptedDecryptedRoundTripWithFullName()
    {
        PersonName input =
            new PersonName("Dr","Jane","Marie","Smith","Jr");
        String     encrypted = target.mapEncrypted(input);
        PersonName decrypted = target.mapDecrypted(encrypted);

        assertNotNull(decrypted);
        assertEquals(input,decrypted);
        assertEquals(input.getTitle(),decrypted.getTitle());
        assertEquals(input.getFirstName(),decrypted.getFirstName());
        assertEquals(input.getMiddleName(),decrypted.getMiddleName());
        assertEquals(input.getLastName(),decrypted.getLastName());
        assertEquals(input.getSuffix(),decrypted.getSuffix());
    }

    @Test
    public void
    testMapEncryptedProducesUniqueOutputPerCall()
    {
        PersonName input      = new PersonName("John","Doe");
        String     encrypted1 = target.mapEncrypted(input);
        String     encrypted2 = target.mapEncrypted(input);

        assertNotEquals(encrypted1,encrypted2,
            "Each encryption should produce unique ciphertext");
    }

    @Test
    public void
    testMapDecryptedWithDifferentCiphertextsProducesSameValue()
    {
        PersonName input      = new PersonName("John","Doe");
        String     encrypted1 = target.mapEncrypted(input);
        String     encrypted2 = target.mapEncrypted(input);
        PersonName decrypted1 = target.mapDecrypted(encrypted1);
        PersonName decrypted2 = target.mapDecrypted(encrypted2);

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
        ISecureMapper<PersonName> mapper =
            new DefaultSecureMapper<>(
                PersonName.class,
                System.getenv("PROPERTIES_ENCRYPTION_KEY"));

        PersonName input     = new PersonName("Alice","Wonder");
        String     encrypted = mapper.mapEncrypted(input);
        PersonName decrypted = mapper.mapDecrypted(encrypted);

        assertEquals(input,decrypted);
    }
}

//////////////////////////////////////////////////////////////////////////////

