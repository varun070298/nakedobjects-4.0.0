package org.nakedobjects.example.expenses.claims;

import org.nakedobjects.applib.annotation.Bounded;
import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.applib.annotation.Immutable;
import org.nakedobjects.applib.annotation.When;


@Bounded
@Immutable(When.ONCE_PERSISTED)
public class ClaimStatus extends Status {

    public static final String NEW = "New";

    @Hidden
    public boolean isNew() {
        return getTitleString().equals(NEW);
    }

    public static final String SUBMITTED = "Submitted For Approval";

    @Hidden
    public boolean isSubmitted() {
        return getTitleString().equals(SUBMITTED);
    }

    public static final String RETURNED = "Returned To Claimant";

    @Hidden
    public boolean isReturned() {
        return getTitleString().equals(RETURNED);
    }

    public static final String TO_BE_PAID = "Ready to be paid";

    @Hidden
    public boolean isToBePaid() {
        return getTitleString().equals(TO_BE_PAID);
    }

    public static final String PAID = "Paid";

    @Hidden
    public boolean isPaid() {
        return getTitleString().equals(PAID);
    }

}
