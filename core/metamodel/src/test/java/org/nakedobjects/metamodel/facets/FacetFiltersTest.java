package org.nakedobjects.metamodel.facets;

import junit.framework.TestCase;

import org.nakedobjects.metamodel.commons.filters.Filter;
import org.nakedobjects.metamodel.commons.filters.Filters;


public class FacetFiltersTest extends TestCase {

    public class FooSuperFacet extends FacetAbstract {
        public FooSuperFacet(final Class<? extends Facet> facetType, final FacetHolder holder) {
            super(facetType, holder, false);
        }
    }

    public class FooFacet extends FooSuperFacet {
        public FooFacet(final FacetHolder holder) {
            super(FooFacet.class, holder);
        }
    }

    public class FooSubFacet extends FooFacet {
        public FooSubFacet(final FacetHolder holder) {
            super(holder);
        }
    }

    public class BarFacet extends FacetAbstract {
        public BarFacet(final FacetHolder holder) {
            super(BarFacet.class, holder, false);
        }
    }

    private FacetHolder facetHolder;
    private FooFacet fooFacet;
    private FooSubFacet fooSubFacet;
    private FooSuperFacet fooSuperFacet;
    private BarFacet barFacet;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        facetHolder = new FacetHolderImpl();
        fooSuperFacet = new FooSuperFacet(FooSuperFacet.class, facetHolder);
        fooFacet = new FooFacet(facetHolder);
        fooSubFacet = new FooSubFacet(facetHolder);
        barFacet = new BarFacet(facetHolder);
    }

    @Override
    protected void tearDown() throws Exception {
        facetHolder = null;
        fooSuperFacet = null;
        fooSubFacet = null;
        fooFacet = null;
        barFacet = null;
        super.tearDown();
    }

    public void testIsAWhenIs() {
        final Filter<Facet> filter = FacetFilters.isA(FooFacet.class);
        assertTrue(filter.accept(fooFacet));
    }

    public void testIsAWhenIsNot() {
        final Filter<Facet> filter = FacetFilters.isA(FooFacet.class);
        assertFalse(filter.accept(barFacet));
    }

    public void testIsAWhenIsSubclass() {
        final Filter<Facet> filter = FacetFilters.isA(FooFacet.class);
        assertTrue(filter.accept(fooSubFacet));
    }

    public void testIsAWhenIsNotBecauseASuperclass() {
        final Filter<Facet> filter = FacetFilters.isA(FooFacet.class);
        assertFalse(filter.accept(fooSuperFacet));
    }

    public void testAndTrueTrue() {
        final Filter<Facet> and = Filters.and(FacetFilters.ANY, FacetFilters.ANY);
        assertTrue(and.accept(fooFacet));
    }

    public void testAndTrueFalse() {
        final Filter<Facet> and = Filters.and(FacetFilters.ANY, FacetFilters.NONE);
        assertFalse(and.accept(fooFacet));
    }

    public void testAndFalseTrue() {
        final Filter<Facet> and = Filters.and(FacetFilters.NONE, FacetFilters.ANY);
        assertFalse(and.accept(fooFacet));
    }

    public void testAndFalseFalse() {
        final Filter<Facet> and = Filters.and(FacetFilters.NONE, FacetFilters.NONE);
        assertFalse(and.accept(fooFacet));
    }

    public void testOrTrueTrue() {
        final Filter<Facet> or = Filters.or(FacetFilters.ANY, FacetFilters.ANY);
        assertTrue(or.accept(fooFacet));
    }

    public void testOrTrueFalse() {
        final Filter<Facet> or = Filters.or(FacetFilters.ANY, FacetFilters.NONE);
        assertTrue(or.accept(fooFacet));
    }

    public void testorFalseTrue() {
        final Filter<Facet> or = Filters.or(FacetFilters.NONE, FacetFilters.ANY);
        assertTrue(or.accept(fooFacet));
    }

    public void testOrFalseFalse() {
        final Filter<Facet> or = Filters.and(FacetFilters.NONE, FacetFilters.NONE);
        assertFalse(or.accept(fooFacet));
    }

    public void testNotTrue() {
        final Filter<Facet> not = Filters.not(FacetFilters.ANY);
        assertFalse(not.accept(fooFacet));
    }

    public void testNotFalse() {
        final Filter<Facet> not = Filters.not(FacetFilters.NONE);
        assertTrue(not.accept(fooFacet));
    }

    public void testAny() {
        final Filter<Facet> any = Filters.any();
        assertTrue(any.accept(fooFacet));
    }

    public void testNone() {
        final Filter<Facet> none = Filters.none();
        assertFalse(none.accept(fooFacet));
    }

}

// Copyright (c) Naked Objects Group Ltd.
