package org.nakedobjects.metamodel.examples.facets.jsr303;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nakedobjects.metamodel.examples.facets.jsr303.Jsr303PropertyValidationFacet;
import org.nakedobjects.metamodel.facets.FacetHolderImpl;


public class Jsr303FacetInstantiation {

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
        new Jsr303PropertyValidationFacet(holder);
    }

}
