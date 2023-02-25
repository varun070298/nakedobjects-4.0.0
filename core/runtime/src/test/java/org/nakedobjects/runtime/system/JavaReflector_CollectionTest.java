package org.nakedobjects.runtime.system;

import java.util.Vector;

import org.junit.Assert;
import org.junit.Test;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.actcoll.typeof.TypeOfFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.specloader.JavaReflector;


public class JavaReflector_CollectionTest extends JavaReflectorTestAbstract {

    @Override
    protected NakedObjectSpecification loadSpecification(final JavaReflector reflector) {
        return reflector.loadSpecification(Vector.class);
    }

    @Test
    public void testType() throws Exception {
        Assert.assertTrue(specification.isCollection());
    }

    @Test
    public void testName() throws Exception {
        Assert.assertEquals(Vector.class.getName(), specification.getFullName());
    }

    @Test
    @Override
    public void testCollectionFacet() throws Exception {
        final Facet facet = specification.getFacet(CollectionFacet.class);
        Assert.assertNotNull(facet);
    }

    @Test
    @Override
    public void testTypeOfFacet() throws Exception {
        final TypeOfFacet facet = specification.getFacet(TypeOfFacet.class);
        Assert.assertNotNull(facet);
        Assert.assertEquals(Object.class, facet.value());
    }

}

// Copyright (c) Naked Objects Group Ltd.
