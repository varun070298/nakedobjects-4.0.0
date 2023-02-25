package org.nakedobjects.plugins.headless.junit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.nakedobjects.metamodel.commons.matchers.NofMatchers.classEqualTo;

import java.util.List;

import org.junit.Test;
import org.nakedobjects.metamodel.facets.disable.DisableForContextFacetViaMethod;
import org.nakedobjects.metamodel.facets.disable.DisabledFacetAnnotation;
import org.nakedobjects.plugins.headless.applib.DisabledException;
import org.nakedobjects.plugins.headless.junit.sample.domain.Order;


public class MemberDisabledTest extends AbstractTest {

    @Test
    public void whenValueDisabledForValueThenThrowsException() {
        custJsDO.disableFirstName = "cannot alter";
        try {
            custJsVO.setFirstName("Dick");
            fail("Should have thrown exception");
        } catch (final DisabledException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(DisableForContextFacetViaMethod.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("First Name"));
            assertThat(ex.getMessage(), equalTo("cannot alter"));
        }
    }

    @Test
    public void whenValueDisabledForNullThenThrowsException() {
        custJsDO.disableFirstName = "cannot alter";
        try {
            custJsVO.setFirstName(null);
            fail("Should have thrown exception");
        } catch (final DisabledException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(DisableForContextFacetViaMethod.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("First Name"));
            assertThat(ex.getMessage(), equalTo("cannot alter"));
        }
    }

    @Test
    public void whenAssociationDisabledForReferenceThenThrowsException() {
        custJsDO.disableCountryOfBirth = "cannot alter";
        try {
            custJsVO.setCountryOfBirth(countryUsaDO);
            fail("Should have thrown exception");
        } catch (final DisabledException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(DisableForContextFacetViaMethod.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Country Of Birth"));
            assertThat(ex.getMessage(), equalTo("cannot alter"));
        }
    }

    @Test
    public void whenAssociationDisabledForNullThenThrowsException() {
        custJsDO.disableCountryOfBirth = "cannot alter";
        try {
            custJsVO.setCountryOfBirth(null);
            fail("Should have thrown exception");
        } catch (final DisabledException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(DisableForContextFacetViaMethod.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Country Of Birth"));
            assertThat(ex.getMessage(), equalTo("cannot alter"));
        }
    }

    @Test
    public void whenCollectionDisabledThenAddToThrowsException() {
        List<Order> orders = custJsVO.getOrders();
		final Order order = orders.get(0);
        try {
            custJsVO.addToMoreOrders(order);
            fail("Should have thrown exception");
        } catch (final DisabledException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(DisabledFacetAnnotation.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("More Orders"));
            assertThat(ex.getMessage(), equalTo("Always disabled"));
        }
    }

    @Test
    public void whenCollectionDisabledThenRemovefromThrowsException() {
        custJsDO.addToVisitedCountries(countryUsaDO);
        custJsDO.disableVisitedCountries = "cannot alter";
        try {
            custJsVO.removeFromVisitedCountries(countryUsaDO);
            fail("Should have thrown exception");
        } catch (final DisabledException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(DisableForContextFacetViaMethod.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Visited Countries"));
            assertThat(ex.getMessage(), equalTo("cannot alter"));
        }
    }

    @Test
    public void whenActionDisabledThenThrowsException() {
        custJsDO.disablePlaceOrder = "cannot invoke";
        try {
            custJsVO.placeOrder(product355DO, 3);
            fail("Should have thrown exception");
        } catch (final DisabledException ex) {
            assertThat(ex.getAdvisorClass(), classEqualTo(DisableForContextFacetViaMethod.class));
            assertThat(ex.getIdentifier().getMemberNaturalName(), equalTo("Place Order"));
            assertThat(ex.getMessage(), equalTo("cannot invoke"));
        }
    }

}
