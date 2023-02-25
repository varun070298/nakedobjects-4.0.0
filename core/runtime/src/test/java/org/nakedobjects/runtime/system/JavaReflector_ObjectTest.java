package org.nakedobjects.runtime.system;

import org.junit.Assert;
import org.junit.Test;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.actcoll.typeof.TypeOfFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.facets.naming.describedas.DescribedAsFacet;
import org.nakedobjects.metamodel.facets.naming.named.NamedFacet;
import org.nakedobjects.metamodel.facets.object.ident.plural.PluralFacet;
import org.nakedobjects.metamodel.facets.object.ident.title.TitleFacet;
import org.nakedobjects.metamodel.facets.object.notpersistable.NotPersistableFacet;
import org.nakedobjects.metamodel.facets.object.validprops.ObjectValidPropertiesFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.specloader.JavaReflector;



public class JavaReflector_ObjectTest extends JavaReflectorTestAbstract {

    @Override
    protected NakedObjectSpecification loadSpecification(final JavaReflector reflector) {
        return reflector.loadSpecification(TestDomainObject.class);
    }

    @Test
    public void testType() throws Exception {
        Assert.assertTrue(specification.isObject());
    }

    @Test
    public void testName() throws Exception {
        Assert.assertEquals(TestDomainObject.class.getName(), specification.getFullName());
    }

    @Test
    public void testStandardFacets() throws Exception {
        Assert.assertNotNull(specification.getFacet(NamedFacet.class));
        Assert.assertNotNull(specification.getFacet(DescribedAsFacet.class));
        Assert.assertNotNull(specification.getFacet(TitleFacet.class));
        Assert.assertNotNull(specification.getFacet(PluralFacet.class));
        Assert.assertNotNull(specification.getFacet(NotPersistableFacet.class));
        Assert.assertNotNull(specification.getFacet(ObjectValidPropertiesFacet.class));
    }

    @Test
    public void testNoCollectionFacet() throws Exception {
        final Facet facet = specification.getFacet(CollectionFacet.class);
        Assert.assertNull(facet);
    }

    @Test
    public void testNoTypeOfFacet() throws Exception {
        final TypeOfFacet facet = specification.getFacet(TypeOfFacet.class);
        Assert.assertNull(facet);
    }

}

// Copyright (c) Naked Objects Group Ltd.
