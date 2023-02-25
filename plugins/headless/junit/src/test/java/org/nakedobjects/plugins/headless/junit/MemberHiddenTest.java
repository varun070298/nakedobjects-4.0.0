package org.nakedobjects.plugins.headless.junit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.nakedobjects.metamodel.commons.matchers.NofMatchers.classEqualTo;

import org.junit.Test;
import org.nakedobjects.metamodel.facets.hide.HiddenFacetAnnotation;
import org.nakedobjects.metamodel.facets.hide.HideForContextFacetViaMethod;
import org.nakedobjects.metamodel.facets.hide.HideForSessionFacetViaMethod;
import org.nakedobjects.plugins.headless.applib.HiddenException;
import org.nakedobjects.plugins.headless.junit.sample.domain.Country;


public class MemberHiddenTest extends AbstractTest {

    @Test
    public void whenValueHiddenImperativelyForValueThenModifyThrowsException() {
        custJsDO.hideFirstName = true;
        try {
            custJsVO.setFirstName("Dick");
            fail("Should have thrown exception");
        } catch (final HiddenException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(HideForContextFacetViaMethod.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("First Name"));
        }
    }

    @Test
    public void whenValueHiddenImperativelyForNullThenModifyThrowsException() {
        custJsDO.hideFirstName = true;
        try {
            custJsVO.setFirstName("Dick");
            fail("Should have thrown exception");
        } catch (final HiddenException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(HideForContextFacetViaMethod.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("First Name"));
        }
    }

    @Test
    public void whenValueHiddenImperativelyThenReadThrowsException() {
        custJsDO.hideFirstName = true;
        try {
            custJsVO.getFirstName();
            fail("Should have thrown exception");
        } catch (final HiddenException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(HideForContextFacetViaMethod.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("First Name"));
        }
    }

    @Test
    public void whenAssociationHiddenImperativelyForValueThenModifyThrowsException() {
        custJsDO.hideCountryOfBirth = true;
        try {
            custJsVO.setCountryOfBirth(countryUsaDO);
            fail("Should have thrown exception");
        } catch (final HiddenException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(HideForContextFacetViaMethod.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Country Of Birth"));
        }
    }


    @Test
    public void whenAssociationHiddenImperativelyForNullThenModifyThrowsException() {
        custJsDO.hideCountryOfBirth = true;
        try {
            custJsVO.setCountryOfBirth(null);
            fail("Should have thrown exception");
        } catch (final HiddenException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(HideForContextFacetViaMethod.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Country Of Birth"));
        }
    }

    @Test
    public void whenAssociationHiddenImperativelyThenReadThrowsException() {
        custJsDO.hideCountryOfBirth = true;
        try {
            custJsVO.getCountryOfBirth();
            fail("Should have thrown exception");
        } catch (final HiddenException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(HideForContextFacetViaMethod.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Country Of Birth"));
        }
    }

    @Test
    public void whenIfCollectionHiddenImperativelyThenAddToThrowsException() {
        custJsDO.hideVisitedCountries = true;
        try {
            custJsVO.addToVisitedCountries(countryGbrDO);
            fail("Should have thrown exception");
        } catch (final HiddenException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(HideForContextFacetViaMethod.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Visited Countries"));
        }
    }

    @Test
    public void whenCollectionHiddenImperativelyThenRemoveFromThrowsException() {
        custJsDO.hideVisitedCountries = true;
        custJsDO.addToVisitedCountries(countryGbrDO);
        try {
            custJsVO.removeFromVisitedCountries(countryGbrDO);
            fail("Should have thrown exception");
        } catch (final HiddenException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(HideForContextFacetViaMethod.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Visited Countries"));
        }
    }

    @Test
    public void whenCollectionHiddenImperativelyThenReadThrowsException() {
        custJsDO.hideVisitedCountries = true;
        custJsDO.addToVisitedCountries(countryGbrDO);
        try {
            custJsVO.getVisitedCountries();
            fail("Should have thrown exception");
        } catch (final HiddenException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(HideForContextFacetViaMethod.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Visited Countries"));
        }
    }

    @Test
    public void whenActionHiddenImperativelyThenThrowsException() {
        custJsDO.hidePlaceOrder = true;
        try {
            custJsVO.placeOrder(product355DO, 3);
            fail("Should have thrown exception");
        } catch (final HiddenException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(HideForContextFacetViaMethod.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Place Order"));
        }
    }

    @Test
    public void whenValueHiddenDeclarativelyForValueThenModifyThrowsException() {
        try {
            custJsVO.setAlwaysHiddenValue("Dick");
            fail("Should have thrown exception");
        } catch (final HiddenException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(HiddenFacetAnnotation.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Always Hidden Value"));
        }
    }

    @Test
    public void whenValueHiddenDeclarativelyForNullThenModifyThrowsException() {
        try {
            custJsVO.setAlwaysHiddenValue(null);
            fail("Should have thrown exception");
        } catch (final HiddenException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(HiddenFacetAnnotation.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Always Hidden Value"));
        }
    }

    @Test
    public void whenValueHiddenDeclarativelyThenReadThrowsException() {
        try {
            custJsVO.getAlwaysHiddenValue();
            fail("Should have thrown exception");
        } catch (final HiddenException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(HiddenFacetAnnotation.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Always Hidden Value"));
        }
    }

    @Test
    public void whenAssociationHiddenDeclarativelyThenModifyThrowsException() {
        final Country[] values = new Country[] { countryUsaDO, null };
        for (final Country value : values) {
            try {
                custJsVO.setAlwaysHiddenAssociation(value);
                fail("Should have thrown exception");
            } catch (final HiddenException ex) {
                assertThat(ex.getAdvisorClass(), classEqualTo(HiddenFacetAnnotation.class));
                assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Always Hidden Association"));
            }
        }
    }

    @Test
    public void whenAssociationHiddenDeclarativelyThenReadThrowsException() {
        try {
            custJsVO.getAlwaysHiddenAssociation();
            fail("Should have thrown exception");
        } catch (final HiddenException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(HiddenFacetAnnotation.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Always Hidden Association"));
        }
    }

    @Test
    public void whenCollectionHiddenDeclarativelyThenAddToThrowsException() {
        try {
            custJsVO.addToAlwaysHiddenCollection(countryUsaDO);
            fail("Should have thrown exception");
        } catch (final HiddenException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(HiddenFacetAnnotation.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Always Hidden Collection"));
        }
    }

    @Test
    public void whenCollectionHiddenDeclarativelyThenRemoveFromThrowsException() {
        custJsDO.removeFromAlwaysHiddenCollection(countryUsaDO);
        try {
            custJsVO.removeFromAlwaysHiddenCollection(countryUsaDO);
            fail("Should have thrown exception");
        } catch (final HiddenException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(HiddenFacetAnnotation.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Always Hidden Collection"));
        }
    }

    @Test
    public void whenCollectionHiddenDeclarativelyThenReadThrowsException() {
        try {
            custJsVO.getAlwaysHiddenCollection();
            fail("Should have thrown exception");
        } catch (final HiddenException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(HiddenFacetAnnotation.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Always Hidden Collection"));
        }
    }

    @Test
    public void whenActionHiddenDeclarativelyThenThrowsException() {
        try {
            custJsVO.alwaysHiddenAction();
            fail("Should have thrown exception");
        } catch (final HiddenException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(HiddenFacetAnnotation.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Always Hidden Action"));
        }
    }


    @Test
    public void whenValueHiddenNotAuthorizedThenModifyThrowsException() {
        final String[] values = new String[] { "Dick", null };
        for (final String value : values) {
            try {
                custJsVO.setSessionHiddenValue(value);
                fail("Should have thrown exception");
            } catch (final HiddenException ex) {
                assertThat(ex.getAdvisorClass(), classEqualTo(HideForSessionFacetViaMethod.class));
                assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Session Hidden Value"));
            }
        }
    }

    @Test
    public void whenValueHiddenNotAuthorizedThenReadThrowsException() {
        try {
            custJsVO.getSessionHiddenValue();
            fail("Should have thrown exception");
        } catch (final HiddenException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(HideForSessionFacetViaMethod.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Session Hidden Value"));
        }
    }

    @Test
    public void whenAssociationHiddenNotAuthorizedThenModifyThrowsException() {
        final Country[] values = new Country[] { countryUsaDO, null };
        for (final Country value : values) {
            try {
                custJsVO.setSessionHiddenAssociation(value);
                fail("Should have thrown exception");
            } catch (final HiddenException ex) {
                assertThat(ex.getAdvisorClass(), classEqualTo(HideForSessionFacetViaMethod.class));
                assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Session Hidden Association"));
            }
        }
    }

    @Test
    public void whenAssociationHiddenNotAuthorizedThenReadThrowsException() {
        try {
            custJsVO.getSessionHiddenAssociation();
            fail("Should have thrown exception");
        } catch (final HiddenException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(HideForSessionFacetViaMethod.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Session Hidden Association"));
        }
    }

    @Test
    public void whenCollectionHiddenNotAuthorizedThenAddToThrowsException() {
        try {
            custJsVO.addToSessionHiddenCollection(countryUsaDO);
            fail("Should have thrown exception");
        } catch (final HiddenException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(HideForSessionFacetViaMethod.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Session Hidden Collection"));
        }
    }

    @Test
    public void whenCollectionHiddenNotAuthorizedThenRemoveFromThrowsException() {
        custJsDO.addToSessionHiddenCollection(countryUsaDO);
        try {
            custJsVO.removeFromSessionHiddenCollection(countryUsaDO);
            fail("Should have thrown exception");
        } catch (final HiddenException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(HideForSessionFacetViaMethod.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Session Hidden Collection"));
        }
    }

    @Test
    public void whenCollectionHiddenNotAuthorizedThenReadThrowsException() {
        try {
            custJsVO.getSessionHiddenCollection();
            fail("Should have thrown exception");
        } catch (final HiddenException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(HideForSessionFacetViaMethod.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Session Hidden Collection"));
        }
    }

    @Test
    public void whenActionHiddenNotAuthorizedThenThrowsException() {
        try {
            custJsVO.sessionHiddenAction();
            fail("Should have thrown exception");
        } catch (final HiddenException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(HideForSessionFacetViaMethod.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Session Hidden Action"));
        }
    }

}
