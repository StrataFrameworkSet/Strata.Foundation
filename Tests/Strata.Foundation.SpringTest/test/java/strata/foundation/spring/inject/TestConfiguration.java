/// ///////////////////////////////////////////////////////////////////////////
// TestConfiguration.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.inject;

import jakarta.inject.Named;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import strata.foundation.core.inject.IInjector;

@Configuration
@Import({ScopeConfiguration.class})
public
class TestConfiguration
{
    @Bean
    @ThreadScoped
    public ThreadNameSupplier
    threadNameSupplier()
    {
        return new ThreadNameSupplier();
    }

    @Bean
    @OperationScoped
    public OperationNameSupplier
    operationNameSupplier()
    {
        return new OperationNameSupplier();
    }

    @Bean
    @PrototypeScoped
    public Operation
    operation()
    {
        return new Operation();
    }

    @Bean
    @SingletonScoped
    @Qualifier("Supplier-A")
    public IStringSupplier
    supplierA()
    {
        return () -> "Supplier A";
    }

    @Bean
    @SingletonScoped
    @Qualifier("Supplier-B")
    public IStringSupplier
    supplierB()
    {
        return () -> "Supplier B";
    }

    @Bean
    @SingletonScoped
    @Qualifier("Supplier-C")
    public IStringSupplier
    supplierC()
    {
        return () -> "Supplier C";
    }

    @Bean
    @SingletonScoped
    @Named("Supplier-D")
    public IStringSupplier
    supplierD()
    {
        return () -> "Supplier D";
    }

    @Bean
    @SingletonScoped
    public IInjector
    injector()
    {
        return new SpringInjector();
    }
}

//////////////////////////////////////////////////////////////////////////////
