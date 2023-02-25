package org.nakedobjects.plugins.headless.junit.sample.fixtures;

import org.apache.log4j.Logger;
import org.nakedobjects.applib.fixtures.AbstractFixture;
import org.nakedobjects.plugins.headless.junit.sample.domain.Customer;
import org.nakedobjects.plugins.headless.junit.sample.domain.Product;
import org.nakedobjects.plugins.headless.junit.sample.service.CustomerRepository;
import org.nakedobjects.plugins.headless.junit.sample.service.ProductRepository;


public class CustomerOrdersFixture extends AbstractFixture {

    // use ctrl+space to bring up the NO templates.

    // also, use CoffeeBytes code folding with
    // user-defined regions of {{ and }}

    // {{ Logger
    private final static Logger LOGGER = Logger.getLogger(CustomerOrdersFixture.class);

    public Logger getLOGGER() {
        return LOGGER;
    }

    // }}

    @Override
    public void install() {
        getLOGGER().debug("installing");
        final Customer richard = getCustomerRepository().findByName("Pawson");
        final Product foldingTable = getProductRepository().findByCode("820-72721");
        final Product foldingChair = getProductRepository().findByCode("820-72725");
        final Product waspCatcher = getProductRepository().findByCode("850-18003");
        final Product coolbox = getProductRepository().findByCode("845-01020");

        setDate(2007, 4, 11);
        setTime(10, 15);
        richard.placeOrder(foldingTable, 1);
        setDate(2007, 4, 12);
        setTime(9, 35);
        richard.placeOrder(foldingChair, 6);
        setDate(2007, 4, 13);
        setTime(14, 20);
        richard.placeOrder(waspCatcher, 1);
        setDate(2007, 4, 14);
        setTime(11, 10);
        richard.placeOrder(coolbox, 1);
    }

    // {{ Injected: CustomerRepository
    private CustomerRepository customerRepository;

    /**
     * This field is not persisted, nor displayed to the user.
     */
    protected CustomerRepository getCustomerRepository() {
        return this.customerRepository;
    }

    /**
     * Injected by the application container.
     */
    public void setCustomerRepository(final CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    // }}

    // {{ Injected: ProductRepository
    private ProductRepository productRepository;

    /**
     * This field is not persisted, nor displayed to the user.
     */
    protected ProductRepository getProductRepository() {
        return this.productRepository;
    }

    /**
     * Injected by the application container.
     */
    public void setProductRepository(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    // }}

}
