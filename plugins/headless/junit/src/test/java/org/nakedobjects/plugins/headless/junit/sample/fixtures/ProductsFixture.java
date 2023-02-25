package org.nakedobjects.plugins.headless.junit.sample.fixtures;

import org.apache.log4j.Logger;
import org.nakedobjects.applib.fixtures.AbstractFixture;
import org.nakedobjects.plugins.headless.junit.sample.service.ProductRepository;


public class ProductsFixture extends AbstractFixture {

    // use ctrl+space to bring up the NO templates.

    // also, use CoffeeBytes code folding with
    // user-defined regions of {{ and }}

    // {{ Logger
    private final static Logger LOGGER = Logger.getLogger(ProductsFixture.class);

    public Logger getLOGGER() {
        return LOGGER;
    }

    // }}

    @Override
    public void install() {
        getLOGGER().debug("installing");
        getProductRepository().newProduct("355-40311", "Weekend camping pack", 5000);
        getProductRepository().newProduct("850-18003", "Stripy Wasp Catcher", 695);
        getProductRepository().newProduct("845-06203", "Combi Backpack Hamper", 5900);
        getProductRepository().newProduct("820-72721", "Folding Table", 4000);
        getProductRepository().newProduct("820-72725", "Folding Chair", 2500);
        getProductRepository().newProduct("845-01020", "Isotherm Cool Box", 2500);
    }

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
