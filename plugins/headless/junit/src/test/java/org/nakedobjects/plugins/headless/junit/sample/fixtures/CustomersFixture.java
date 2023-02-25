package org.nakedobjects.plugins.headless.junit.sample.fixtures;

import org.apache.log4j.Logger;
import org.nakedobjects.applib.fixtures.AbstractFixture;
import org.nakedobjects.plugins.headless.junit.sample.domain.Country;
import org.nakedobjects.plugins.headless.junit.sample.service.CountryRepository;
import org.nakedobjects.plugins.headless.junit.sample.service.CustomerRepository;


public class CustomersFixture extends AbstractFixture {

    // {{ Logger
    private final static Logger LOGGER = Logger.getLogger(CustomersFixture.class);

    public Logger getLOGGER() {
        return LOGGER;
    }

    // }}

    @Override
    public void install() {
        getLOGGER().debug("installing");
        final Country countryGBR = getCountryRepository().findByCode("GBR");
        getCustomerRepository().newCustomer("Richard", "Pawson", 1, countryGBR);
        getCustomerRepository().newCustomer("Robert", "Matthews", 2, countryGBR);
        getCustomerRepository().newCustomer("Dan", "Haywood", 3, countryGBR);
        getCustomerRepository().newCustomer("Stef", "Cascarini", 4, countryGBR);
        getCustomerRepository().newCustomer("Dave", "Slaughter", 5, countryGBR);
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

    // {{ Injected: CountryRepository
    private CountryRepository countryRepository;

    /**
     * This field is not persisted, nor displayed to the user.
     */
    protected CountryRepository getCountryRepository() {
        return this.countryRepository;
    }

    /**
     * Injected by the application container.
     */
    public void setCountryRepository(final CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }
    // }}

}
