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
public class MustSatisfySpecificationValidatingInteractionMoreTest {

    private final Mockery mockery = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    private MustSatisfySpecificationFacet facetForSpecificationFirstLetterUpperCase;
    private FacetHolder mockHolder;

    private PropertyModifyContext mockContext;
    
    private NakedObject mockProposedNakedObject;

    private SpecificationRequiresFirstLetterToBeUpperCase requiresFirstLetterToBeUpperCase;
    
    @Before
    public void setUp() throws Exception {
        mockHolder = mockery.mock(Identified.class);
        requiresFirstLetterToBeUpperCase = new SpecificationRequiresFirstLetterToBeUpperCase();
        
        facetForSpecificationFirstLetterUpperCase = new MustSatisfySpecificationFacet(Utils.listOf(requiresFirstLetterToBeUpperCase), mockHolder);

        mockContext = mockery.mock(PropertyModifyContext.class);
        mockProposedNakedObject = mockery.mock(NakedObject.class, "proposed");
    }

    @After
    public void tearDown() throws Exception {
        mockHolder = null;
        requiresFirstLetterToBeUpperCase = null;
        mockContext = null;
    }


    /**
     * As in: 
     * <pre>
     * [at]ValidatedBy(SpecificationRequiresFirstLetterToBeUpperCase.class)
     * public void getLastName() { ... }
     * </pre>
     *
     * @see SpecificationRequiresFirstLetterToBeUpperCase
     */
    @Test
    public void validatesUsingSpecificationIfProposedOkay() {
        mockery.checking(new Expectations() {
            {
                one(mockContext).getProposed();
                will(returnValue(mockProposedNakedObject));

                one(mockProposedNakedObject).getObject();
                will(returnValue("This starts with an upper case letter and so is okay"));
            }
        });

        final String reason = facetForSpecificationFirstLetterUpperCase.invalidates(mockContext);
        assertThat(reason, is(nullValue()));
    }

    @Test
    public void invalidatesUsingSpecificationIfProposedNotOkay() {
        mockery.checking(new Expectations() {
            {
                one(mockContext).getProposed();
                will(returnValue(mockProposedNakedObject));

                one(mockProposedNakedObject).getObject();
                will(returnValue("this starts with an lower case letter and so is not okay"));
            }
        });

        final String reason = facetForSpecificationFirstLetterUpperCase.invalidates(mockContext);
        assertThat(reason, is(not(nullValue())));
        assertThat(reason, is("Must start with upper case"));
    }


}
