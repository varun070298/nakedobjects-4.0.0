package org.nakedobjects.metamodel.facets.ordering.memberorder;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;


public class MemberOrderComparatorTest extends TestCase {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(new TestSuite(MemberOrderComparatorTest.class));
    }

    private MemberOrderComparator comparator, laxComparator;

    private final MemberPeerStub m1 = new MemberPeerStub("abc");
    private final MemberPeerStub m2 = new MemberPeerStub("abc");

    @Override
    protected void setUp() {
        LogManager.getLoggerRepository().setThreshold(Level.OFF);

        comparator = new MemberOrderComparator(true);
        laxComparator = new MemberOrderComparator(false);
    }

    @Override
    protected void tearDown() throws Exception {}

    public void testDefaultGroupOneComponent() {
        m1.addFacet(new MemberOrderFacetAnnotation("", "1", m1));
        m2.addFacet(new MemberOrderFacetAnnotation("", "2", m2));
        assertEquals(-1, comparator.compare(m1, m2));
    }

    public void testDefaultGroupOneComponentOtherWay() {
        m1.addFacet(new MemberOrderFacetAnnotation("", "2", m1));
        m2.addFacet(new MemberOrderFacetAnnotation("", "1", m2));
        assertEquals(+1, comparator.compare(m1, m2));
    }

    public void testDefaultGroupOneComponentSame() {
        m1.addFacet(new MemberOrderFacetAnnotation("", "1", m1));
        m2.addFacet(new MemberOrderFacetAnnotation("", "1", m2));
        assertEquals(0, comparator.compare(m1, m2));
    }

    public void testDefaultGroupOneSideRunsOutOfComponentsFirst() {
        m1.addFacet(new MemberOrderFacetAnnotation("", "1", m1));
        m2.addFacet(new MemberOrderFacetAnnotation("", "1.1", m2));
        assertEquals(-1, comparator.compare(m1, m2));
    }

    public void testDefaultGroupOneSideRunsOutOfComponentsFirstOtherWay() {
        m1.addFacet(new MemberOrderFacetAnnotation("", "1.1", m1));
        m2.addFacet(new MemberOrderFacetAnnotation("", "1", m2));
        assertEquals(+1, comparator.compare(m1, m2));
    }

    public void testDefaultGroupOneSideRunsTwoComponents() {
        m1.addFacet(new MemberOrderFacetAnnotation("", "1.1", m1));
        m2.addFacet(new MemberOrderFacetAnnotation("", "1.2", m2));
        assertEquals(-1, comparator.compare(m1, m2));
    }

    public void testDefaultGroupOneSideRunsTwoComponentsOtherWay() {
        m1.addFacet(new MemberOrderFacetAnnotation("", "1.2", m1));
        m2.addFacet(new MemberOrderFacetAnnotation("", "1.1", m2));
        assertEquals(+1, comparator.compare(m1, m2));
    }

    public void testDefaultGroupOneSideRunsLotsOfComponents() {
        m1.addFacet(new MemberOrderFacetAnnotation("", "1.2.5.8.3.3", m1));
        m2.addFacet(new MemberOrderFacetAnnotation("", "1.2.5.8.3.4", m2));
        assertEquals(-1, comparator.compare(m1, m2));
    }

    public void testDefaultGroupOneSideRunsLotsOfComponentsOtherWay() {
        m1.addFacet(new MemberOrderFacetAnnotation("", "1.2.5.8.3.4", m1));
        m2.addFacet(new MemberOrderFacetAnnotation("", "1.2.5.8.3.3", m2));
        assertEquals(+1, comparator.compare(m1, m2));
    }

    public void testDefaultGroupOneSideRunsLotsOfComponentsSame() {
        m1.addFacet(new MemberOrderFacetAnnotation("", "1.2.5.8.3.3", m1));
        m2.addFacet(new MemberOrderFacetAnnotation("", "1.2.5.8.3.3", m2));
        assertEquals(0, comparator.compare(m1, m2));
    }

    public void testNamedGroupOneSideRunsLotsOfComponents() {
        m1.addFacet(new MemberOrderFacetAnnotation("abc", "1.2.5.8.3.3", m1));
        m2.addFacet(new MemberOrderFacetAnnotation("abc", "1.2.5.8.3.4", m2));
        assertEquals(-1, comparator.compare(m1, m2));
    }

    public void testEnsuresInSameGroup() {
        m1.addFacet(new MemberOrderFacetAnnotation("abc", "1", m1));
        m2.addFacet(new MemberOrderFacetAnnotation("def", "2", m2));
        try {
            assertEquals(-1, comparator.compare(m1, m2));
            fail("Exception should have been thrown");
        } catch (final IllegalArgumentException ex) {
            // expected
        }
    }

    public void testEnsuresInSameGroupCanBeDisabled() {
        m1.addFacet(new MemberOrderFacetAnnotation("abc", "1", m1));
        m2.addFacet(new MemberOrderFacetAnnotation("def", "2", m2));
        assertEquals(-1, laxComparator.compare(m1, m2));
    }

    public void testNonAnnotatedAfterAnnotated() {
        // don't annotate m1
        m2.addFacet(new MemberOrderFacetAnnotation("def", "2", m2));
        assertEquals(+1, comparator.compare(m1, m2));
    }

}
// Copyright (c) Naked Objects Group Ltd.
