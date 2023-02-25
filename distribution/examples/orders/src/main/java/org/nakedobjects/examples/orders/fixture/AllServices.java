package org.nakedobjects.examples.orders.fixture;

import java.util.Vector;

import org.nakedobjects.examples.orders.services.CustomerRepository;
import org.nakedobjects.examples.orders.services.ProductRepository;


public class AllServices extends Vector<Object> {

	private static final long serialVersionUID = 1L;

	public AllServices() {
		add(new ProductRepository());
		add(new CustomerRepository());
	}
}
