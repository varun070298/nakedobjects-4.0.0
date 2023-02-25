package org.nakedobjects.metamodel.examples.facets.namefile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nakedobjects.metamodel.examples.facets.namefile.NameFileFacet;
import org.nakedobjects.metamodel.facets.FacetHolderImpl;


public class NameFileFacetInstantiationTest {

    private FacetHolderImpl holder;

    @Before
    public void setUp() throws Exception {
        holder = new FacetHolderImpl();
    }

    @After
    public void tearDown() throws Exception {
        holder = null;
    }

    @Test
    public void canInstantiate() {
        new NameFileFacet(holder, "foobar");
    }

}
