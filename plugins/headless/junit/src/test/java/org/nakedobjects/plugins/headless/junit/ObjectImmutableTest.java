package org.nakedobjects.plugins.headless.junit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.nakedobjects.metamodel.commons.matchers.NofMatchers.classEqualTo;

import org.junit.Test;
import org.nakedobjects.metamodel.facets.object.immutable.DisabledFacetDerivedFromImmutable;
import org.nakedobjects.plugins.headless.applib.DisabledException;


public class ObjectImmutableTest extends AbstractTest {

    @Test
    public void settingValueOnImmutableObjectThrowsException() {
        try {
            product355VO.setDescription("Changed");
            fail("Should have thrown exception");
        } catch (final DisabledException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(DisabledFacetDerivedFromImmutable.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Description"));
        }
    }

    @Test
    public void settingAssociationOnImmutableObjectThrowsException() {
        try {
            product355VO.setPlaceOfManufacture(countryUsaDO);
            fail("Should have thrown exception");
        } catch (final DisabledException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(DisabledFacetDerivedFromImmutable.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Place Of Manufacture"));
        }
    }

    @Test
    public void addingToCollectionOnImmutableObjectThrowsException() {
        try {
            product355VO.addToSimilarProducts(product850DO);
            fail("Should have thrown exception");
        } catch (final DisabledException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(DisabledFacetDerivedFromImmutable.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Similar Products"));
        }
    }

    @Test
    public void removingFromCollectionOnImmutableObjectThrowsException() {
        product355DO.addToSimilarProducts(product850DO); // TODO: can't setup, throws
        // ObjectPersistenceException
        try {
            product355VO.removeFromSimilarProducts(product850DO);
            fail("Should have thrown exception");
        } catch (final DisabledException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(DisabledFacetDerivedFromImmutable.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Similar Products"));
        }
    }

    @Test
    public void canInvokingOnImmutableObject() {
        product355VO.foobar();
    }

}
