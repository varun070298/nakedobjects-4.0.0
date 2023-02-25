package org.nakedobjects.plugins.headless.junit.sample.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.nakedobjects.applib.AbstractFactoryAndRepository;
import org.nakedobjects.applib.annotation.Named;
import org.nakedobjects.plugins.headless.junit.sample.domain.Customer;
import org.nakedobjects.plugins.headless.junit.sample.domain.Order;


@Named("Orders")
public class OrderRepository extends AbstractFactoryAndRepository {

    // use ctrl+space to bring up the NO templates.

    // also, use CoffeeBytes code folding with
    // user-defined regions of {{ and }}

    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(OrderRepository.class);

    // {{ findRecentOrders
    public List<Order> findRecentOrders(final Customer customer, @Named("Number of Orders")
    final Integer numberOfOrders) {
        final List<Order> orders = customer.getOrders();
        Collections.sort(orders, new Comparator<Order>() {
            public int compare(final Order o1, final Order o2) {
                final long time1 = o1.getOrderDate().getTime();
                final long time2 = o2.getOrderDate().getTime();
                return (int) (time2 - time1);
            }
        });
        if (orders.size() < numberOfOrders) {
            return orders;
        } else {
            return orders.subList(0, numberOfOrders);
        }
    }

    public Object[] defaultFindRecentOrders(final Customer customer, final Integer numberOfOrders) {
        return new Object[] { null, new Integer(3) };
    }
    // }}

}
