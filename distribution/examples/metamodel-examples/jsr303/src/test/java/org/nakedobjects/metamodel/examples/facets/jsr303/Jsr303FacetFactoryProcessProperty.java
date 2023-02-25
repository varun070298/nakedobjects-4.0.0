package org.nakedobjects.metamodel.examples.facets.jsr303;

import static org.nakedobjects.metamodel.commons.matchers.NofMatchers.anInstanceOf;

import java.lang.reflect.Method;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.examples.facets.jsr303.Jsr303FacetFactory;
import org.nakedobjects.metamodel.examples.facets.jsr303.Jsr303PropertyValidationFacet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;

@RunWith(JMock.class)
public class Jsr303FacetFactoryProcessProperty {

    private Mockery mockery = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    
    private Jsr303FacetFactory facetFactory;
    private MethodRemover mockMethodRemover;
    private FacetHolder mockFacetHolder;

    private Class<DomainObjectVanilla> domainObjectClass;
    private Method firstNameMethod;

    @Before
    public void setUp() throws Exception {
        facetFactory = new Jsr303FacetFactory();
        mockMethodRemover = mockery.mock(MethodRemover.class);
        mockFacetHolder = mockery.mock(FacetHolder.class);
        domainObjectClass = DomainObjectVanilla.class;
        firstNameMethod = domainObjectClass.getMethod("getFirstName");
    }

    @After
    public void tearDown() throws Exception {
        facetFactory = null;
        mockMethodRemover = null;
        mockFacetHolder = null;
    }

    @Test
    public void alwaysAddsAJsr303FacetToHolder() {
        mockery.checking(new Expectations() {{
            one(mockFacetHolder).addFacet(with(anInstanceOf(Jsr303PropertyValidationFacet.class)));
        }});

        facetFactory.process(firstNameMethod, mockMethodRemover, mockFacetHolder);
    }


}
