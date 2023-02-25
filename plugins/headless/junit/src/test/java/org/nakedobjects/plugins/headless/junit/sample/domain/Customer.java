package org.nakedobjects.plugins.headless.junit.sample.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.nakedobjects.applib.AbstractDomainObject;
import org.nakedobjects.applib.annotation.DescribedAs;
import org.nakedobjects.applib.annotation.Disabled;
import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.applib.annotation.MaxLength;
import org.nakedobjects.applib.annotation.Named;
import org.nakedobjects.applib.annotation.Optional;
import org.nakedobjects.applib.annotation.RegEx;
import org.nakedobjects.applib.annotation.TypicalLength;
import org.nakedobjects.applib.annotation.When;
import org.nakedobjects.applib.clock.Clock;
import org.nakedobjects.applib.security.UserMemento;
import org.nakedobjects.applib.util.TitleBuffer;


public class Customer extends AbstractDomainObject {

    // {{ Identification Methods
    /**
     * Defines the title that will be displayed on the user interface in order to identity this object.
     */
    public String title() {
        final TitleBuffer t = new TitleBuffer();
        t.append(getFirstName()).append(getLastName());
        return t.toString();
    }
    // }}

    // {{ FirstName
    private String firstName;

    @DescribedAs("Given or christian name")
    @TypicalLength(20)
    @MaxLength(100)
    @Optional
    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }
    public boolean modifyFirstNameCalled = false;

    public void modifyFirstName(final String firstName) {
        setFirstName(firstName);
        this.modifyFirstNameCalled = true;
    }
    public boolean clearFirstNameCalled = false;

    public void clearFirstName() {
        setFirstName(null);
        this.clearFirstNameCalled = true;
    }

    public String validateFirstName;
    public String validateFirstNameExpectedArg;

    public String validateFirstName(final String firstName) {
        if (validateFirstNameExpectedArg != null && !validateFirstNameExpectedArg.equals(firstName)) {
            return "argument provided by XAT framework was incorrect";
        }
        return validateFirstName;
    }

    public String disableFirstName;

    public String disableFirstName() {
        return this.disableFirstName;
    }

    public boolean hideFirstName;

    public boolean hideFirstName() {
        return this.hideFirstName;
    }
    // }}

    // {{ CountryOfBirth
    private Country countryOfBirth;

    @Optional
    public Country getCountryOfBirth() {
        return countryOfBirth;
    }

    public void setCountryOfBirth(final Country countryOfBirth) {
        this.countryOfBirth = countryOfBirth;
    }

    public boolean modifyCountryOfBirthCalled = false;

    public void modifyCountryOfBirth(final Country countryOfBirth) {
        setCountryOfBirth(countryOfBirth);
        this.modifyCountryOfBirthCalled = true;
    }
    public boolean clearCountryOfBirthCalled = false;

    public void clearCountryOfBirth() {
        setCountryOfBirth(null);
        this.clearCountryOfBirthCalled = true;
    }

    public String validateCountryOfBirth;

    public String validateCountryOfBirth(final Country countryOfBirth) {
        return validateCountryOfBirth;
    }

    public String disableCountryOfBirth;

    public String disableCountryOfBirth() {
        return this.disableCountryOfBirth;
    }

    public boolean hideCountryOfBirth;

    public boolean hideCountryOfBirth() {
        return this.hideCountryOfBirth;
    }
    // }}

    // {{ VisitedCountries
    private List<Country> visitedCountries = new ArrayList<Country>();

    public List<Country> getVisitedCountries() {
        return this.visitedCountries;
    }

    @SuppressWarnings("unused")
    private void setVisitedCountries(final List<Country> visitedCountries) {
        this.visitedCountries = visitedCountries;
    }

    public void addToVisitedCountries(final Country country) {
        getVisitedCountries().add(country);
    }

    public void removeFromVisitedCountries(final Country country) {
        getVisitedCountries().remove(country);
    }
    public String validateAddToVisitedCountries;

    public String validateAddToVisitedCountries(final Country country) {
        return validateAddToVisitedCountries;
    }
    public String validateRemoveFromVisitedCountries;

    public String validateRemoveFromVisitedCountries(final Country country) {
        return validateRemoveFromVisitedCountries;
    }

    public String disableVisitedCountries;

    public String disableVisitedCountries() {
        return this.disableVisitedCountries;
    }

    public boolean hideVisitedCountries;

    public boolean hideVisitedCountries() {
        return this.hideVisitedCountries;
    }
    // }}

    // {{ AlwaysDisabledValue
    private String alwaysDisabledValue;

    @Disabled(When.ALWAYS)
    public String getAlwaysDisabledValue() {
        return this.alwaysDisabledValue;
    }

    public void setAlwaysDisabledValue(final String alwaysDisabled) {
        this.alwaysDisabledValue = alwaysDisabled;
    }
    // }}

    // {{ AlwaysDisabledAssociation
    private Country alwaysDisabledAssociation;

    @Disabled(When.ALWAYS)
    public Country getAlwaysDisabledAssociation() {
        return this.alwaysDisabledAssociation;
    }

    public void setAlwaysDisabledAssociation(final Country alwaysDisabled) {
        this.alwaysDisabledAssociation = alwaysDisabled;
    }
    // }}

    // {{ AlwaysDisabledCollection
    private List<Country> alwaysDisabledCollection = new ArrayList<Country>();

    @Disabled(When.ALWAYS)
    public List<Country> getAlwaysDisabledCollection() {
        return this.alwaysDisabledCollection;
    }

    @SuppressWarnings("unused")
    private void setAlwaysDisabledCollection(final List<Country> alwaysDisabledCollection) {
        this.alwaysDisabledCollection = alwaysDisabledCollection;
    }

    public void addToAlwaysDisabledCollection(final Country country) {
        getAlwaysDisabledCollection().add(country);
    }

    public void removeFromAlwaysDisabledCollection(final Country country) {
        getAlwaysDisabledCollection().remove(country);
    }

    // }}

    // {{ AlwaysDisabledAction
    @Disabled(When.ALWAYS)
    public void alwaysDisabledAction() {}
    // }}

    // {{ SessionDisabledValue
    private String sessionDisabledValue;

    public String getSessionDisabledValue() {
        return this.sessionDisabledValue;
    }

    public void setSessionDisabledValue(final String sessionDisabled) {
        this.sessionDisabledValue = sessionDisabled;
    }

    public static String disableSessionDisabledValue(final UserMemento user) {
        return "disabled for this user";
    }
    // }}

    // {{ SessionDisabledAssociation
    private Country sessionDisabledAssociation;

    public Country getSessionDisabledAssociation() {
        return this.sessionDisabledAssociation;
    }

    public void setSessionDisabledAssociation(final Country sessionDisabled) {
        this.sessionDisabledAssociation = sessionDisabled;
    }

    public static String disableSessionDisabledAssociation(final UserMemento user) {
        return "disabled for this user";
    }
    // }}

    // {{ SessionDisabledCollection
    private List<Country> sessionDisabledCollection = new ArrayList<Country>();

    public List<Country> getSessionDisabledCollection() {
        return this.sessionDisabledCollection;
    }

    @SuppressWarnings("unused")
    private void setSessionDisabledCollection(final List<Country> sessionDisabledCollection) {
        this.sessionDisabledCollection = sessionDisabledCollection;
    }

    public void addToSessionDisabledCollection(final Country country) {
        getSessionDisabledCollection().add(country);
    }

    public void removeFromSessionDisabledCollection(final Country country) {
        getSessionDisabledCollection().remove(country);
    }

    public static String disableSessionDisabledCollection(final UserMemento user) {
        return "disabled for this user";
    }

    // }}

    // {{ SessionDisabledAction
    public void sessionDisabledAction() {}

    public static String disableSessionDisabledAction(final UserMemento user) {
        return "disabled for this user";
    }
    // }}

    // {{ AlwaysHiddenValue
    private String alwaysHiddenValue;

    @Hidden(When.ALWAYS)
    public String getAlwaysHiddenValue() {
        return this.alwaysHiddenValue;
    }

    public void setAlwaysHiddenValue(final String alwaysHidden) {
        this.alwaysHiddenValue = alwaysHidden;
    }
    // }}

    // {{ AlwaysHiddenAssociation
    private Country alwaysHiddenAssociation;

    @Hidden(When.ALWAYS)
    public Country getAlwaysHiddenAssociation() {
        return this.alwaysHiddenAssociation;
    }

    public void setAlwaysHiddenAssociation(final Country alwaysHidden) {
        this.alwaysHiddenAssociation = alwaysHidden;
    }
    // }}

    // {{ AlwaysHiddenCollection
    private List<Country> alwaysHiddenCollection = new ArrayList<Country>();

    @Hidden(When.ALWAYS)
    public List<Country> getAlwaysHiddenCollection() {
        return this.alwaysHiddenCollection;
    }

    @SuppressWarnings("unused")
    private void setAlwaysHiddenCollection(final List<Country> alwaysHiddenCollection) {
        this.alwaysHiddenCollection = alwaysHiddenCollection;
    }

    public void addToAlwaysHiddenCollection(final Country country) {
        getAlwaysHiddenCollection().add(country);
    }

    public void removeFromAlwaysHiddenCollection(final Country country) {
        getAlwaysHiddenCollection().remove(country);
    }

    // }}

    // {{ SessionDisabledAction
    @Hidden(When.ALWAYS)
    public void alwaysHiddenAction() {}
    // }}

    // {{ SessionHiddenValue
    private String sessionHiddenValue;

    public String getSessionHiddenValue() {
        return this.sessionHiddenValue;
    }

    public void setSessionHiddenValue(final String sessionHidden) {
        this.sessionHiddenValue = sessionHidden;
    }

    public static boolean hideSessionHiddenValue(final UserMemento user) {
        return true;
    }
    // }}

    // {{ SessionHiddenAssociation
    private Country sessionHiddenAssociation;

    public Country getSessionHiddenAssociation() {
        return this.sessionHiddenAssociation;
    }

    public void setSessionHiddenAssociation(final Country sessionHidden) {
        this.sessionHiddenAssociation = sessionHidden;
    }

    public static boolean hideSessionHiddenAssociation(final UserMemento user) {
        return true;
    }
    // }}

    // {{ SessionHiddenCollection
    private List<Country> sessionHiddenCollection = new ArrayList<Country>();

    public List<Country> getSessionHiddenCollection() {
        return this.sessionHiddenCollection;
    }

    @SuppressWarnings("unused")
    private void setSessionHiddenCollection(final List<Country> sessionHiddenCollection) {
        this.sessionHiddenCollection = sessionHiddenCollection;
    }

    public void addToSessionHiddenCollection(final Country country) {
        getSessionHiddenCollection().add(country);
    }

    public void removeFromSessionHiddenCollection(final Country country) {
        getSessionHiddenCollection().remove(country);
    }

    public static boolean hideSessionHiddenCollection(final UserMemento user) {
        return true;
    }

    // }}

    // {{ SessionHiddenAction
    public void sessionHiddenAction() {}

    public static boolean hideSessionHiddenAction(final UserMemento user) {
        return true;
    }
    // }}

    // {{ Mandatory
    private String mandatoryValue;

    public String getMandatoryValue() {
        return this.mandatoryValue;
    }

    public void setMandatoryValue(final String mandatory) {
        this.mandatoryValue = mandatory;
    }
    // }}

    // {{ Mandatory
    private Country mandatoryAssociation;

    public Country getMandatoryAssociation() {
        return this.mandatoryAssociation;
    }

    public void setMandatoryAssociation(final Country mandatory) {
        this.mandatoryAssociation = mandatory;
    }
    // }}

    // {{ Optional
    private String optionalValue;

    @Optional
    public String getOptionalValue() {
        return this.optionalValue;
    }

    public void setOptionalValue(final String optional) {
        this.optionalValue = optional;
    }
    // }}

    // {{ OptionalAssociation
    private Country optionalAssociation;

    @Optional
    public Country getOptionalAssociation() {
        return this.optionalAssociation;
    }

    public void setOptionalAssociation(final Country optional) {
        this.optionalAssociation = optional;
    }
    // }}

    // {{ OptionalCollection
    private List<Country> optionalCollection = new ArrayList<Country>();

    @Optional
    public List<Country> getOptionalCollection() {
        return this.optionalCollection;
    }

    @SuppressWarnings("unused")
    private void setOptionalCollection(final List<Country> optionalCollection) {
        this.optionalCollection = optionalCollection;
    }

    public void addToOptionalCollection(final Country country) {
        getOptionalCollection().add(country);
    }

    public void removeFromOptionalCollection(final Country country) {
        getOptionalCollection().remove(country);
    }
    // }}

    // {{ MaxLength
    private String maxLengthField;

    @MaxLength(10)
    public String getMaxLengthField() {
        return this.maxLengthField;
    }

    public void setMaxLengthField(final String maxLength) {
        this.maxLengthField = maxLength;
    }
    // }}

    // {{ RegExCaseSensitive
    private String regExCaseSensitiveField;

    @RegEx(validation = "abc.+", caseSensitive = true)
    public String getRegExCaseSensitiveField() {
        return this.regExCaseSensitiveField;
    }

    public void setRegExCaseSensitiveField(final String regEx) {
        this.regExCaseSensitiveField = regEx;
    }
    // }}

    // {{ RegExCaseInsensitive
    private String regExCaseInsensitiveField;

    @RegEx(validation = "abc.+", caseSensitive = false)
    public String getRegExCaseInsensitiveField() {
        return this.regExCaseInsensitiveField;
    }

    public void setRegExCaseInsensitiveField(final String regExCaseInsensitive) {
        this.regExCaseInsensitiveField = regExCaseInsensitive;
    }
    // }}

    // {{ LastName
    private String lastName;

    @DescribedAs("Family name or surname")
    @MaxLength(100)
    @TypicalLength(30)
    @Named("Surname")
    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public void modifyLastName(final String lastName) {
        this.lastName = lastName;
    }
    // }}

    // {{ CustomerNumber
    private Integer customerNumber;

    @Disabled(When.ONCE_PERSISTED)
    public Integer getCustomerNumber() {
        return this.customerNumber;
    }

    public void setCustomerNumber(final Integer customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String validateCustomerNumber(final Integer customerNumber) {
        return null;
    }
    // }}

    // {{ Orders
    private List<Order> orders = new ArrayList<Order>();

    public List<Order> getOrders() {
        return this.orders;
    }

    @SuppressWarnings("unused")
    private void setOrders(final List<Order> orders) {
        this.orders = orders;
    }

    public void addToOrders(final Order order) {
        getOrders().add(order);
    }

    public void removeFromOrders(final Order order) {
        getOrders().remove(order);
    }
    // }}

    // {{ LastOrder
    private Order lastOrder;

    @Disabled
    public Order getLastOrder() {
        return this.lastOrder;
    }

    public void setLastOrder(final Order lastOrder) {
        this.lastOrder = lastOrder;
    }

    public void modifyLastOrder(final Order lastOrder) {
        setLastOrder(lastOrder);
    }

    public void clearLastOrder() {
        setLastOrder(null);
    }

    // }}

    // {{ PlaceOrder
    public void placeOrder(final Product p, @Named("Quantity")
    final Integer quantity) {
        final Order order = getContainer().newTransientInstance(Order.class);
        order.modifyCustomer(this);
        order.modifyProduct(p);
        order.setOrderDate(new Date(Clock.getTime()));
        order.setQuantity(quantity);
        addToOrders(order);
        modifyLastOrder(order);
        order.makePersistent();
    }
    public String validatePlaceOrder;

    public String validatePlaceOrder(final Product p, final Integer quantity) {
        return validatePlaceOrder;
    }
    public String disablePlaceOrder;

    public String disablePlaceOrder() {
        return disablePlaceOrder;
    }
    public boolean hidePlaceOrder;

    public boolean hidePlaceOrder() {
        return hidePlaceOrder;
    }

    public Object[] defaultPlaceOrder() {
        Product lastProductOrdered = null;
        if (getLastOrder() != null) {
            lastProductOrdered = getLastOrder().getProduct();
        }
        return new Object[] { lastProductOrdered, new Integer(1) };
    }
    // }}

    // {{ MoreOrders
    private List<Order> moreOrders = new ArrayList<Order>();

    @Disabled
    public List<Order> getMoreOrders() {
        return this.moreOrders;
    }

    @SuppressWarnings("unused")
    private void setMoreOrders(final List<Order> moreOrders) {
        this.moreOrders = moreOrders;
    }

    public void addToMoreOrders(final Order order) {
        getMoreOrders().add(order);
    }

    public void removeFromMoreOrders(final Order order) {
        getMoreOrders().remove(order);
    }
    // }}

    public String validate;
    public boolean validateCalled = false;

    public String validate() {
        validateCalled = true;
        return validate;
    }

    public Long actionWithOptionalValueParameterArgument = Long.MAX_VALUE;

    public void actionWithOptionalValueParameter(@Optional
    @Named("Amount")
    final Long val) {
        actionWithOptionalValueParameterArgument = val;
    }

    public Long actionWithMandatoryValueParameterArgument = Long.MAX_VALUE;

    public void actionWithMandatoryValueParameter(@Named("Amount")
    final Long val) {
        actionWithMandatoryValueParameterArgument = val;
    }

    public Product actionWithMandatoryReferenceParameterArgument = new Product();

    public void actionWithMandatoryReferenceParameter(final Product product) {
        actionWithMandatoryReferenceParameterArgument = product;
    }

    public Product actionWithOptionalReferenceParameterArgument = new Product();

    public void actionWithOptionalReferenceParameter(@Optional
    final Product product) {
        actionWithOptionalReferenceParameterArgument = product;
    }

    public String actionWithOptionalStringParameterArgument = "original value";

    public void actionWithOptionalStringParameter(@Optional
    @Named("Amount")
    final String val) {
        actionWithOptionalStringParameterArgument = val;
    }

    public String actionWithMandatoryStringParameterArgument = "original value";

    public void actionWithMandatoryStringParameter(@Named("Amount")
    final String val) {
        actionWithMandatoryStringParameterArgument = val;
    }

    public String actionWithMaxLengthStringParameterArgument = "1234";

    public void actionWithMaxLengthStringParameter(@Named("Amount")
    @MaxLength(4)
    final String val) {
        actionWithMaxLengthStringParameterArgument = val;
    }

    public String actionWithRegExStringParameterArgument = "1234";

    public void actionWithRegExStringParameter(@Named("Amount")
    @RegEx(validation = "[0-9]{4}")
    final String val) {
        actionWithRegExStringParameterArgument = val;
    }


}
