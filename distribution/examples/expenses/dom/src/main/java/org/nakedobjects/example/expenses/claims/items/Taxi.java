package org.nakedobjects.example.expenses.claims.items;

import org.nakedobjects.example.expenses.claims.ExpenseItem;


public class Taxi extends Journey {

    @Override
    protected void copyAnyEmptyFieldsSpecificToSubclassOfJourney(final ExpenseItem otherItem) {
    // No extra fields to copy.
    }

    @Override
    protected boolean mandatoryJourneySubClassFieldsComplete() {
        return true; // No extra fields tocheck
    }

}
