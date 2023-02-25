package org.nakedobjects.metamodel.facets;

import junit.framework.TestCase;


public class NamedAndDescribedFacetHolderImplTest extends TestCase {

    private NamedAndDescribedFacetHolderImpl foo, bar;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        foo = new NamedAndDescribedFacetHolderImpl("Foo");
        bar = new NamedAndDescribedFacetHolderImpl("Bar", "This is bar");
    }

    @Override
    protected void tearDown() throws Exception {
        foo = bar = null;
        super.tearDown();
    }

    public void testGetName() {
        assertEquals("Foo", foo.getName());
        assertEquals("Bar", bar.getName());
    }

    public void testGetDescription() {
        assertNull(foo.getDescription());
        assertEquals("This is bar", bar.getDescription());
    }

}

// Copyright (c) Naked Objects Group Ltd.
