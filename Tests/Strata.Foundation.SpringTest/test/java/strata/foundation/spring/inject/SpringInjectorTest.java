/// ///////////////////////////////////////////////////////////////////////////
// SpringInjectorTest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.inject;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import strata.foundation.core.inject.IInjector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("CommitStage")
public
class SpringInjectorTest
{
    private static ApplicationContext context;
    private IInjector injector;

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
        injector = context.getBean(IInjector.class);
    }

    @Test
    public void
    testGetInstanceType()
    {
        ThreadNameSupplier supplier =
            injector.getInstance(ThreadNameSupplier.class);

        assertNotNull(supplier);
    }

    @Test
    public void
    testGetInstanceTypeAndQualifier()
    {
        IStringSupplier supplierA =
            injector.getInstance(IStringSupplier.class,"Supplier-A");
        IStringSupplier supplierB =
            injector.getInstance(IStringSupplier.class,"Supplier-B");
        IStringSupplier supplierC =
            injector.getInstance(IStringSupplier.class,"Supplier-C");

        assertNotNull(supplierA);
        assertNotNull(supplierB);
        assertNotNull(supplierC);

        assertEquals("Supplier A",supplierA.get());
        assertEquals("Supplier B",supplierB.get());
        assertEquals("Supplier C",supplierC.get());
    }

    @Test
    public void
    testGetInstanceTypeAndName()
    {
        IStringSupplier supplierA =
            injector.getInstance(IStringSupplier.class,"supplierA");
        IStringSupplier supplierB =
            injector.getInstance(IStringSupplier.class,"supplierB");
        IStringSupplier supplierC =
            injector.getInstance(IStringSupplier.class,"supplierC");

        assertNotNull(supplierA);
        assertNotNull(supplierB);
        assertNotNull(supplierC);

        assertEquals("Supplier A",supplierA.get());
        assertEquals("Supplier B",supplierB.get());
        assertEquals("Supplier C",supplierC.get());
    }

    @Test
    public void
    testGetInstanceTypeAndAnnotation()
    {
        IStringSupplier supplierA =
            injector.getInstance(
                IStringSupplier.class,
                Annotated.qualifier("Supplier-A"));
        IStringSupplier supplierB =
            injector.getInstance(
                IStringSupplier.class,
                Annotated.qualifier("Supplier-B"));
        IStringSupplier supplierC =
            injector.getInstance(
                IStringSupplier.class,
                Annotated.qualifier("Supplier-C"));
        IStringSupplier supplierD =
            injector.getInstance(
                IStringSupplier.class,
                Annotated.named("Supplier-D"));

        assertNotNull(supplierA);
        assertNotNull(supplierB);
        assertNotNull(supplierC);
        assertNotNull(supplierD);

        assertEquals("Supplier A",supplierA.get());
        assertEquals("Supplier B",supplierB.get());
        assertEquals("Supplier C",supplierC.get());
        assertEquals("Supplier D",supplierD.get());
    }

}

//////////////////////////////////////////////////////////////////////////////
