package org.nakedobjects.example.expenses.claims.items;

import java.util.List;

import org.nakedobjects.applib.AbstractDomainObject;
import org.nakedobjects.applib.annotation.Disabled;
import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.applib.annotation.MemberOrder;
import org.nakedobjects.applib.annotation.Named;
import org.nakedobjects.applib.value.Date;
import org.nakedobjects.applib.value.Money;
import org.nakedobjects.example.expenses.claims.AbstractClaim;
import org.nakedobjects.example.expenses.claims.Claim;
import org.nakedobjects.example.expenses.claims.ClaimRepository;
import org.nakedobjects.example.expenses.claims.ExpenseItem;
import org.nakedobjects.example.expenses.claims.ExpenseItemStatus;
import org.nakedobjects.example.expenses.claims.ExpenseType;
import org.nakedobjects.example.expenses.claims.ProjectCode;
import org.nakedobjects.example.expenses.services.UserFinder;


public abstract class AbstractExpenseItem extends AbstractDomainObject implements ExpenseItem {

    // {{ Title & Icon
    public String title() {
        final StringBuilder t = new StringBuilder();
        t.append(getExpenseType());
        return t.toString();
    }

    public String iconName() {
        return getExpenseType().iconName();
    }

    // }}

    // {{ Injected Services
    /*
     * This region contains references to the services (Repositories, Factories or other Services) used by
     * this domain object. The references are injected by the application container.
     */

    // {{ Injected: ClaimRepository
    private ClaimRepository claimRepository;

    /**
     * Injected by the application container.
     */
    public void setClaimRepository(final ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
    }

    // }}

    // {{ Injected: UserFinder
    private UserFinder userFinder;

    /**
     * This field is not persisted, nor displayed to the user.
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

    // {{ Life Cycle methods
    public void created() {
        changeStatusToNewIncomplete();
    }

    public void saved() {
        claim.addToExpenseItems(this);
    }

    // }}

    // {{ ExpenseType
    private ExpenseType expenseType;

    @Hidden
    public ExpenseType getExpenseType() {
        return this.expenseType;
    }

    public void setExpenseType(final ExpenseType expenseType) {
        this.expenseType = expenseType;
    }

    // }}


    // {{ Project Code
    private ProjectCode projectCode;

    @Hidden
    public void newProjectCode(final ProjectCode newCode) {
        modifyProjectCode(newCode);
    }

    @MemberOrder(sequence = "4")
    public ProjectCode getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(final ProjectCode projectCode) {
        this.projectCode = projectCode;
    }

    public void modifyProjectCode(final ProjectCode newCode) {
        setProjectCode(newCode);
        checkIfComplete();
    }

    public void clearProjectCode() {
        setProjectCode(null);
        checkIfComplete();
    }

    public String disableProjectCode() {
        return disabledIfLocked();
    }
    // }}

    // {{ Claim
    private AbstractClaim claim;

    @Hidden
    public AbstractClaim getClaim() {
        return this.claim;
    }

    public void setClaim(final AbstractClaim claim) {
        this.claim = claim;
    }

    // }}

    // {{ Amount
    private Money amount;

    @MemberOrder(sequence = "3")
    public Money getAmount() {
        return this.amount;
    }

    public void setAmount(final Money amount) {
        this.amount = amount;
    }

    public void modifyAmount(final Money newAmount) {
        if (newAmount != null) {
            // coerce currency to current currency
            setAmount(new Money(newAmount.doubleValue(), getClaim().currencyCode()));
            checkIfComplete();
            recalculateClaimTotalIfPersistent();
        }
    }

    public String validateAmount(final Money newAmount) {
        return validateAnyAmountField(newAmount);
    }

    public String disableAmount() {
        return disabledIfLocked();
    }

    public final static String CURRENCY_NOT_VALID_FOR_THIS_CLAIM = "Currency not valid for this claim";

    protected String checkCurrency(final Money newAmount) {
        if (newAmount == null || newAmount.getCurrency().equalsIgnoreCase(getClaim().currencyCode())) {
            return null;
        }
        return CURRENCY_NOT_VALID_FOR_THIS_CLAIM + ": " + newAmount.getCurrency();
    }

    public final static String AMOUNT_CANNOT_BE_NEGATIVE = "Amount cannot be negative";

    protected String validateAnyAmountField(final Money newAmount) {
        return newAmount != null && newAmount.isLessThanZero() ? AMOUNT_CANNOT_BE_NEGATIVE : checkCurrency(newAmount);
    }

    @Hidden
    public void initialiseAmount() {
        setAmount(new Money(0.0, getClaim().currencyCode()));
    }

    // }}

    // {{ Date Incurred
    private Date dateIncurred;

    @MemberOrder(sequence = "1")
    public Date getDateIncurred() {
        return dateIncurred;
    }

    public void setDateIncurred(final Date dateIncurred) {
        this.dateIncurred = dateIncurred;
    }

    public void modifyDateIncurred(final Date newDate) {
        setDateIncurred(newDate);
        checkIfComplete();
    }

    public void clearDateIncurred() {
        setDateIncurred(null);
        checkIfComplete();
    }

    public String disableDateIncurred() {
        return disabledIfLocked();
    }

    // }}

    // {{ Description
    private String description;

    @MemberOrder(sequence = "2")
    public String getDescription() {
        return description;
    }

    public void setDescription(final String Title) {
        this.description = Title;
    }

    public void modifyDescription(final String newTitle) {
        setDescription(newTitle);
        checkIfComplete();
    }

    public void clearDescription() {
        setDescription(null);
        checkIfComplete();
    }

    private final static String DESCRIPTION_WARN = "Description cannot be empty";

    public String validateDescription(final String newTitle) {
        return newTitle != null && newTitle.length() > 0 ? null : DESCRIPTION_WARN;
    }

    public String disableDescription() {
        return disabledIfLocked();
    }

    // }}

    // {{ Status
    private void changeStatusTo(final String title) {
        setStatus(claimRepository.findExpenseItemStatus(title));
    }

    @Hidden
    public boolean isNewIncomplete() {
        return getStatus().isNewIncomplete();
    }

    @Hidden
    public void changeStatusToNewIncomplete() {
        changeStatusTo(ExpenseItemStatus.NEW_INCOMPLETE);
    }

    @Hidden
    public boolean isNewComplete() {
        return getStatus().isNewComplete();
    }

    @Hidden
    public void changeStatusToNewComplete() {
        changeStatusTo(ExpenseItemStatus.NEW_COMPLETE);
    }

    @Hidden
    public boolean isApproved() {
        return getStatus().isApproved();
    }

    @Hidden
    public void changeStatusToApproved() {
        changeStatusTo(ExpenseItemStatus.APPROVED);
    }

    @Hidden
    public boolean isRejected() {
        return getStatus().isRejected();
    }

    @Hidden
    public void changeStatusToRejected() {
        changeStatusTo(ExpenseItemStatus.REJECTED);
    }

    @Hidden
    public boolean isQueried() {
        return getStatus().isQueried();
    }

    @Hidden
    public void changeStatusToQueried() {
        changeStatusTo(ExpenseItemStatus.QUERIED);
    }

    private ExpenseItemStatus status;

    @MemberOrder(sequence = "5")
    @Disabled
    public ExpenseItemStatus getStatus() {
        return status;
    }

    public void setStatus(final ExpenseItemStatus status) {
        this.status = status;
    }

    // }}

    // {{ Comment (visible only when status is Rejected or Queried)
    private String comment;

    @Disabled
    public String getComment() {
        return this.comment;
    }

    public void setComment(final String reason) {
        this.comment = reason;
    }

    public boolean hideComment() {
        return !(isRejected() || isQueried());
    }

    // }}

    // {{ Controls
    protected boolean mandatoryFieldsComplete() {
        return (amount != null && !amount.isZero() && dateIncurred != null && description != null && !description.equals("")
                && projectCode != null && mandatorySubClassFieldsComplete());
    }

    protected abstract boolean mandatorySubClassFieldsComplete();

    protected void checkIfComplete() {
        if (isNewComplete() && !mandatoryFieldsComplete()) {
            changeStatusToNewIncomplete();
        }
        if (isNewIncomplete() && mandatoryFieldsComplete()) {
            changeStatusToNewComplete();
        }
    }

    // {{ Locked
    private boolean locked;

    @Hidden
    public boolean isLocked() {
        return locked;
    }

    public void setLocked(final boolean locked) {
        this.locked = locked;
    }

    private final static String SUBMITTED_WARN = "Read-only : submitted";

    protected String disabledIfLocked() {
        return isLocked() ? SUBMITTED_WARN : null;
    }

    // }}
    // }}

    // {{ Copy From
    @MemberOrder(sequence = "5")
    public void copyFrom(final ExpenseItem otherItem) {
        if (belongsToSameClaim(otherItem)) {
            if (dateIncurred == null) {
                modifyDateIncurred(otherItem.getDateIncurred());
            }
        } else if (getClass().isInstance(otherItem)) {
            copyAllSameClassFields(otherItem);
        }
    }

    public String disableCopyFrom() {
        return disabledIfLocked();
    }

    public String validateCopyFrom(final ExpenseItem otherItem) {
        if (belongsToSameClaim(otherItem) || (getClass().equals(otherItem.getClass()))) {
            return null;
        }
        return COPY_WARN;
    }

    private final static String COPY_WARN = "Cannot copy";

    protected void copyAllSameClassFields(final ExpenseItem otherItem) {
        copyAllFieldsFromAbstractExpenseItem(otherItem);
        copyAnyEmptyFieldsSpecificToSubclassOfAbstractExpenseItem(otherItem);
    }

    protected void copyAllFieldsFromAbstractExpenseItem(final ExpenseItem otherItem) {
        if (amount == null || amount.isZero()) {
            // Guard against different currency
            if (otherItem.getAmount().getCurrency().equals(getAmount().getCurrency())) { // TODO: Use
                // hasSameCurrencyAs
                // on Money when
                // implemented
                modifyAmount(otherItem.getAmount());
            }
        }
        if (description == null || description.equals("")) {
            modifyDescription(otherItem.getDescription());
        }
        if (projectCode == null) {
            modifyProjectCode(otherItem.getProjectCode());
        }
    }

    protected abstract void copyAnyEmptyFieldsSpecificToSubclassOfAbstractExpenseItem(final ExpenseItem otherItem);

    protected boolean belongsToSameClaim(final ExpenseItem otherItem) {
        return claim.equals(otherItem.getClaim());
    }

    // }}

    // {{ Recalculate
    protected void checkIfCompleteAndRecalculateClaimTotalIfPersistent() {
        recalculateClaimTotalIfPersistent();
        if (!isLocked()) {
            checkIfComplete();
        }
    }

    protected void recalculateClaimTotalIfPersistent() {
        if (isPersistent(this)) {
            if (getClaim() instanceof Claim) {
                ((Claim) getClaim()).recalculateTotal();
            }
        }
    }

    // }}

    // {{ Approvals
    @MemberOrder(sequence = "1")
    public void approve() {
        changeStatusToApproved();
        recalculateClaimTotalIfPersistent();
    }

    public String disableApprove() {
        return disableApproverActions();
    }

    @MemberOrder(sequence = "3")
    public void reject(@Named("Reason")
    final String reason) {
        setComment(reason);
        changeStatusToRejected();
        recalculateClaimTotalIfPersistent();
    }

    public String disableReject(final String reason) {
        return disableApproverActions();
    }

    @MemberOrder(sequence = "2")
    public void query(@Named("Reason")
    final String reason) {
        setComment(reason);
        changeStatusToQueried();
        recalculateClaimTotalIfPersistent();
    }

    public String disableQuery(final String reason) {
        return disableApproverActions();
    }

    private String disableApproverActions() {
        if (isNewIncomplete()) {
            return CANNOT_APPROVE_AN_INCOMPLETE_ITEM;
        }
        return getClaim().disableApproverActionsOnAllItems();
    }

    public static final String CANNOT_APPROVE_AN_INCOMPLETE_ITEM = "Cannot approve an incomplete item";

    @Hidden
    public Money requestedOrApprovedAmount() {
        if (isRejected() || isQueried()) {
            return new Money(0, getClaim().currencyCode());
        }
        return getAmount();
    }

    // }}

    // {{ Find similar items
    @MemberOrder(sequence = "4")
    public List<ExpenseItem> findSimilarExpenseItems() {
        return claimRepository.findExpenseItemsLike(this);
    }

    // }}

}
