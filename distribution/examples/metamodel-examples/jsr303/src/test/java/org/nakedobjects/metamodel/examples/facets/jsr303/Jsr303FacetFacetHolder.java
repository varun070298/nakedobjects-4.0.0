package org.nakedobjects.metamodel.examples.facets.jsr303;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.examples.facets.jsr303.Jsr303PropertyValidationFacet;
import org.nakedobjects.metamodel.facets.FacetHolder;




@RunWith(JMock.class)
public class Jsr303FacetFacetHolder {

    private final Mockery mockery = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    private Jsr303PropertyValidationFacet facet;
    private FacetHolder mockHolder;

    
    @Before
    public void setUp() throws Exception {
        mockHolder = mockery.mock(FacetHolder.class);
        facet = new Jsr303PropertyValidationFacet(mockHolder);
    }

    @After
    public void tearDown() throws Exception {
        mockHolder = null;
        facet = null;
    }


    @Test
    public void facetHolder() {
        assertThat(facet.getFacetHolder(), is(mockHolder));
    }

}
