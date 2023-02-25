package org.nakedobjects.example.expenses.fixtures;

import org.nakedobjects.applib.value.Date;
import org.nakedobjects.example.expenses.claims.Claim;
import org.nakedobjects.example.expenses.employee.Employee;


public class SvenClaim5New extends AbstractClaimFixture {

    public static Employee SVEN;
    public static Employee DICK;;
    public static Claim SVEN_CLAIM_5;

    @Override
    public void install() {
        SVEN = EmployeeFixture.SVEN;
        DICK = EmployeeFixture.DICK;

        SVEN_CLAIM_5 = createNewClaim(SVEN, null, "14th Mar - Sales call, London", ProjectCodeFixture.CODE1, new Date(2007, 4, 3));
        final Date mar14th = new Date(107, 3, 14);
        addTaxi(SVEN_CLAIM_5, mar14th, null, 18.00, "Euston", "City", true);
        addMeal(SVEN_CLAIM_5, mar14th, "Lunch with client", 50.00);
    }
}
