package org.nakedobjects.metamodel.examples.facets.jsr303;

import static org.junit.Assert.fail;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.examples.facets.jsr303.Jsr303FacetFactory;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;


@RunWith(JMock.class)
public class Jsr303FacetFactoryProcessObject {

    private Mockery mockery = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    
    private Jsr303FacetFactory facetFactory;
    private MethodRemover mockMethodRemover;
    private FacetHolder mockFacetHolder;

    private Class<DomainObjectVanilla> domainObjectClass;

    @Before
    public void setUp() throws Exception {
        facetFactory = new Jsr303FacetFactory();
        mockMethodRemover = mockery.mock(MethodRemover.class);
        mockFacetHolder = mockery.mock(FacetHolder.class);
        domainObjectClass = DomainObjectVanilla.class;
    }

    @After
    public void tearDown() throws Exception {
        facetFactory = null;
        mockMethodRemover = null;
        mockFacetHolder = null;
    }

    @Ignore("TODO")
    @Test
    public void process() {
        facetFactory.process(domainObjectClass, mockMethodRemover, mockFacetHolder);
        fail();
    }


}
