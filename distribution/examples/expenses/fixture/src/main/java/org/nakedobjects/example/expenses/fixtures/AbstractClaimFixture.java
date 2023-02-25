package org.nakedobjects.example.expenses.fixtures;

import org.nakedobjects.applib.fixtures.AbstractFixture;
import org.nakedobjects.applib.value.Date;
import org.nakedobjects.applib.value.Money;
import org.nakedobjects.example.expenses.claims.AbstractClaim;
import org.nakedobjects.example.expenses.claims.Claim;
import org.nakedobjects.example.expenses.claims.ClaimFactory;
import org.nakedobjects.example.expenses.claims.ClaimRepository;
import org.nakedobjects.example.expenses.claims.ExpenseItem;
import org.nakedobjects.example.expenses.claims.ExpenseType;
import org.nakedobjects.example.expenses.claims.ProjectCode;
import org.nakedobjects.example.expenses.claims.items.AbstractExpenseItem;
import org.nakedobjects.example.expenses.claims.items.Airfare;
import org.nakedobjects.example.expenses.claims.items.CarRental;
import org.nakedobjects.example.expenses.claims.items.GeneralExpense;
import org.nakedobjects.example.expenses.claims.items.Hotel;
import org.nakedobjects.example.expenses.claims.items.Journey;
import org.nakedobjects.example.expenses.claims.items.PrivateCarJourney;
import org.nakedobjects.example.expenses.claims.items.Taxi;
import org.nakedobjects.example.expenses.employee.Employee;
import org.nakedobjects.example.expenses.employee.EmployeeRepository;


public abstract class AbstractClaimFixture extends AbstractFixture {

    // {{ Injected Services

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

    // {{ Injected: ClaimFactory
    private ClaimFactory claimFactory;

    /**
     * This property is not persisted, nor displayed to the user.
     */
    protected ClaimFactory getClaimFactory() {
        return this.claimFactory;
    }

    /**
     * Injected by the application container.
     */
    public void setClaimFactory(final ClaimFactory claimFactory) {
        this.claimFactory = claimFactory;
    }
    // }}

    // {{ Injected: EmployeeRepository
    private EmployeeRepository employeeRepository;

    /**
     * This field is not persisted, nor displayed to the user.
     */
    protected EmployeeRepository getEmployeeRepository() {
        return this.employeeRepository;
    }

    /**
     * Injected by the application container.
     */
    public void setEmployeeRepository(final EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // }}
    // }}

    protected Claim createNewClaim(
            final Employee employee,
            final Employee approver,
            final String description,
            final ProjectCode projectCode,
            final Date dateCreated) {
        final Claim claim = getClaimFactory().createNewClaim(employee, description);
        claim.modifyApprover(approver);
        claim.modifyProjectCode(projectCode);
        if (dateCreated != null) {
            claim.setDateCreated(dateCreated);
        }
        return claim;
    }

    private ExpenseItem createExpenseItem(
            final AbstractClaim claim,
            final ExpenseType type,
            final Date dateIncurred,
            final String description,
            final double amount) {
        final AbstractExpenseItem item = (AbstractExpenseItem) claim.createNewExpenseItem(type);
        item.modifyDateIncurred(dateIncurred);
        item.modifyDescription(description);
        item.modifyAmount(money(amount, claim));
        return item;
    }

    private void modifyStandardJourneyFields(
            final Journey journey,
            final String origin,
            final String destination,
            final boolean returnJourney) {
        journey.modifyOrigin(origin);
        journey.modifyDestination(destination);
        journey.modifyReturnJourney(new Boolean(returnJourney));
    }

    protected GeneralExpense addGeneralExpense(
            final AbstractClaim claim,
            final Date dateIncurred,
            final String description,
            final double amount) {
        final GeneralExpense item = (GeneralExpense) createExpenseItem(claim, ExpenseTypeFixture.GENERAL, dateIncurred,
                description, amount);
        persist(item);
        return item;
    }

    protected Airfare addAirfare(
            final AbstractClaim claim,
            final Date dateIncurred,
            final String description,
            final double amount,
            final String airline,
            final String origin,
            final String destination,
            final boolean returnJourney) {
        final Airfare item = (Airfare) createExpenseItem(claim, ExpenseTypeFixture.AIRFARE, dateIncurred, description, amount);
        item.modifyAirlineAndFlight(airline);
        modifyStandardJourneyFields(item, origin, destination, returnJourney);
        persist(item);
        return item;
    }

    protected Hotel addHotel(
            final AbstractClaim claim,
            final Date dateIncurred,
            final String description,
            final double amount,
            final String hotelURL,
            final int numberOfNights,
            final double accommodation,
            final double food,
            final double other) {
        final Hotel item = (Hotel) createExpenseItem(claim, ExpenseTypeFixture.HOTEL, dateIncurred, description, amount);
        item.modifyHotelURL(hotelURL);
        item.modifyNumberOfNights(numberOfNights);
        item.modifyAccommodation(money(accommodation, claim));
        item.modifyFood(money(food, claim));
        item.modifyOther(money(other, claim));
        persist(item);
        return item;
    }

    protected CarRental addCarRental(
            final AbstractClaim claim,
            final Date dateIncurred,
            final String description,
            final double amount,
            final String rentalCompany,
            final int noOfDays) {
        final CarRental item = (CarRental) createExpenseItem(claim, ExpenseTypeFixture.CAR_RENTAL, dateIncurred, description,
                amount);
        item.modifyRentalCompany(rentalCompany);
        item.modifyNumberOfDays(noOfDays);
        persist(item);
        return item;
    }

    protected GeneralExpense addMobilePhone(
            final AbstractClaim claim,
            final Date dateIncurred,
            final String phoneNumber,
            final double amount) {
        final GeneralExpense item = (GeneralExpense) createExpenseItem(claim, ExpenseTypeFixture.MOBILE_PHONE, dateIncurred,
                "Phone No. " + phoneNumber, amount);
        persist(item);
        return item;
    }

    protected PrivateCarJourney addPrivateCarJourney(
            final AbstractClaim claim,
            final Date dateIncurred,
            final String description,
            final String origin,
            final String destination,
            final boolean returnJourney,
            final int totalMiles) {
        final PrivateCarJourney item = (PrivateCarJourney) createExpenseItem(claim, ExpenseTypeFixture.PRIVATE_CAR, dateIncurred,
                description, 0.0);
        modifyStandardJourneyFields(item, origin, destination, returnJourney);
        item.modifyTotalMiles(totalMiles);
        item.modifyMileageRate(0.40);
        persist(item);
        return item;
    }

    protected Taxi addTaxi(
            final AbstractClaim claim,
            final Date dateIncurred,
            final String description,
            final double amount,
            final String origin,
            final String destination,
            final boolean returnJourney) {
        final Taxi item = (Taxi) createExpenseItem(claim, ExpenseTypeFixture.TAXI, dateIncurred, description, amount);
        modifyStandardJourneyFields(item, origin, destination, returnJourney);
        persist(item);
        return item;
    }

    protected GeneralExpense addMeal(
            final AbstractClaim claim,
            final Date dateIncurred,
            final String description,
            final double amount) {
        final GeneralExpense item = (GeneralExpense) createExpenseItem(claim, ExpenseTypeFixture.MEAL, dateIncurred, description,
                amount);

        persist(item);
        return item;
    }

    private Money money(final double amount, final AbstractClaim claim) {
        return new Money(amount, claim.currencyCode());
    }
}
