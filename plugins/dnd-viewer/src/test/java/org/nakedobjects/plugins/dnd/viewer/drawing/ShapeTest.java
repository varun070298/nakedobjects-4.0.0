package org.nakedobjects.plugins.dnd.viewer.drawing;

import org.nakedobjects.plugins.dnd.viewer.drawing.Shape;

import junit.framework.TestCase;


public class ShapeTest extends TestCase {

    private Shape shape;

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(ShapeTest.class);
    }

    @Override
    protected void setUp() throws Exception {
        shape = new Shape();
    }

    public void testNew() {
        assertEquals(0, shape.count());
        assertEquals(0, shape.getX().length);
        assertEquals(0, shape.getY().length);
    }

    public void testAddPoint() {
        shape.addVertex(10, 12);
        assertEquals(1, shape.count());
        assertEquals(10, shape.getX()[0]);
        assertEquals(12, shape.getY()[0]);
    }

    public void testAddThreePoints() {
        shape.addVertex(10, 12);
        shape.addVertex(8, 5);
        shape.addVertex(0, 2);
        assertEquals(3, shape.count());
        assertEquals(10, shape.getX()[0]);
        assertEquals(12, shape.getY()[0]);
        assertEquals(8, shape.getX()[1]);
        assertEquals(5, shape.getY()[1]);
        assertEquals(0, shape.getX()[2]);
        assertEquals(2, shape.getY()[2]);
    }

    public void testCreateCopy() {
        shape.addVertex(10, 12);
        shape.addVertex(8, 5);
        shape.addVertex(0, 2);

        final Shape copy = new Shape(shape);

        assertEquals(3, copy.count());
        assertEquals(10, copy.getX()[0]);
        assertEquals(12, copy.getY()[0]);
        assertEquals(8, copy.getX()[1]);
        assertEquals(5, copy.getY()[1]);
        assertEquals(0, copy.getX()[2]);
        assertEquals(2, copy.getY()[2]);
    }

    public void testTransform() {
        shape.addVertex(10, 12);
        shape.addVertex(8, 5);
        shape.addVertex(0, 2);
        shape.translate(10, 20);
        assertEquals(3, shape.count());
        assertEquals(20, shape.getX()[0]);
        assertEquals(32, shape.getY()[0]);
        assertEquals(18, shape.getX()[1]);
        assertEquals(25, shape.getY()[1]);
        assertEquals(10, shape.getX()[2]);
        assertEquals(22, shape.getY()[2]);
    }

}
// Copyright (c) Naked Objects Group Ltd.
