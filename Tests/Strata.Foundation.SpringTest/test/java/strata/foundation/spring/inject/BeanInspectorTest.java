/// ///////////////////////////////////////////////////////////////////////////
// BeanInspectorTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.inject;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import strata.foundation.core.container.Pair;
import strata.foundation.core.inject.IInjector;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Tag("CommitStage")
public
class BeanInspectorTest
{
    private static ApplicationContext context;
    private BeanInspector inspector;

    @BeforeAll
    public static void
    setUpAll() throws Exception
    {
        context =
            new AnnotationConfigApplicationContext(TestConfiguration.class);
    }

    @BeforeEach
    public void
    setUp() throws Exception
    {
        inspector = new BeanInspector(context);
    }

    @Test
    public void
    testGetQualifiedBeanNames()
    {
        Map<Pair<Class<?>,String>,String> qualifiedBeanNames =
            inspector.getQualifiedBeanNames();
        Pair<Class<?>,String> keyA =
            Pair.create(IStringSupplier.class,"Supplier-A");
        Pair<Class<?>,String> keyB =
            Pair.create(IStringSupplier.class,"Supplier-B");
        Pair<Class<?>,String> keyC =
            Pair.create(IStringSupplier.class,"Supplier-C");

        assertFalse(qualifiedBeanNames.isEmpty());

        System.out.println(qualifiedBeanNames);

        assertTrue(qualifiedBeanNames.containsKey(keyA));
        assertEquals("supplierA",qualifiedBeanNames.get(keyA));

        assertTrue(qualifiedBeanNames.containsKey(keyB));
        assertEquals("supplierB",qualifiedBeanNames.get(keyB));

        assertTrue(qualifiedBeanNames.containsKey(keyC));
        assertEquals("supplierC",qualifiedBeanNames.get(keyC));
    }

}

//////////////////////////////////////////////////////////////////////////////
