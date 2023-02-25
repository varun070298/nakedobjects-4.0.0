package org.nakedobjects.metamodel.examples.facets.jsr303;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsArrayContaining.hasItemInArray;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nakedobjects.metamodel.examples.facets.jsr303.Jsr303FacetFactory;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;



public class Jsr303FacetFactoryFeatureTypes {

    private Jsr303FacetFactory facetFactory;

    @Before
    public void setUp() throws Exception {
        facetFactory = new Jsr303FacetFactory();
    }

    @After
    public void tearDown() throws Exception {
        facetFactory = null;
    }

    @Test
    public void featureTypesLength() {
        NakedObjectFeatureType[] featureTypes = facetFactory.getFeatureTypes();
        assertThat(featureTypes.length, is(2));
    }

    @Test
    public void featureTypesContainsTypeRepresentingObject() {
        NakedObjectFeatureType[] featureTypes = facetFactory.getFeatureTypes();
        assertThat(featureTypes, hasItemInArray(NakedObjectFeatureType.OBJECT));
    }

    @Test
    public void featureTypesContainsTypeRepresentingProperty() {
        NakedObjectFeatureType[] featureTypes = facetFactory.getFeatureTypes();
        assertThat(featureTypes, hasItemInArray(NakedObjectFeatureType.PROPERTY));
    }


}
