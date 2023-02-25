package org.nakedobjects.plugins.headless.junit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.nakedobjects.metamodel.commons.matchers.NofMatchers.classEqualTo;

import org.junit.Test;
import org.nakedobjects.metamodel.facets.object.validate.ValidateObjectFacetViaValidateMethod;
import org.nakedobjects.plugins.headless.applib.InvalidException;
import org.nakedobjects.plugins.headless.applib.ViewObject;
import org.nakedobjects.plugins.headless.junit.sample.domain.Customer;

public class SaveObjectsTest extends AbstractTest {

	@SuppressWarnings("unchecked")
	private ViewObject<Customer> asViewObject(final Customer proxiedNewCustomer) {
		return (ViewObject<Customer>) proxiedNewCustomer;
	}

	@Test
	public void invokingSaveThroughProxyMakesTransientObjectPersistent() {
		final Customer newCustomer = getDomainObjectContainer().newTransientInstance(Customer.class);
		assertThat(getDomainObjectContainer().isPersistent(newCustomer),is(false));
		final Customer newCustomerViewObject = getHeadlessViewer().view(newCustomer);
		newCustomerViewObject.setCustomerNumber(123);
		newCustomerViewObject.setLastName("Smith");
		newCustomerViewObject.setMandatoryAssociation(countryGbrDO);
		newCustomerViewObject.setMandatoryValue("foo");
		newCustomerViewObject.setMaxLengthField("abc");
		newCustomerViewObject.setRegExCaseInsensitiveField("ABCd");
		newCustomerViewObject.setRegExCaseSensitiveField("abcd");
		final ViewObject<Customer> proxyNewCustomer = asViewObject(newCustomerViewObject);
		proxyNewCustomer.save();
		assertThat(getDomainObjectContainer().isPersistent(newCustomer),
				is(true));
	}

	@Test
	public void invokingSaveOnThroughProxyOnAlreadyPersistedObjectJustUpdatesIt() {
		// just to get into valid state
		custJsDO.setCustomerNumber(123);
		custJsDO.setLastName("Smith");
		custJsDO.setMandatoryAssociation(countryGbrDO);
		custJsDO.setMandatoryValue("foo");
		custJsDO.setMaxLengthField("abc");
		custJsDO.setRegExCaseInsensitiveField("ABCd");
		custJsDO.setRegExCaseSensitiveField("abcd");
		
		assertThat(getDomainObjectContainer().isPersistent(custJsDO), is(true));
		
		final ViewObject<Customer> proxyNewCustomer = asViewObject(custJsVO);
		proxyNewCustomer.save();
		
		assertThat(getDomainObjectContainer().isPersistent(custJsDO), is(true));
	}

	@Test
	public void whenValidateMethodThenCanVetoSave() {
		final Customer newCustomer = getDomainObjectContainer().newTransientInstance(Customer.class);
		
		// just to get into valid state
		newCustomer.setCustomerNumber(123);
		newCustomer.setLastName("Smith");
		newCustomer.setMandatoryAssociation(countryGbrDO);
		newCustomer.setMandatoryValue("foo");
		newCustomer.setMaxLengthField("abc");
		newCustomer.setRegExCaseInsensitiveField("ABCd");
		newCustomer.setRegExCaseSensitiveField("abcd");

		final Customer newCustomerViewObject = getHeadlessViewer().view(newCustomer);
		newCustomer.validate = "No shakes";
		
		final ViewObject<Customer> proxyNewCustomer = asViewObject(newCustomerViewObject);
		try {
			assertThat(getDomainObjectContainer().isPersistent(newCustomer),
					is(false));
			proxyNewCustomer.save();
			fail("An InvalidImperativelyException should have been thrown");
		} catch (final InvalidException ex) {

			assertThat(ex.getAdvisorClass(), classEqualTo(ValidateObjectFacetViaValidateMethod.class));
			assertThat(getDomainObjectContainer().isPersistent(newCustomer),
					is(false)); // not saved
			assertThat(ex.getMessage(), equalTo("No shakes"));
		}
	}
		     
}
