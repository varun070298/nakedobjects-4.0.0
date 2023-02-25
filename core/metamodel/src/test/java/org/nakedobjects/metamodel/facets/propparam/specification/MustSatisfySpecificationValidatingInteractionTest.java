package org.nakedobjects.metamodel.facets.propparam.specification;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.interactions.PropertyModifyContext;
import org.nakedobjects.metamodel.spec.identifier.Identified;


@RunWith(JMock.class)
public class MustSatisfySpecificationValidatingInteractionTest {

    private final Mockery mockery = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    private MustSatisfySpecificationFacet facetForSpecificationAlwaysSatisfied;
    private MustSatisfySpecificationFacet facetForSpecificationNeverSatisfied;
    private FacetHolder mockHolder;

    private PropertyModifyContext mockContext;
    
    private NakedObject mockProposedNakedObject;
    private Object mockProposedObject;

    private SpecificationAlwaysSatisfied specificationAlwaysSatisfied;
    private SpecificationNeverSatisfied specificationNeverSatisfied;
    
    @Before
    public void setUp() throws Exception {
        mockHolder = mockery.mock(Identified.class);
        specificationAlwaysSatisfied = new SpecificationAlwaysSatisfied();
        specificationNeverSatisfied = new SpecificationNeverSatisfied();
        
        facetForSpecificationAlwaysSatisfied = new MustSatisfySpecificationFacet(Utils.listOf(specificationAlwaysSatisfied), mockHolder);
        facetForSpecificationNeverSatisfied = new MustSatisfySpecificationFacet(Utils.listOf(specificationNeverSatisfied), mockHolder);

        mockContext = mockery.mock(PropertyModifyContext.class);
        mockProposedNakedObject = mockery.mock(NakedObject.class, "proposed");
        mockProposedObject = mockery.mock(Object.class, "proposedObject");

        mockery.checking(new Expectations() {
            {
                one(mockContext).getProposed();
                will(returnValue(mockProposedNakedObject));

                one(mockProposedNakedObject).getObject();
                will(returnValue(mockProposedObject));
            }
        });
    }

    @After
    public void tearDown() throws Exception {
        mockHolder = null;
        facetForSpecificationAlwaysSatisfied = null;
        facetForSpecificationNeverSatisfied = null;
        mockContext = null;
    }

    @Test
    public void validatesWhenSpecificationDoesNotVeto() {
        final String reason = facetForSpecificationAlwaysSatisfied.invalidates(mockContext);
        assertThat(reason, is(nullValue()));
    }

    @Test
    public void invalidatesWhenSpecificationVetoes() {
        final String reason = facetForSpecificationNeverSatisfied.invalidates(mockContext);
        assertThat(reason, is(not(nullValue())));
        assertThat(reason, is("not satisfied"));
    }


}
