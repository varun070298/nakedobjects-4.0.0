package org.nakedobjects.example.expenses.fixtures;

import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.applib.fixtures.AbstractFixture;
import org.nakedobjects.example.expenses.claims.ClaimStatus;
import org.nakedobjects.example.expenses.claims.ExpenseItemStatus;


public class StatusFixture extends AbstractFixture {


    public static ClaimStatus NEW_CLAIM;
    public static ClaimStatus SUBMITTED;
    public static ClaimStatus RETURNED;
    public static ClaimStatus PAID;

    @Override
    public void install() {
        createExpenseItemStatus(ExpenseItemStatus.NEW_COMPLETE);
        createExpenseItemStatus(ExpenseItemStatus.NEW_INCOMPLETE);
        createExpenseItemStatus(ExpenseItemStatus.REJECTED);
        createExpenseItemStatus(ExpenseItemStatus.APPROVED);
        createExpenseItemStatus(ExpenseItemStatus.QUERIED);

        NEW_CLAIM = createClaimStatus(ClaimStatus.NEW);
        SUBMITTED = createClaimStatus(ClaimStatus.SUBMITTED);
        RETURNED = createClaimStatus(ClaimStatus.RETURNED);
        PAID = createClaimStatus(ClaimStatus.PAID);
    }

    @Hidden
    public ExpenseItemStatus createExpenseItemStatus(final String description) {
        final ExpenseItemStatus status = newTransientInstance(ExpenseItemStatus.class);
        status.setTitleString(description);
        getContainer().persist(status);
        return status;
    }

    @Hidden
    public ClaimStatus createClaimStatus(final String description) {
        final ClaimStatus status = newTransientInstance(ClaimStatus.class);
        status.setTitleString(description);
        persist(status);
        return status;
    }
}
