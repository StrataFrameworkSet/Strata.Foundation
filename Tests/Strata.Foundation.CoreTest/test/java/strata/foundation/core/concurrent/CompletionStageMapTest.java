/// ///////////////////////////////////////////////////////////////////////////
// CompletionStageMapTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@Tag("CommitStage")
public
class CompletionStageMapTest
{
    private ExecutorService                   executor;
    private CompletionStageMap<String,String> subject;

    @BeforeEach
    public void
    setUp() throws Exception
    {
        executor = Executors.newCachedThreadPool();
        subject  = new CompletionStageMap<>();
    }

    @Test
    public void
    testPutGet() throws Exception
    {
        CompletionStage<String> expectedA =
            CompletableFuture.supplyAsync(
                () -> outputWithDelay("Apple",5000),executor);
        CompletionStage<String> expectedB =
            CompletableFuture.supplyAsync(
                () -> outputWithDelay("Banana",5000),executor);
        CompletionStage<String> expectedC =
            CompletableFuture.supplyAsync(
                () -> outputWithDelay("Cherry",5000),executor);

        subject.put("A",expectedA);
        subject.put("B",expectedB);
        subject.put("C",expectedC);

        assertEquals(expectedA,subject.get("A"));
        assertEquals(expectedB,subject.get("B"));
        assertEquals(expectedC,subject.get("C"));
    }

    @Test
    public void
    testRemoveIsEmpty() throws Exception
    {
        CompletionStage<String> expectedA =
            CompletableFuture.supplyAsync(
                () -> outputWithDelay("Apple",50),executor);
        CompletionStage<String> expectedB =
            CompletableFuture.supplyAsync(
                () -> outputWithDelay("Banana",50),executor);
        CompletionStage<String> expectedC =
            CompletableFuture.supplyAsync(
                () -> outputWithDelay("Cherry",50),executor);

        subject.put("A",expectedA);
        subject.put("B",expectedB);
        subject.put("C",expectedC);

        assertFalse(subject.isEmpty());

        Thread.sleep(100);

        assertTrue(subject.isEmpty());

    }

    @Test
    public void
    testJoinAll() throws Exception
    {
        CompletionStage<String> expectedA =
            CompletableFuture.supplyAsync(
                () -> outputWithDelay("Apple",5000),executor);
        CompletionStage<String> expectedB =
            CompletableFuture.supplyAsync(
                () -> outputWithDelay("Banana",7500),executor);
        CompletionStage<String> expectedC =
            CompletableFuture.supplyAsync(
                () -> outputWithDelay("Cherry",10000),executor);
        Map<String,String> joined;

        subject.put("A",expectedA);
        subject.put("B",expectedB);
        subject.put("C",expectedC);

        joined = subject.joinAll();

        assertEquals(3,joined.size());
        assertEquals("Apple",joined.get("A"));
        assertEquals("Banana",joined.get("B"));
        assertEquals("Cherry",joined.get("C"));
    }

    @Test
    public void
    testJoinAll2() throws Exception
    {
        CompletionStage<String> expectedA =
            CompletableFuture.supplyAsync(
                () -> outputWithDelay("Apple",100),executor);
        CompletionStage<String> expectedB =
            CompletableFuture.supplyAsync(
                () -> outputWithDelay("Banana",100),executor);
        CompletionStage<String> expectedC =
            CompletableFuture.supplyAsync(
                () -> outputWithDelay("Cherry",10000),executor);
        Map<String,String> joined;

        subject.put("A",expectedA);
        subject.put("B",expectedB);
        subject.put("C",expectedC);

        Thread.sleep(500);

        joined = subject.joinAll();

        assertEquals(1,joined.size());
        assertEquals("Cherry",joined.get("C"));

    }

    @Test
    public void
    testSerialization() throws Exception
    {
        CompletionStageMap<String,String> actual = deserialize(serialize(subject));
        CompletionStage<String> expectedA =
            CompletableFuture.supplyAsync(
                () -> outputWithDelay("Apple",5000),executor);
        CompletionStage<String> expectedB =
            CompletableFuture.supplyAsync(
                () -> outputWithDelay("Banana",7500),executor);
        CompletionStage<String> expectedC =
            CompletableFuture.supplyAsync(
                () -> outputWithDelay("Cherry",10000),executor);
        Map<String,String> joined;

        actual.put("A",expectedA);
        actual.put("B",expectedB);
        actual.put("C",expectedC);

        joined = actual.joinAll();

        assertEquals(3,joined.size());
        assertEquals("Apple",joined.get("A"));
        assertEquals("Banana",joined.get("B"));
        assertEquals("Cherry",joined.get("C"));
    }

    private String
    outputWithDelay(String value,long delayMillis)
    {
        try
        {
            Thread.sleep(delayMillis);
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }

        return value;
    }

    private byte[]
    serialize(CompletionStageMap<String,String> map)
    {
        try (
            ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            ObjectOutputStream oos = new java.io.ObjectOutputStream(baos))
        {
            oos.writeObject(map);
            oos.flush();
            return baos.toByteArray();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Serialization failed: " + e.getMessage(), e);
        }
    }

    private CompletionStageMap<String,String>
    deserialize(byte[] data)
    {
        try (
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais))
        {
            return (CompletionStageMap<String,String>) ois.readObject();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Deserialization failed: " + e.getMessage(), e);
        }

    }
}

//////////////////////////////////////////////////////////////////////////////
