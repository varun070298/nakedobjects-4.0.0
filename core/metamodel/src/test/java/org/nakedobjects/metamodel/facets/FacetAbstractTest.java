package org.nakedobjects.metamodel.facets;

import junit.framework.TestCase;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.NakedObjectNoop;


public class FacetAbstractTest extends TestCase {

    public static interface FooFacet extends Facet {}

    public static interface BarFacet extends Facet {}

    public class ConcreteFacet extends FacetAbstract {
        public ConcreteFacet(final Class<? extends Facet> facetType, final FacetHolder holder) {
            super(facetType, holder, false);
        }

    }


    private FacetAbstract fooFacet;
    private FacetHolder facetHolder, facetHolder2;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        facetHolder = new FacetHolderImpl();
        facetHolder2 = new FacetHolderImpl();
        fooFacet = new ConcreteFacet(FooFacet.class, facetHolder);
        new ConcreteFacet(BarFacet.class, facetHolder);
        facetHolder.addFacet(fooFacet);
    }

    @Override
    protected void tearDown() throws Exception {
        fooFacet = null;
        facetHolder = null;
        super.tearDown();
    }

    public void testFacetType() {
        assertEquals(FooFacet.class, fooFacet.facetType());
    }

    public void testGetFacetHolder() {
        assertEquals(facetHolder, fooFacet.getFacetHolder());
    }

    public void testSetFacetHolder() {
        fooFacet.setFacetHolder(facetHolder2);
        assertEquals(facetHolder2, fooFacet.getFacetHolder());
    }

    public void testToString() {
        assertEquals("FacetAbstractTest$ConcreteFacet[type=FacetAbstractTest$FooFacet]", fooFacet.toString());
    }

    public void testUnwrapObjectWhenNull() {
        assertNull(fooFacet.unwrapObject(null));
    }

    public void testUnwrapObjectWhenNotNull() {
        final Object underlyingDomainObject = new Object();
        final NakedObject nakedObject = new NakedObjectNoop() {
            @Override
            public Object getObject() {
                return underlyingDomainObject;
            }

        };

        assertEquals(underlyingDomainObject, fooFacet.unwrapObject(nakedObject));
    }

    public void testUnwrapStringWhenNull() {
        assertNull(fooFacet.unwrapString(null));
    }

    public void testUnwrapStringWhenNotNullButNotString() {
        final Object underlyingDomainObject = new Object();
        final NakedObject nakedObject = new NakedObjectNoop() {
            @Override
            public Object getObject() {
                return underlyingDomainObject;
            }
        };
        assertNull(fooFacet.unwrapString(nakedObject));
    }

    public void testUnwrapStringWhenNotNullAndString() {
        final String underlyingDomainObject = "huzzah";
        final NakedObject nakedObject = new NakedObjectNoop() {
            @Override
            public Object getObject() {
                return underlyingDomainObject;
            }
        };
        assertEquals("huzzah", fooFacet.unwrapString(nakedObject));
    }

}

// Copyright (c) Naked Objects Group Ltd.
