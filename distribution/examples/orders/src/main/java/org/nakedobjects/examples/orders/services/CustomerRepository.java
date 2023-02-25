package org.nakedobjects.examples.orders.services;

import java.util.List;


import org.nakedobjects.applib.AbstractFactoryAndRepository;
import org.nakedobjects.applib.Filter;
import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.applib.annotation.Named;
import org.nakedobjects.examples.orders.domain.Customer;


@Named("Customers")
public class CustomerRepository extends AbstractFactoryAndRepository {

    // use ctrl+space to bring up the NO templates.

    // also, use CoffeeBytes code folding with
    // user-defined regions of {{ and }}

    // {{ showAll
    /**
     * Lists all products in the repository.
     */
    public List<Customer> showAll() {
        return allInstances(Customer.class);
    }

    // }}

    // {{ findAllByName, findByName
    /**
     * Returns a list of Customers with given last name.
     */
    public List<Customer> findAllByName(@Named("Last name") final String lastName) {
        return allMatches(Customer.class, new FilterLastName(lastName));
    }

    /**
     * Returns the first Customer with given last name.
     */
    public Customer findByName(@Named("Last name") final String lastName) {
        return firstMatch(Customer.class, new FilterLastName(lastName));
    }

    private final class FilterLastName implements Filter<Customer> {
        private final String name;

        private FilterLastName(String name) {
            this.name = name;
        }

        public boolean accept(Customer pojo) {
            return pojo.getLastName().toLowerCase().contains(name.toLowerCase());
        }
    }

    // }}

    // {{ newCustomer
    /**
     * Creates a new (still-transient) customer.
     */
    public Customer newCustomer() {
        Customer customer = (Customer) newTransientInstance(Customer.class);
        return customer;
    }

    /**
     * Creates a new (already persisted) customer.
     * 
     * <p>
     * For use by fixtures only.
     */
    @Hidden
    public Customer newCustomer(String firstName, String lastName, int customerNumber) {
        Customer customer = newCustomer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setCustomerNumber(customerNumber);
        persist(customer);
        return customer;
    }

    // }}

    // {{ identification
    /**
     * Use <tt>Customer.gif</tt> for icon.
     */
    public String iconName() {
        return "Customer";
    }
    // }}

}
