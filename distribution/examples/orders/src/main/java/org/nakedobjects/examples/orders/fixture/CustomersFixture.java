package org.nakedobjects.examples.orders.fixture;

import org.apache.log4j.Logger;
import org.nakedobjects.applib.fixtures.AbstractFixture;
import org.nakedobjects.examples.orders.services.CustomerRepository;


public class CustomersFixture extends AbstractFixture {

    // use ctrl+space to bring up the NO templates.
    
    // also, use CoffeeBytes code folding with
    // user-defined regions of {{ and }}

    
    // {{ Logger
    private final static Logger LOGGER = Logger.getLogger(CustomersFixture.class);
    public Logger getLOGGER() {
        return LOGGER;
    }
    // }}

    public void install() {
        getCustomerRepository().newCustomer("Richard", "Pawson", 1);
        getCustomerRepository().newCustomer("Robert", "Matthews", 2);
        getCustomerRepository().newCustomer("Dan", "Haywood", 3);
        getCustomerRepository().newCustomer("Stef", "Cascarini", 4);
        getCustomerRepository().newCustomer("Dave", "Slaughter", 5);
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
    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    // }}
    

}