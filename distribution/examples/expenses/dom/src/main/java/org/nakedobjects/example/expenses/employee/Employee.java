package org.nakedobjects.example.expenses.employee;

import org.nakedobjects.applib.AbstractDomainObject;
import org.nakedobjects.applib.annotation.Disabled;
import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.applib.annotation.MemberOrder;
import org.nakedobjects.applib.annotation.Optional;
import org.nakedobjects.applib.annotation.RegEx;
import org.nakedobjects.example.expenses.currency.Currency;
import org.nakedobjects.example.expenses.recordedAction.Actor;
import org.nakedobjects.example.expenses.recordedAction.RecordActionService;
import org.nakedobjects.example.expenses.recordedAction.RecordedActionContext;
import org.nakedobjects.example.expenses.services.UserFinder;


public class Employee extends AbstractDomainObject implements Actor, RecordedActionContext {

    // {{ Title
    public String title() {
        return getName();
    }

    // }}

    // {{ Injected Services

    // {{ Injected: RecordActionService
    private RecordActionService recordActionService;

    /**
     * This property is not persisted, nor displayed to the user.
     */
    protected RecordActionService getRecordActionService() {
        return this.recordActionService;
    }

    /**
     * Injected by the application container.
     */
    public void setRecordActionService(final RecordActionService recordActionService) {
        this.recordActionService = recordActionService;
    }

    // }}

    // {{ Injected: UserFinder
    private UserFinder userFinder;

    /**
     * This property is not persisted, nor displayed to the user.
     */
    protected UserFinder getUserFinder() {
        return this.userFinder;
    }

    /**
     * Injected by the application container.
     */
    public void setUserFinder(final UserFinder userFinder) {
        this.userFinder = userFinder;
    }

    // }}

    // }}

    // {{ Name
    private String name;

    @MemberOrder(sequence = "1")
    @Disabled
    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    // }}

    // {{ UserName field
    private String userName;

    /**
     * Hidden field containing this TeamMember log-on username
     */
    @Hidden
    public String getUserName() {
        return userName;
    }

    /**
     * @see #getUserName
     */
    public void setUserName(final String variable) {
        this.userName = variable;
    }

    // }}

    // {{ EmailAddress
    private String emailAddress;

    @MemberOrder(sequence = "2")
    @Optional
    @RegEx(validation = "(\\w+\\.)*\\w+@(\\w+\\.)+[A-Za-z]+")
    public String getEmailAddress() {
        return this.emailAddress;
    }

    public void setEmailAddress(final String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void modifyEmailAddress(final String emailAddress) {
        getRecordActionService().recordFieldChange(this, "Email Address", getEmailAddress(), emailAddress);
        setEmailAddress(emailAddress);
    }

    public void clearEmailAddress() {
        getRecordActionService().recordFieldChange(this, "Email Address", getEmailAddress(), "EMPTY");
        setEmailAddress(null);
    }

    public boolean hideEmailAddress() {
        return !employeeIsCurrentUser();
    }

    private Object currentUser;

    private boolean employeeIsCurrentUser() {
        if (currentUser == null) {
            currentUser = getUserFinder().currentUserAsObject();
        }
        return currentUser == this;
    }

    // }}

    // {{ Currency
    private Currency currency;

    @MemberOrder(sequence = "3")
    @Disabled
    public Currency getCurrency() {
        return this.currency;
    }

    public void setCurrency(final Currency currency) {
        this.currency = currency;
    }

    // }}

    // {{ NormalApprover
    private Employee normalApprover;

    @MemberOrder(sequence = "4")
    @Optional
    public Employee getNormalApprover() {
        return this.normalApprover;
    }

    public void setNormalApprover(final Employee normalAuthoriser) {
        this.normalApprover = normalAuthoriser;
    }

    public void modifyNormalApprover(final Employee normalAuthoriser) {
        getRecordActionService().recordFieldChange(this, "Normal Approver", getNormalApprover(), normalApprover);
        setNormalApprover(normalAuthoriser);
    }

    public void clearNormalApprover() {
        getRecordActionService().recordFieldChange(this, "Normal Approver", getNormalApprover(), "EMPTY");
        setNormalApprover(null);
    }

    public String validateNormalApprover(final Employee newApprover) {
        return newApprover == this ? CANT_BE_APPROVER_FOR_OWN_CLAIMS : null;
    }

    public String disableNormalApprover() {
        return employeeIsCurrentUser() ? null : NOT_MODIFIABLE;
    }

    public static final String NOT_MODIFIABLE = "Not modifiable by current user";
    public static final String CANT_BE_APPROVER_FOR_OWN_CLAIMS = "Can't be the approver for your own claims";

    // }}
}
