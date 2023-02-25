package org.nakedobjects.metamodel.examples.facets.namefile;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsArrayContaining.hasItemInArray;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nakedobjects.metamodel.examples.facets.namefile.NameFileFacetFactory;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;



public class NameFileFacetFactoryFeatureTypesTest {

    private NameFileFacetFactory facetFactory;

    @Before
    public void setUp() throws Exception {
        facetFactory = new NameFileFacetFactory();
    }

    @After
    public void tearDown() throws Exception {
        facetFactory = null;
    }

    @Test
    public void featureTypesLength() {
        NakedObjectFeatureType[] featureTypes = facetFactory.getFeatureTypes();
        assertThat(featureTypes.length, is(4));
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

    @Test
    public void featureTypesContainsTypeRepresentingCollection() {
        NakedObjectFeatureType[] featureTypes = facetFactory.getFeatureTypes();
        assertThat(featureTypes, hasItemInArray(NakedObjectFeatureType.COLLECTION));
    }

    @Test
    public void featureTypesContainsTypeRepresentingAction() {
        NakedObjectFeatureType[] featureTypes = facetFactory.getFeatureTypes();
        assertThat(featureTypes, hasItemInArray(NakedObjectFeatureType.ACTION));
    }

}
