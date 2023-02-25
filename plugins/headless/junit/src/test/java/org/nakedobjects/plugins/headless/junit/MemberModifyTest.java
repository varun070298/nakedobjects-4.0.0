package org.nakedobjects.plugins.headless.junit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.nakedobjects.plugins.headless.applib.InvalidException;
import org.nakedobjects.plugins.headless.junit.sample.domain.Country;
import org.nakedobjects.plugins.headless.junit.sample.domain.Order;


public class MemberModifyTest extends AbstractTest {

    @Test
    public void valueModifiedToNonNull() {
        custJsVO.setFirstName("Dick");

        assertThat(custJsVO.getFirstName(), equalTo("Dick"));
    }

    @Test
    public void valueModifiedToNull() {
        custJsVO.setFirstName(null);

        assertThat(custJsVO.getFirstName(), nullValue());
    }

    @Test
    public void whenValueModifyCalledRatherThanSetForNonNull() {
        custJsVO.setFirstName("Dick");
        assertThat(custJsDO.modifyFirstNameCalled, is(true));
    }

    @Test
    public void whenValueClearCalledRatherThanSetForNull() {
        custJsVO.setFirstName(null);
        assertThat(custJsDO.clearFirstNameCalled, is(true));
    }

    @Test
    public void whenAssociationModifyCalledRatherThanSetForNonNull() {
        custJsVO.setCountryOfBirth(countryUsaDO);
        assertThat(custJsDO.modifyCountryOfBirthCalled, is(true));
    }

    @Test
    public void whenAssociationClearCalledRatherThanSetForNull() {
        custJsVO.setCountryOfBirth(null);
        assertThat(custJsDO.clearCountryOfBirthCalled, is(true));
    }

    @Test
    public void cannotUseAddDirectlyOnCollections() {
        final List<Country> visitedCountries = custJsVO.getVisitedCountries();
        try {
            visitedCountries.add(countryGbrDO);
            fail("UnsupportedOperationException should have been thrown.");
        } catch (final UnsupportedOperationException ex) {
            // expected
        }
    }

    @Test
    public void cannotUseRemoveDirectlyOnCollections() {
        final List<Country> visitedCountries = custJsVO.getVisitedCountries();
        try {
            visitedCountries.remove(countryGbrDO);
            fail("UnsupportedOperationException should have been thrown.");
        } catch (final UnsupportedOperationException ex) {
            // expected
        }
    }

    @Test
    public void cannotUseClearDirectlyOnCollections() {
        final List<Country> visitedCountries = custJsVO.getVisitedCountries();
        try {
            visitedCountries.clear();
            fail("UnsupportedOperationException should have been thrown.");
        } catch (final UnsupportedOperationException ex) {
            // expected
        }
    }

    @Test
    public void sttemptingToAddNullObjectIntoCollectionThrowsException() {
        try {
            custJsVO.addToVisitedCountries(null);
            fail("Exception should have been raised.");
        } catch (final IllegalArgumentException ex) {
            // expected
        }
    }

    @Test
    public void removingNonExistentRemoveObjectFromCollectionDoesNothing() {
        assertThat(custJsDO.getVisitedCountries().contains(countryGbrDO), is(false));

        custJsVO.removeFromVisitedCountries(countryGbrDO);
        // no exception raised.
    }

    @Test
    public void canInvokeAction() {
        final int sizeBefore = custJsVO.getOrders().size();
        final Order orderBefore = custJsVO.getLastOrder();
        custJsVO.placeOrder(product355DO, 3);
        final Order orderAfter = custJsVO.getLastOrder();

        final int sizeAfter = custJsVO.getOrders().size();
        assertThat(sizeAfter, is(sizeBefore + 1));
        assertThat(orderAfter, is(not(orderBefore)));
    }

    @Test
    public void canInvokeActionIfOptionalValueParameterAndNullArgumentProvided() {
        custJsVO.actionWithOptionalValueParameter(null);
        assertThat(custJsDO.actionWithOptionalValueParameterArgument, nullValue());
    }

    @Test
    public void cannotInvokeActionIfMandatoryValueParameterAndNullArgumentProvided() {
        try {
            custJsVO.actionWithMandatoryValueParameter(null);
            fail("InvalidMandatoryException should have been thrown");
        } catch (final InvalidException ex) {
            assertThat(custJsDO.actionWithMandatoryValueParameterArgument, equalTo(Long.MAX_VALUE)); // ie
        }
    }

    @Test
    public void canInvokeActionIfOptionalReferenceParameterAndNullArgumentProvided() {
        custJsVO.actionWithOptionalReferenceParameter(null);
        assertThat(custJsDO.actionWithOptionalReferenceParameterArgument, nullValue());
    }

    @Test
    public void cannotInvokeActionIfMandatoryReferenceParameterAndNullArgumentProvided() {
        try {
            custJsVO.actionWithMandatoryReferenceParameter(null);
            fail("InvalidMandatoryException should have been thrown");
        } catch (final InvalidException ex) {
            assertThat(custJsDO.actionWithMandatoryReferenceParameterArgument, not(nullValue()));
        }
    }

    @Test
    public void canInvokeActionIfOptionalStringParameterAndEmptyStringProvidedAsArgument() {
        custJsVO.actionWithOptionalStringParameter("");
        assertThat(custJsDO.actionWithOptionalStringParameterArgument, equalTo(""));
    }

    @Test
    public void cannotInvokeActionIfMandatoryStringParameterAndEmptyStringProvidedAsArgument() {
        try {
            custJsVO.actionWithMandatoryStringParameter("");
            fail("InvalidMandatoryException should have been thrown");
        } catch (final InvalidException ex) {
            assertThat(custJsDO.actionWithMandatoryStringParameterArgument, equalTo("original value")); // ie
        }
    }

    @Test
    public void canInvokeActionIfParameterMatchRegularExpression() {
        custJsVO.actionWithRegExStringParameter("6789");
        assertThat(custJsDO.actionWithRegExStringParameterArgument, equalTo("6789"));
    }

    @Test
    public void cannotInvokeActionIfParameterDoesNotMatchRegularExpression() {
        try {
            custJsVO.actionWithRegExStringParameter("abcd"); // doesn't match [0-9]{4}
            fail("InvalidRegExException should have been thrown");
        } catch (final InvalidException ex) {
            assertThat(custJsDO.actionWithRegExStringParameterArgument, equalTo("1234")); // ie unchanged
        }
    }

    @Test
    public void canInvokeActionIfParameterNoLongerMaximumLength() {
        custJsVO.actionWithMaxLengthStringParameter("abcd");
        assertThat(custJsDO.actionWithMaxLengthStringParameterArgument, equalTo("abcd"));
    }

    @Test
    public void cannotInvokeActionIfParameterExceedsMaximumLength() {
        try {
            custJsVO.actionWithMaxLengthStringParameter("abcde");
            fail("InvalidMaxLengthException should have been thrown");
        } catch (final InvalidException ex) {
            assertThat(custJsDO.actionWithMaxLengthStringParameterArgument, equalTo("1234")); // ie unchanged
        }
    }

}
