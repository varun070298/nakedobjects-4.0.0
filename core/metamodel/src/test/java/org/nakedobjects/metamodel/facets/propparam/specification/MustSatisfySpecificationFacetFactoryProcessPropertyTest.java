package org.nakedobjects.metamodel.facets.propparam.specification;

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
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;

@RunWith(JMock.class)
public class MustSatisfySpecificationFacetFactoryProcessPropertyTest {

    private Mockery mockery = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    
    private MustSatisfySpecificationFacetFactory facetFactory;
    private MethodRemover mockMethodRemover;
    private FacetHolder mockFacetHolder;

    private Class<DomainObjectWithoutMustSatisfyAnnotations> domainObjectClassWithoutAnnotation;
    private Class<DomainObjectWithMustSatisfyAnnotations> domainObjectClassWithAnnotation;
    private Method firstNameMethodWithout;
    private Method firstNameMethodWith;

    @Before
    public void setUp() throws Exception {
        facetFactory = new MustSatisfySpecificationFacetFactory();
        mockMethodRemover = mockery.mock(MethodRemover.class);
        mockFacetHolder = mockery.mock(FacetHolder.class);
        domainObjectClassWithoutAnnotation = DomainObjectWithoutMustSatisfyAnnotations.class;
        domainObjectClassWithAnnotation = DomainObjectWithMustSatisfyAnnotations.class;
        firstNameMethodWithout = domainObjectClassWithoutAnnotation.getMethod("getFirstName");
        firstNameMethodWith = domainObjectClassWithAnnotation.getMethod("getFirstName");
    }

    @After
    public void tearDown() throws Exception {
        facetFactory = null;
        mockMethodRemover = null;
        mockFacetHolder = null;
    }

    @Test
    public void addsAMustSatisfySpecificationFacetIfAnnotated() {
        mockery.checking(new Expectations() {{
            one(mockFacetHolder).addFacet(with(anInstanceOf(MustSatisfySpecificationFacet.class)));
        }});
        facetFactory.process(domainObjectClassWithAnnotation.getClass(), firstNameMethodWith, mockMethodRemover, mockFacetHolder);
    }

    @Test
    public void doesNotAddsAMustSatisfySpecificationFacetIfNotAnnotated() {
        mockery.checking(new Expectations() {{
            never(mockFacetHolder).addFacet(with(anInstanceOf(MustSatisfySpecificationFacet.class)));
        }});
        facetFactory.process(domainObjectClassWithAnnotation.getClass(), firstNameMethodWithout, mockMethodRemover, mockFacetHolder);
    }

}
