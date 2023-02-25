package org.nakedobjects.examples.orders.fixture;

import java.util.ArrayList;
import java.util.List;

import org.nakedobjects.applib.fixtures.CompositeFixture;


public class AllFixtures implements CompositeFixture {

    public List<Object> getFixtures() {
        List<Object> list = new ArrayList<Object>();
        list.add(new JoeBloggsFixture());
        list.add(new ProductsFixture());
        list.add(new CustomersFixture());
        list.add(new CustomerOrdersFixture());
        return list;
    }

}
