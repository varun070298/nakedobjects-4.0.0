package org.nakedobjects.example.expenses.claims;

import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.applib.annotation.MemberOrder;
import org.nakedobjects.applib.value.Date;
import org.nakedobjects.applib.value.Money;


public interface ExpenseItem {

    // {{ Claim
    void setClaim(final AbstractClaim claim);

    @Hidden
    AbstractClaim getClaim();

    // }}

    // {{ ExpenseType
    @Hidden
    ExpenseType getExpenseType();

    void setExpenseType(ExpenseType type);

    // }}

    // {{ Amount
    Money requestedOrApprovedAmount();

    @MemberOrder(sequence = "3")
    Money getAmount();

    void modifyAmount(final Money amount);

    /**
     * Sets the amount to 0.0 in the correct currency
     * 
     */
    void initialiseAmount();

    // }}

    // {{ Date Incurred
    @MemberOrder(sequence = "1")
    Date getDateIncurred();

    void modifyDateIncurred(final Date dateIncurred);

    // }}

    // {{ Description
    @MemberOrder(sequence = "2")
    String getDescription();

    void modifyDescription(final String title);

    // }}

    // {{ Status
    @MemberOrder(sequence = "5")
    ExpenseItemStatus getStatus();

    // }}

    // {{ Project Code
    @MemberOrder(sequence = "4")
    ProjectCode getProjectCode();

    void modifyProjectCode(final ProjectCode projectCodeImpl);

    // }}

    void setLocked(boolean locked);

    void copyFrom(final ExpenseItem otherItem);

    void approve();

    void reject(final String reason);

    void query(final String reason);

    // {{ Status tests
    @Hidden
    public boolean isNewIncomplete();

    @Hidden
    public void changeStatusToNewIncomplete();

    @Hidden
    public boolean isNewComplete();

    @Hidden
    public void changeStatusToNewComplete();

    @Hidden
    public boolean isApproved();

    @Hidden
    public void changeStatusToApproved();

    @Hidden
    public boolean isRejected();

    @Hidden
    public void changeStatusToRejected();

    @Hidden
    public boolean isQueried();

    @Hidden
    public void changeStatusToQueried();
    // }}

}
