package org.nakedobjects.example.expenses.fixtures;

import org.nakedobjects.applib.fixtures.AbstractFixture;


public class RefdataFixture extends AbstractFixture {

    public RefdataFixture() {
        addFixture(new ExpenseTypeFixture());
        addFixture(new StatusFixture());
        addFixture(new ProjectCodeFixture());
        addFixture(new CurrencyFixture());
    }

    @Override
    public void install() {

    }

}
