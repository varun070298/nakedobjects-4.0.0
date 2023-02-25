package org.nakedobjects.plugins.headless.junit.sample.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.nakedobjects.applib.AbstractFactoryAndRepository;
import org.nakedobjects.applib.Filter;
import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.applib.annotation.Named;
import org.nakedobjects.plugins.headless.junit.sample.domain.Customer;
import org.nakedobjects.plugins.headless.junit.sample.domain.Product;


@Named("Products")
public class ProductRepository extends AbstractFactoryAndRepository {

    // use ctrl+space to bring up the NO templates.

    // also, use CoffeeBytes code folding with
    // user-defined regions of {{ and }}

    // {{ Logger
    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(ProductRepository.class);

    // }}

    /**
     * Lists all products in the repository.
     */
    public List<Product> showAll() {
        return allInstances(Product.class);
    }

    // {{ findByCode
    /**
     * Returns the Product with given code
     */
    public Product findByCode(@Named("Code")
    final String code) {
        return firstMatch(Product.class, new Filter<Product>() {
            public boolean accept(final Product product) {
                return code.equals(product.getCode());
            }
        });
    }

    // }}

    /**
     * Creates a new product.
     * 
     * <p>
     * For use by fixtures only.
     * 
     * @return
     */
    @Hidden
    public Product newProduct(final String code, final String description, final int priceInPence) {
        final Product product = newTransientInstance(Product.class);
        product.setCode(code);
        product.setDescription(description);
        product.setPrice(new Double(priceInPence / 100));
        persist(product);
        return product;
    }

    /**
     * Creates a new still transient product.
     * 
     * <p>
     * For use by tests only. Using this rather than {@link Customer} since {@link Product} has a
     * {@link Product#validate()} method.
     * 
     * @return
     */
    @Hidden
    public Product newProduct() {
        return newTransientInstance(Product.class);
    }

}
