package org.nakedobjects.example.expenses.claims;

import org.nakedobjects.applib.annotation.Bounded;
import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.applib.annotation.Immutable;
import org.nakedobjects.applib.annotation.When;


@Bounded
@Immutable(When.ONCE_PERSISTED)
public class ExpenseItemStatus extends Status {

    public static final String NEW_INCOMPLETE = "New - Incomplete";
    public static final String NEW_COMPLETE = "New - Complete";
    public static final String REJECTED = "Rejected";
    public static final String APPROVED = "Approved";
    public static final String QUERIED = "Queried";

    @Hidden
    public boolean isNewIncomplete() {
        return getTitleString().equals(NEW_INCOMPLETE);
    }

    @Hidden
    public boolean isNewComplete() {
        return getTitleString().equals(NEW_COMPLETE);
    }

    @Hidden
    public boolean isApproved() {
        return getTitleString().equals(APPROVED);
    }

    @Hidden
    public boolean isRejected() {
        return getTitleString().equals(REJECTED);
    }

    @Hidden
    public boolean isQueried() {
        return getTitleString().equals(QUERIED);
    }

}
