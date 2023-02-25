package org.nakedobjects.example.expenses.claims;

import org.nakedobjects.example.expenses.employee.Employee;

import java.util.List;


public interface ClaimRepository {

    final static int MAX_CLAIMS = 20;

    final static int MAX_ITEMS = 10;

    List<Claim> findClaims(final Employee employee, final ClaimStatus status, final String description);

    List<Claim> findRecentClaims(final Employee employee);

    boolean descriptionIsUniqueForClaimant(final Employee employee, final String initialDescription);

    List<ExpenseItem> findExpenseItemsLike(final ExpenseItem item);

    List<Claim> findClaimsAwaitingApprovalBy(Employee approver);

    ClaimStatus findClaimStatus(String title);

    ExpenseItemStatus findExpenseItemStatus(String title);

}
