package org.nakedobjects.plugins.headless.junit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.nakedobjects.plugins.headless.applib.ViewObject;
import org.nakedobjects.plugins.headless.junit.sample.domain.Country;
import org.nakedobjects.plugins.headless.junit.sample.domain.Customer;


public class ViewObjectTest extends AbstractTest {

    @SuppressWarnings("unchecked")
    private ViewObject<Customer> asViewObject() {
        return (ViewObject<Customer>) custJsVO;
    }

	@Test
    public void canCastViewsToViewObject() {
        @SuppressWarnings("unused")
        final ViewObject<Customer> custRpVOAsViewObject = asViewObject();
    }

    @Test
    public void shouldBeAbleToCreateAView() {
        final Customer custRpVO = getHeadlessViewer().view(custJsDO);
        assertThat(custRpVO, instanceOf(Customer.class));
        custRpVO.setFirstName("Dick");

        assertThat("Dick", equalTo(custRpVO.getFirstName()));
    }

    @Test
    public void viewShouldPassesThroughSetterToUnderlyingDomainObject() {
        final Customer custRpVO = getHeadlessViewer().view(custJsDO);
        custRpVO.setFirstName("Dick");

        assertThat("Dick", equalTo(custRpVO.getFirstName()));
    }

    @Test
    public void objectIsViewShouldReturnTrueWhenDealingWithView() {
        final Customer custRpVO = getHeadlessViewer().view(custJsDO);
        assertThat(getHeadlessViewer().isView(custRpVO), is(true));
    }

    @Test
    public void objectIsViewShouldReturnFalseWhenDealingWithUnderlying() {
        assertThat(getHeadlessViewer().isView(custJsDO), is(false));
    }

    @Test
    public void collectionInstanceOfViewObjectShouldReturnTrueWhenDealingWithView() {
        custJsDO.addToVisitedCountries(countryGbrDO);
        custJsDO.addToVisitedCountries(countryUsaDO);
        final List<Country> visitedCountries = custJsVO.getVisitedCountries();
        assertThat(visitedCountries instanceof ViewObject, is(true));
    }

    @Test
    public void containsOnViewedCollectionShouldIntercept() {
        custJsDO.addToVisitedCountries(countryGbrDO);
        custJsDO.addToVisitedCountries(countryUsaDO);
        final List<Country> visitedCountries = custJsVO.getVisitedCountries();
        assertThat(visitedCountries.contains(countryGbrDO), is(true));
    }

}
