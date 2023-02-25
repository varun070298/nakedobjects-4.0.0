package org.nakedobjects.metamodel.examples.facets.namefile;

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
import org.nakedobjects.metamodel.examples.facets.namefile.NameFileFacet;
import org.nakedobjects.metamodel.facets.FacetHolder;




@RunWith(JMock.class)
public class NameFileFacetFacetHolderTest {

    private final Mockery mockery = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    private NameFileFacet facet;
    private FacetHolder mockHolder;

    
    @Before
    public void setUp() throws Exception {
        mockHolder = mockery.mock(FacetHolder.class);
        facet = new NameFileFacet(mockHolder, "Foobar");
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
