package org.nakedobjects.example.expenses.services.inmemory;

import org.nakedobjects.applib.Filter;
import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.example.expenses.claims.Claim;
import org.nakedobjects.example.expenses.claims.ClaimRepositoryAbstract;
import org.nakedobjects.example.expenses.claims.ClaimStatus;
import org.nakedobjects.example.expenses.claims.ExpenseItem;
import org.nakedobjects.example.expenses.claims.ExpenseType;
import org.nakedobjects.example.expenses.claims.items.AbstractExpenseItem;
import org.nakedobjects.example.expenses.employee.Employee;

import java.util.ArrayList;
import java.util.List;


public class ClaimRepositoryInMemory extends ClaimRepositoryAbstract {

    @SuppressWarnings("unused")
    private Integer maxClaimsToRetrieve;

    @Hidden
    public List<Claim> findClaims(final Employee employee, final ClaimStatus status, final String description) {
        return allMatches(Claim.class, new Filter<Claim>() {
            public boolean accept(final Claim claim) {
                return (employee == null || claim.getClaimant() == employee) && (status == null || claim.getStatus() == status)
                        && (description == null || claim.getDescription().contains(description));
            }
        });
    }

    @Hidden
    public List<Claim> findRecentClaims(final Employee employee) {
        maxClaimsToRetrieve = Integer.valueOf(MAX_ITEMS);
        final List<Claim> foundClaims = findClaims(employee, null, null);
        maxClaimsToRetrieve = null;
        return foundClaims;
    }

    @Hidden
    public List<Claim> findClaimsAwaitingApprovalBy(final Employee approver) {
        return allMatches(Claim.class, new Filter<Claim>() {
            public boolean accept(final Claim claim) {
                return claim.getStatus().isSubmitted() && claim.getApprover() == approver;
            }
        });
    }

    @Override
    @Hidden
    public List<ExpenseItem> findExpenseItemsOfType(final Employee employee, final ExpenseType type) {
        final List<ExpenseItem> items = new ArrayList<ExpenseItem>();
        for (final AbstractExpenseItem item : allInstances(AbstractExpenseItem.class)) {
            if (item.getExpenseType() == type && item.getClaim().getClaimant() == employee) {
                items.add(item);
            }
        }
        return items;
    }

}
