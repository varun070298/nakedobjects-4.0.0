package org.nakedobjects.example.expenses.fixtures;

import org.nakedobjects.applib.value.Date;
import org.nakedobjects.example.expenses.claims.Claim;
import org.nakedobjects.example.expenses.employee.Employee;


public class SvenClaim1NewStatus extends AbstractClaimFixture {

    public static Employee SVEN;
    public static Employee DICK;
    public static Claim SVEN_CLAIM_1;

    @Override
    public void install() {
        SVEN = EmployeeFixture.SVEN;
        DICK = EmployeeFixture.DICK;

        SVEN_CLAIM_1 = createNewClaim(SVEN, DICK, "28th Mar - Sales call, London", ProjectCodeFixture.CODE1, new Date(2007, 4, 3));
        final Date mar28th = new Date(2007, 3, 28);
        addTaxi(SVEN_CLAIM_1, mar28th, null, 8.50, "Euston", "Mayfair", false);
        addMeal(SVEN_CLAIM_1, mar28th, "Lunch with client", 31.90);
        addTaxi(SVEN_CLAIM_1, mar28th, null, 11.00, "Mayfair", "City", false);

    }

}
