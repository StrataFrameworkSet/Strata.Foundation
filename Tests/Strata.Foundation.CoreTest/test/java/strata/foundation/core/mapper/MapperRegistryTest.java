//////////////////////////////////////////////////////////////////////////////
// MapperRegistryTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.mapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Tag("CommitStage")
public
class MapperRegistryTest
{
    private MapperRegistry target;

    @BeforeEach
    public void
    setUp()
    {
        target = MapperRegistry.of();
        clearKnownMappings(target);
    }

    @AfterEach
    public void
    tearDown()
    {
        clearKnownMappings(target);
        target = null;
    }

    @Test
    public void
    testOfReturnsNonNullInstance()
    {
        assertNotNull(MapperRegistry.of());
    }

    @Test
    public void
    testOfReturnsNewInstanceEachCall()
    {
        MapperRegistry first  = MapperRegistry.of();
        MapperRegistry second = MapperRegistry.of();

        assertNotSame(first,second);
    }

    @Test
    public void
    testConstructorProducesUsableInstance()
    {
        MapperRegistry registry = new MapperRegistry();

        assertFalse(registry.has(Integer.class,String.class));
    }

    @Test
    public void
    testRegisterStoresMapper()
    {
        IMapper<Integer,String> mapper = Integer::valueOf;

        target.register(Integer.class,String.class,mapper);

        assertTrue(target.has(Integer.class,String.class));
    }

    @Test
    public void
    testRegisterReturnsThisForChaining()
    {
        IMapper<Integer,String> mapper = Integer::valueOf;

        MapperRegistry returned =
            target.register(Integer.class,String.class,mapper);

        assertSame(target,returned);
    }

    @Test
    public void
    testRegisterOverwritesPreviousMapper()
    {
        IMapper<Integer,String> first  = s -> 1;
        IMapper<Integer,String> second = s -> 2;

        target.register(Integer.class,String.class,first);
        target.register(Integer.class,String.class,second);

        Optional<IMapper<Integer,String>> actual =
            target.get(Integer.class,String.class);

        assertTrue(actual.isPresent());
        assertSame(second,actual.get());
        assertEquals(2,actual.get().map("ignored"));
    }

    @Test
    public void
    testUnregisterRemovesMapping()
    {
        target.register(Integer.class,String.class,Integer::valueOf);

        target.unregister(Integer.class,String.class);

        assertFalse(target.has(Integer.class,String.class));
        assertTrue(target.get(Integer.class,String.class).isEmpty());
    }

    @Test
    public void
    testUnregisterReturnsThisForChaining()
    {
        target.register(Integer.class,String.class,Integer::valueOf);

        MapperRegistry returned =
            target.unregister(Integer.class,String.class);

        assertSame(target,returned);
    }

    @Test
    public void
    testUnregisterOfUnregisteredMappingIsNoOp()
    {
        assertDoesNotThrow(
            () -> target.unregister(Integer.class,String.class));
        assertFalse(target.has(Integer.class,String.class));
    }

    @Test
    public void
    testHasReturnsFalseWhenMappingAbsent()
    {
        assertFalse(target.has(Integer.class,String.class));
    }

    @Test
    public void
    testHasReturnsTrueWhenMappingRegistered()
    {
        target.register(Integer.class,String.class,Integer::valueOf);

        assertTrue(target.has(Integer.class,String.class));
    }

    @Test
    public void
    testGetReturnsRegisteredMapper()
    {
        IMapper<Integer,String> mapper = Integer::valueOf;

        target.register(Integer.class,String.class,mapper);

        Optional<IMapper<Integer,String>> actual =
            target.get(Integer.class,String.class);

        assertTrue(actual.isPresent());
        assertSame(mapper,actual.get());
    }

    @Test
    public void
    testGetReturnsEmptyWhenMappingAbsent()
    {
        Optional<IMapper<Integer,String>> actual =
            target.get(Integer.class,String.class);

        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void
    testGetMapperFunctionsCorrectly()
    {
        target.register(Integer.class,String.class,Integer::valueOf);

        Optional<IMapper<Integer,String>> mapper =
            target.get(Integer.class,String.class);

        assertTrue(mapper.isPresent());
        assertEquals(Integer.valueOf(42),mapper.get().map("42"));
    }

    @Test
    public void
    testTypeDirectionalityIsDistinct()
    {
        IMapper<Integer,String> stringToInteger = Integer::valueOf;
        IMapper<String,Integer> integerToString = Object::toString;

        target.register(Integer.class,String.class,stringToInteger);
        target.register(String.class,Integer.class,integerToString);

        Optional<IMapper<Integer,String>> forward =
            target.get(Integer.class,String.class);
        Optional<IMapper<String,Integer>> reverse =
            target.get(String.class,Integer.class);

        assertTrue(forward.isPresent());
        assertTrue(reverse.isPresent());
        assertSame(stringToInteger,forward.get());
        assertSame(integerToString,reverse.get());
        assertNotSame(forward.get(),reverse.get());
    }

    @Test
    public void
    testUnregisterOneDirectionLeavesOtherIntact()
    {
        target.register(Integer.class,String.class,Integer::valueOf);
        target.register(String.class,Integer.class,Object::toString);

        target.unregister(Integer.class,String.class);

        assertFalse(target.has(Integer.class,String.class));
        assertTrue(target.has(String.class,Integer.class));
    }

    @Test
    public void
    testMultipleDistinctRegistrationsCoexist()
    {
        IMapper<Integer,String>     stringToInteger = Integer::valueOf;
        IMapper<Long,String>        stringToLong    = Long::valueOf;
        IMapper<Double,Number>      numberToDouble  = Number::doubleValue;
        IMapper<String,CharSequence> charSeqToString = CharSequence::toString;

        target
            .register(Integer.class,String.class,stringToInteger)
            .register(Long.class,String.class,stringToLong)
            .register(Double.class,Number.class,numberToDouble)
            .register(String.class,CharSequence.class,charSeqToString);

        assertSame(
            stringToInteger,
            target.get(Integer.class,String.class).orElseThrow());
        assertSame(
            stringToLong,
            target.get(Long.class,String.class).orElseThrow());
        assertSame(
            numberToDouble,
            target.get(Double.class,Number.class).orElseThrow());
        assertSame(
            charSeqToString,
            target.get(String.class,CharSequence.class).orElseThrow());
    }

    @Test
    public void
    testFluentChainOfOperations()
    {
        IMapper<Integer,String> stringToInteger = Integer::valueOf;
        IMapper<Long,String>    stringToLong    = Long::valueOf;

        MapperRegistry returned =
            target
                .register(Integer.class,String.class,stringToInteger)
                .register(Long.class,String.class,stringToLong)
                .unregister(Integer.class,String.class);

        assertSame(target,returned);
        assertFalse(target.has(Integer.class,String.class));
        assertTrue(target.has(Long.class,String.class));
    }

    @Test
    public void
    testBackingStoreSharedAcrossInstances()
    {
        MapperRegistry other = MapperRegistry.of();
        IMapper<Integer,String> mapper = Integer::valueOf;

        target.register(Integer.class,String.class,mapper);

        Optional<IMapper<Integer,String>> fromOther =
            other.get(Integer.class,String.class);

        assertTrue(other.has(Integer.class,String.class));
        assertTrue(fromOther.isPresent());
        assertSame(mapper,fromOther.get());

        other.unregister(Integer.class,String.class);

        assertFalse(target.has(Integer.class,String.class));
    }

    private static void
    clearKnownMappings(MapperRegistry registry)
    {
        registry.unregister(Integer.class,String.class);
        registry.unregister(Long.class,String.class);
        registry.unregister(Double.class,Number.class);
        registry.unregister(String.class,CharSequence.class);
        registry.unregister(String.class,Integer.class);
    }
}

//////////////////////////////////////////////////////////////////////////////
