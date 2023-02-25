package org.nakedobjects.example.expenses.fixtures;

import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.applib.fixtures.AbstractFixture;
import org.nakedobjects.example.expenses.claims.ClaimRepository;
import org.nakedobjects.example.expenses.currency.Currency;
import org.nakedobjects.example.expenses.employee.Employee;
import org.nakedobjects.example.expenses.employee.EmployeeRepository;


public class EmployeeFixture extends AbstractFixture {

    public static Employee SVEN;
    public static Employee DICK;
    public static Employee BOB;
    public static Employee JOE;

    @Override
    public void install() {

        createEmployees();
    }

    private void createEmployees() {
        SVEN = createEmployee("Sven Bloggs", "sven", "sven@example.com", CurrencyFixture.GBP);
        DICK = createEmployee("Dick Barton", "dick", "dick@example.com", CurrencyFixture.GBP);
        BOB = createEmployee("Robert Bruce", "bob", "bob@example.com", CurrencyFixture.USD);
        JOE = createEmployee("Joe Sixpack", "joe", "joe@example.com", CurrencyFixture.USD);
        createEmployee("Intrepid Explorer", "exploration", "exploration@example.com", CurrencyFixture.USD);

        SVEN.setNormalApprover(DICK);
        DICK.setNormalApprover(BOB);
    }

    @Hidden
    public Employee createEmployee(final String myName, final String userName, final String emailAddress, final Currency currency) {
        final Employee employee = newTransientInstance(Employee.class);
        assert (myName != null && myName != "");
        assert (userName != null && userName != "");
        assert (emailAddress != null && emailAddress != "");
        assert (currency != null);

        employee.setName(myName);
        employee.setUserName(userName);
        employee.setEmailAddress(emailAddress);
        employee.setCurrency(currency);
        persist(employee);
        return employee;
    }

    
    // {{ Injected Services
    /*
     * This region contains references to the services (Repositories, Factories or other Services) used by
     * this domain object. The references are injected by the application container.
     */

    // {{ Injected: ClaimantRepository
    private EmployeeRepository employeeRepository;

    /**
     * This field is not persisted, nor displayed to the user.
     */
    protected EmployeeRepository getClaimantRepository() {
        return this.employeeRepository;
    }

    /**
     * Injected by the application container.
     */
    public void setClaimantRepository(final EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // }}

    // {{ Injected: ClaimRepository
    private ClaimRepository claimRepository;

    /**
     * This field is not persisted, nor displayed to the user.
     */
    protected ClaimRepository getClaimRepository() {
        return this.claimRepository;
    }

    /**
     * Injected by the application container.
     */
    public void setClaimRepository(final ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
    }

    // }}

    // }}


}
