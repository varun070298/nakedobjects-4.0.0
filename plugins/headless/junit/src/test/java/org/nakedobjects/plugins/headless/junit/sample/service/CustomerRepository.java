package org.nakedobjects.plugins.headless.junit.sample.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.nakedobjects.applib.AbstractFactoryAndRepository;
import org.nakedobjects.applib.Filter;
import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.applib.annotation.Named;
import org.nakedobjects.plugins.headless.junit.sample.domain.Country;
import org.nakedobjects.plugins.headless.junit.sample.domain.Customer;


@Named("Customers")
public class CustomerRepository extends AbstractFactoryAndRepository {

    // use ctrl+space to bring up the NO templates.

    // also, use CoffeeBytes code folding with
    // user-defined regions of {{ and }}

    // {{ Logger
    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(CustomerRepository.class);

    // }}

    /**
     * Lists all customers in the repository.
     */
    public List<Customer> showAll() {
        return allInstances(Customer.class);
    }

    // {{ findAllByName, findByName
    /**
     * Returns a list of Customers with given last name.
     */
    public List<Customer> findAllByName(@Named("Last name")
    final String lastName) {
        return allMatches(Customer.class, new FilterLastName(lastName));
    }

    /**
     * Returns the first Customer with given last name.
     */
    public Customer findByName(@Named("Last name")
    final String lastName) {
        Customer firstMatch = firstMatch(Customer.class, new FilterLastName(lastName));
        return firstMatch;
    }

    private final class FilterLastName implements Filter<Customer> {
        private final String name;

        private FilterLastName(final String name) {
            this.name = name;
        }

        public boolean accept(final Customer customer) {
            return customer.getLastName().toLowerCase().contains(name.toLowerCase());
        }
    }

    // }}

    /**
     * Creates a new (still-transient) customer.
     * 
     * @return
     */
    public Customer newCustomer() {
        final Customer customer = newTransientInstance(Customer.class);
        return customer;
    }

    /**
     * Creates a new (already persisted) customer.
     * 
     * <p>
     * For use by fixtures only.
     * 
     * @return
     */
    @Hidden
    public Customer newCustomer(
            final String firstName,
            final String lastName,
            final int customerNumber,
            final Country countryOfBirth) {

        final Customer customer = newCustomer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setCustomerNumber(customerNumber);
        customer.modifyCountryOfBirth(countryOfBirth);

        persist(customer);
        return customer;
    }

}
