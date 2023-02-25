package org.nakedobjects.example.expenses.fixtures;

import org.nakedobjects.applib.value.Date;
import org.nakedobjects.example.expenses.claims.Claim;
import org.nakedobjects.example.expenses.claims.items.GeneralExpense;
import org.nakedobjects.example.expenses.employee.Employee;


public class SvenClaim3Returned extends AbstractClaimFixture {

    public static Employee SVEN;
    public static Employee DICK;;
    public static Claim SVEN_CLAIM_3;

    @Override
    public void install() {
        SVEN = EmployeeFixture.SVEN;
        DICK = EmployeeFixture.DICK;

        SVEN_CLAIM_3 = createNewClaim(SVEN, DICK, "23rd Feb - Sales trip, London", ProjectCodeFixture.CODE1, new Date(2007, 4, 3));
        final Date feb23rd = new Date(2007, 2, 23);
        addTaxi(SVEN_CLAIM_3, feb23rd, null, 18.00, "Euston", "City", false);
        addTaxi(SVEN_CLAIM_3, feb23rd, null, 10.00, "City", "West End", false);
        final GeneralExpense meal = addMeal(SVEN_CLAIM_3, feb23rd, "Lunch with client", 50.00);
        SVEN_CLAIM_3.submit(DICK, false);
        meal.reject("Too expensive");
        SVEN_CLAIM_3.approveItems(true);
        SVEN_CLAIM_3.returnToClaimant("Please discuss Meal item with me", false);

    }

}
