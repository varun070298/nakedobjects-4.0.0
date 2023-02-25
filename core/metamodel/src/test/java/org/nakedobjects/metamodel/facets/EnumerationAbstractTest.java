package org.nakedobjects.metamodel.facets;

import junit.framework.TestCase;


public class EnumerationAbstractTest extends TestCase {

    public class ConcreteEnumeration extends EnumerationAbstract {

        protected ConcreteEnumeration(final int num, final String nameInCode, final String friendlyName) {
            super(num, nameInCode, friendlyName);
        }
    }

    public class OtherConcreteEnumeration extends EnumerationAbstract {

        protected OtherConcreteEnumeration(final int num, final String nameInCode, final String friendlyName) {
            super(num, nameInCode, friendlyName);
        }
    }

    private EnumerationAbstract foo1, foo2, bar1, fooInterloper;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        foo1 = new ConcreteEnumeration(1, "foo", "Foo");
        foo2 = new ConcreteEnumeration(1, "foo", "Foo");
        bar1 = new ConcreteEnumeration(2, "bar", "Bar");
        fooInterloper = new OtherConcreteEnumeration(1, "foo", "Foo");
    }

    @Override
    protected void tearDown() throws Exception {
        foo1 = foo2 = bar1 = null;
        super.tearDown();
    }

    public void testGetNum() {
        assertEquals(1, foo1.getNum());
    }

    public void testGetNameInCode() {
        assertEquals("foo", foo1.getNameInCode());
    }

    public void testGetFriendlyName() {
        assertEquals("Foo", foo1.getFriendlyName());
    }

    public void testEqualsAbstractEnumerationForSelf() {
        assertTrue(foo1.equals(foo1));
    }

    public void testEqualsAbstractEnumerationForEqual() {
        assertTrue(foo1.equals(foo2));
    }

    public void testEqualsAbstractEnumerationForDifferent() {
        assertFalse(foo1.equals(bar1));
    }

    public void testEqualsAbstractEnumerationForSameCardinalButDifferentType() {
        assertFalse(foo1.equals(fooInterloper));
    }

    public void testHashCodeForEqual() {
        assertEquals(foo1.hashCode(), foo2.hashCode());
    }

    public void testHashCodeForDifferent() {
        assertFalse(foo1.hashCode() == bar1.hashCode());
    }

}

// Copyright (c) Naked Objects Group Ltd.
