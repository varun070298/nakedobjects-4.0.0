package org.nakedobjects.plugins.headless.junit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.nakedobjects.plugins.headless.junit.sample.domain.Country;


public class MemberReadTest extends AbstractTest {

    @Test
    public void value() {
        assertThat(custJsVO.getFirstName(), equalTo("Richard"));
    }

    @Test
    public void valueWhenNull() {
        custJsDO.setFirstName(null);
        assertThat(custJsVO.getFirstName(), nullValue());
    }

    @Test
    public void association() {
        assertThat(custJsVO.getCountryOfBirth(), equalTo(countryGbrDO));
    }

    @Test
    public void associationWhenNull() {
        custJsDO.setCountryOfBirth(null);
        assertThat(custJsVO.getCountryOfBirth(), nullValue());
    }

    @Test
    public void collectionContainsWhenDoesAndDoesNot() {
        custJsDO.addToVisitedCountries(countryGbrDO);
        custJsDO.addToVisitedCountries(countryUsaDO);
        final List<Country> visitedCountries = custJsVO.getVisitedCountries();
        assertThat(visitedCountries.contains(countryGbrDO), is(true));
        assertThat(visitedCountries.contains(countryUsaDO), is(true));
        assertThat(visitedCountries.contains(countryAusDO), is(false));
    }

    @Test
    public void collectionSizeWhenEmpty() {
        assertThat(custJsVO.getVisitedCountries().size(), is(0));
    }

    @Test
    public void collectionSizeWhenNotEmpty() {
        custJsDO.addToVisitedCountries(countryGbrDO);
        custJsDO.addToVisitedCountries(countryUsaDO);

        assertThat(custJsVO.getVisitedCountries().size(), is(2));
    }

    @Test
    public void isEmptySizeWhenEmpty() {
        assertThat(custJsVO.getVisitedCountries().isEmpty(), is(true));
    }

    @Test
    public void isEmptySizeWhenNotEmpty() {
        custJsDO.addToVisitedCountries(countryGbrDO);
        custJsDO.addToVisitedCountries(countryUsaDO);

        assertThat(custJsVO.getVisitedCountries().isEmpty(), is(false));
    }

}
