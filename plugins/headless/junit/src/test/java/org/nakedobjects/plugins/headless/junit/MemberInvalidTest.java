package org.nakedobjects.plugins.headless.junit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.nakedobjects.metamodel.commons.matchers.NofMatchers.classEqualTo;

import org.junit.Test;
import org.nakedobjects.metamodel.facets.actions.validate.ActionValidationFacetViaMethod;
import org.nakedobjects.metamodel.facets.collections.validate.CollectionValidateAddToFacetViaMethod;
import org.nakedobjects.metamodel.facets.collections.validate.CollectionValidateRemoveFromFacetViaMethod;
import org.nakedobjects.metamodel.facets.properties.validate.PropertyValidateFacetViaMethod;
import org.nakedobjects.metamodel.facets.propparam.validate.mandatory.MandatoryFacetDefault;
import org.nakedobjects.metamodel.facets.propparam.validate.maxlength.MaxLengthFacetAnnotation;
import org.nakedobjects.metamodel.facets.propparam.validate.regex.RegExFacetAnnotation;
import org.nakedobjects.plugins.headless.applib.InvalidException;
import org.nakedobjects.plugins.headless.junit.sample.domain.Country;


public class MemberInvalidTest extends AbstractTest {

    @Test
    public void whenValueInvalidImperativelyThenThrowsException() {
        final String[] values = new String[] { "Dick", null };
        for (final String value : values) {
            custJsDO.validateFirstNameExpectedArg = value;
            custJsDO.validateFirstName = "bad first name";
            try {
                custJsVO.setFirstName(value);
                fail("Should have thrown exception");
            } catch (final InvalidException ex) {
                assertThat(ex.getAdvisorClass(), classEqualTo(PropertyValidateFacetViaMethod.class));
                assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("First Name"));
                assertThat(ex.getMessage(), equalTo("bad first name"));
            }
        }
    }

    @Test
    public void whenAssociationInvalidImperativelyThenThrowsException() {
        custJsDO.validateCountryOfBirth = "bad country of birth";
        final Country[] values = new Country[] { countryUsaDO, null };
        for (final Country value : values) {
            try {
                custJsVO.setCountryOfBirth(value);
                fail("Should have thrown exception");
            } catch (final InvalidException ex) {
                assertThat(ex.getAdvisorClass(), classEqualTo(PropertyValidateFacetViaMethod.class));
                assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Country Of Birth"));
                assertThat(ex.getMessage(), equalTo("bad country of birth"));
            }
        }
    }

    @Test
    public void whenCollectionInvalidImperativelyThenAddToThrowsException() {
        custJsDO.validateAddToVisitedCountries = "bad country";
        try {
            custJsVO.addToVisitedCountries(countryGbrDO);
            fail("Should have thrown exception");
        } catch (final InvalidException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(CollectionValidateAddToFacetViaMethod.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Visited Countries"));
            assertThat(ex.getMessage(), equalTo("bad country"));
        }
    }

    @Test
    public void whenCollectionInvalidImperativelyThenRemoveFromThrowsException() {
        custJsDO.addToVisitedCountries(countryGbrDO);
        custJsDO.validateRemoveFromVisitedCountries = "bad country";
        try {
            custJsVO.removeFromVisitedCountries(countryGbrDO);
            fail("Should have thrown exception");
        } catch (final InvalidException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(CollectionValidateRemoveFromFacetViaMethod.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Visited Countries"));
            assertThat(ex.getMessage(), equalTo("bad country"));
        }
    }

    @Test
    public void whenActionInvalidImperativelyThenThrowsException() {
        custJsDO.validatePlaceOrder = "can't place order";
        try {
            custJsVO.placeOrder(product355DO, 3);
            fail("Should have thrown exception");
        } catch (final InvalidException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(ActionValidationFacetViaMethod.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Place Order"));
            assertThat(ex.getMessage(), equalTo("can't place order"));
        }
    }

    @Test
    public void whenValueCanSetNullOnOptionalField() {
        custJsVO.setOptionalValue(null);
    }

    @Test
    public void whenAssociationCanSetNullOnOptionalField() {
        custJsVO.setOptionalAssociation(null);
    }

    @Test
    public void whenValueInvalidMandatoryThenThrowsException() {
        try {
            custJsVO.setMandatoryValue(null);
            fail("Should have thrown exception");
        } catch (final InvalidException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(MandatoryFacetDefault.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Mandatory Value"));
        }
    }

    @Test
    public void whenAssociationInvalidMandatoryThenThrowsException() {
        try {
            custJsVO.setMandatoryAssociation(null);
            fail("Should have thrown exception");
        } catch (final InvalidException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(MandatoryFacetDefault.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Mandatory Association"));
        }
    }


    @Test
    public void whenInvalidMaxLengthThenThrowsException() {
        try {
            custJsVO.setMaxLengthField("This is far too long");
            fail("Should have thrown exception");
        } catch (final InvalidException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(MaxLengthFacetAnnotation.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Max Length Field"));
        }
    }


    @Test
    public void whenInvalidRegExCaseSensitiveThenThrowsException() {
        try {
            custJsVO.setRegExCaseSensitiveField("abCfoobar");
            fail("Should have thrown exception");
        } catch (final InvalidException ex) {
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Reg Ex Case Sensitive Field"));
        }
    }

    @Test
    public void whenCanSetValidRegExCaseSensitive() {
        custJsVO.setRegExCaseInsensitiveField("abcfoobar");

    }

    @Test
    public void whenInvalidRegExCaseInsensitiveThenThrowsException() {
        try {
            custJsVO.setRegExCaseInsensitiveField("abXfoobar");
            fail("Should have thrown exception");
        } catch (final InvalidException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(RegExFacetAnnotation.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Reg Ex Case Insensitive Field"));
        }
    }

    @Test
    public void whenCanSetValidRegExCaseInsensitive() {
        custJsVO.setRegExCaseInsensitiveField("AbCfoobar");
    }

}
