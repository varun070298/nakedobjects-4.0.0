package org.nakedobjects.examples.claims.fixture;

import org.nakedobjects.applib.fixtures.AbstractFixture;
import org.nakedobjects.applib.value.Date;
import org.nakedobjects.applib.value.Money;

import org.nakedobjects.examples.claims.dom.claim.Claim;
import org.nakedobjects.examples.claims.dom.claim.ClaimItem;
import org.nakedobjects.examples.claims.dom.employee.Employee;


public class ClaimsFixture extends AbstractFixture {

    @Override
    public void install() {
        Employee fred = createCustomer("Fred Smith", null);
        Employee tom = createCustomer("Tom Brown", fred);
        createCustomer("Sam Jones", fred);

        Claim claim = createClaim(tom, -16, "Meeting with client");
        addItem(claim, -16, 38.50, "Lunch with client");
        addItem(claim, -16, 16.50, "Euston - Mayfair (return)");
        
        claim = createClaim(tom, -18, "Meeting in city office");
        addItem(claim, -16, 18.00, "Car parking");
        addItem(claim, -16, 26.50, "Reading - London (return)");

        claim = createClaim(fred, -14, "Meeting at clients");
        addItem(claim, -14, 18.00, "Car parking");
        addItem(claim, -14, 26.50, "Reading - London (return)");

    }
    
    private Employee createCustomer(String name, Employee approver) {
        Employee claimant;
        claimant = newTransientInstance(Employee.class);
        claimant.setName(name);
        claimant.setApprover(approver);
        persist(claimant);
        return claimant;
    }

    private Claim createClaim(Employee claimant, int days, String description) { 
        Claim claim = newTransientInstance(Claim.class);
        claim.setClaimant(claimant);
        claim.setDescription(description);
        Date date = new Date();
        date = date.add(0,0, days);
        claim.setDate(date);
        persist(claim);
        return claim;
    }

    private void addItem(Claim claim, int days, double amount, String description) { 
        ClaimItem claimItem = newTransientInstance(ClaimItem.class);
        Date date = new Date();
        date = date.add(0,0, days);
        claimItem.setDateIncurred(date);
        claimItem.setDescription(description);
        claimItem.setAmount(new Money(amount, "USD"));
        persist(claimItem);
        claim.addToItems(claimItem);
    }

}
