package org.nakedobjects.plugins.headless.junit;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.nakedobjects.applib.DomainObjectContainer;
import org.nakedobjects.plugins.headless.applib.HeadlessViewer;
import org.nakedobjects.plugins.headless.applib.listeners.InteractionListener;
import org.nakedobjects.plugins.headless.junit.Fixture;
import org.nakedobjects.plugins.headless.junit.Fixtures;
import org.nakedobjects.plugins.headless.junit.NakedObjectsTestRunner;
import org.nakedobjects.plugins.headless.junit.Service;
import org.nakedobjects.plugins.headless.junit.Services;
import org.nakedobjects.plugins.headless.junit.sample.domain.Country;
import org.nakedobjects.plugins.headless.junit.sample.domain.Customer;
import org.nakedobjects.plugins.headless.junit.sample.domain.Product;
import org.nakedobjects.plugins.headless.junit.sample.fixtures.CountriesFixture;
import org.nakedobjects.plugins.headless.junit.sample.fixtures.CustomerOrdersFixture;
import org.nakedobjects.plugins.headless.junit.sample.fixtures.CustomersFixture;
import org.nakedobjects.plugins.headless.junit.sample.fixtures.ProductsFixture;
import org.nakedobjects.plugins.headless.junit.sample.service.CountryRepository;
import org.nakedobjects.plugins.headless.junit.sample.service.CustomerRepository;
import org.nakedobjects.plugins.headless.junit.sample.service.OrderRepository;
import org.nakedobjects.plugins.headless.junit.sample.service.ProductRepository;


@RunWith(NakedObjectsTestRunner.class)
@Fixtures({
	@Fixture(CountriesFixture.class),
	@Fixture(ProductsFixture.class),
	@Fixture(CustomersFixture.class),
	@Fixture(CustomerOrdersFixture.class)
})
@Services({
	@Service(CountryRepository.class),
	@Service(ProductRepository.class),
	@Service(CustomerRepository.class),
	@Service(OrderRepository.class)
})
public abstract class AbstractTest {
	
    protected Customer custJsDO;
    protected Customer custJsVO;

    protected Product product355DO;
    protected Product product355VO;

    protected Product product850DO;

    protected Country countryGbrDO;
    protected Country countryGbrVO;

    protected Country countryUsaDO;
    protected Country countryAusDO;

    private ProductRepository productRepository;
    private CustomerRepository customerRepository;
    private CountryRepository countryRepository;
    
    private DomainObjectContainer domainObjectContainer;
    private HeadlessViewer headlessViewer;
    
    private InteractionListener interactionListener;

    @Before
    public void setUp() {
    	
        product355DO = productRepository.findByCode("355-40311");
        product355VO = headlessViewer.view(product355DO);
        product850DO = productRepository.findByCode("850-18003");

        countryGbrDO = countryRepository.findByCode("GBR");
        countryGbrVO = headlessViewer.view(countryGbrDO);

        countryUsaDO = countryRepository.findByCode("USA");
        countryAusDO = countryRepository.findByCode("AUS");

        custJsDO = customerRepository.findByName("Pawson");
        custJsVO = headlessViewer.view(custJsDO);
    }

    @After
    public void tearDown() {
    }

    protected InteractionListener getInteractionListener() {
        return interactionListener;
    }

    
    ////////////////////////////////////////////////////////
    // Injected.
    ////////////////////////////////////////////////////////
    
    protected HeadlessViewer getHeadlessViewer() {
        return headlessViewer;
    }
    public void setHeadlessViewer(HeadlessViewer headlessViewer) {
    	this.headlessViewer = headlessViewer;
    }

    protected DomainObjectContainer getDomainObjectContainer() {
        return domainObjectContainer;
    }

    public void setDomainObjectContainer(final DomainObjectContainer domainObjectContainer) {
        this.domainObjectContainer = domainObjectContainer;
    }


    protected ProductRepository getProductRepository() {
        return productRepository;
    }

    public void setProductRepository(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    protected CustomerRepository getCustomerRepository() {
        return customerRepository;
    }

    public void setCustomerRepository(final CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    protected CountryRepository getCountryRepository() {
        return countryRepository;
    }

    public void setCountryRepository(final CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }



    

}
