package org.nakedobjects.example.expenses.claims.items;

import org.nakedobjects.example.expenses.claims.ExpenseItem;


public class GeneralExpense extends AbstractExpenseItem {

    @Override
    protected void copyAnyEmptyFieldsSpecificToSubclassOfAbstractExpenseItem(final ExpenseItem otherItem) {
    // No extra fields to copy
    }

    @Override
    protected boolean mandatorySubClassFieldsComplete() {
        return true; // No extra fields to check.
    }
}
