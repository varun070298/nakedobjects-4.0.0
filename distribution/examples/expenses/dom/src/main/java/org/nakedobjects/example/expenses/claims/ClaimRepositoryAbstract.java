package org.nakedobjects.example.expenses.claims;

import org.nakedobjects.applib.AbstractFactoryAndRepository;
import org.nakedobjects.applib.annotation.Debug;
import org.nakedobjects.applib.annotation.Exploration;
import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.example.expenses.employee.Employee;

import java.util.List;


public abstract class ClaimRepositoryAbstract extends AbstractFactoryAndRepository implements ClaimRepository {

    @Debug
    public List<ProjectCode> listAllCodes() {
        return allInstances(ProjectCode.class);
    }

    @Exploration
    public List<Claim> allClaims() {
        return allInstances(Claim.class);
    }

    @Hidden
    public boolean descriptionIsUniqueForClaimant(final Employee employee, final String initialDescription) {
        final Claim claimPattern = newTransientInstance(Claim.class);
        claimPattern.setClaimant(employee);
        claimPattern.setDescription(initialDescription);
        claimPattern.setDateCreated(null);
        claimPattern.setStatus(null);
        final List<Claim> allClaims = allMatches(Claim.class, claimPattern);
        // as this might just be a partial match look at each one and check for an exact match
        for (Claim claim : allClaims) {
            if (claim.getDescription().equalsIgnoreCase(initialDescription)) {
                return false;
            }
        }
        return true;
    }

    @Hidden
    public ClaimStatus findClaimStatus(final String title) {
        return uniqueMatch(ClaimStatus.class, title);
    }

    @Hidden
    public ExpenseItemStatus findExpenseItemStatus(final String title) {
        return uniqueMatch(ExpenseItemStatus.class, title);
    }

    @Hidden
    public List<ExpenseItem> findExpenseItemsLike(final ExpenseItem item) {
        // Simple implementation: could be extended to compare any fields that have already been set on the
        // item provided.
        return findExpenseItemsOfType(item.getClaim().getClaimant(), item.getExpenseType());
    }

    @Hidden
    public abstract List<ExpenseItem> findExpenseItemsOfType(final Employee employee, final ExpenseType type);
}
