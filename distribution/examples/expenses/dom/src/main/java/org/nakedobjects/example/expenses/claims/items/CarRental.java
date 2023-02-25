package org.nakedobjects.example.expenses.claims.items;

import org.nakedobjects.applib.annotation.MemberOrder;
import org.nakedobjects.example.expenses.claims.ExpenseItem;


public class CarRental extends AbstractExpenseItem {

    // {{ Rental Company
    private String rentalCompany;

    public void setRentalCompany(final String rentalCompany) {
        this.rentalCompany = rentalCompany;
    }

    @MemberOrder(sequence = "2.1")
    public String getRentalCompany() {
        return rentalCompany;
    }

    public void modifyRentalCompany(final String newRentalCompany) {
        setRentalCompany(newRentalCompany);
        checkIfComplete();
    }

    public void clearRentalCompany() {
        setRentalCompany(null);
        checkIfComplete();
    }

    public String disableRentalCompany() {
        return disabledIfLocked();
    }
    // }}

    // {{ Number of Days
    private int numberOfDays = 0;

    public void setNumberOfDays(final int noOfDays) {
        this.numberOfDays = noOfDays;
    }

    @MemberOrder(sequence = "2.2")
    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void modifyNumberOfDays(final int noOfDays) {
        setNumberOfDays(noOfDays);
        checkIfComplete();
    }

    public void clearNumberOfDays() {
        setNumberOfDays(0);
        checkIfComplete();
    }

    public String disableNumberOfDays() {
        return disabledIfLocked();
    }

    // }}

    // {{ Copying
    @Override
    protected void copyAnyEmptyFieldsSpecificToSubclassOfAbstractExpenseItem(final ExpenseItem otherItem) {
        if (otherItem instanceof CarRental) {
            final CarRental carRental = (CarRental) otherItem;

            if (rentalCompany == null || rentalCompany.length() == 0) {
                modifyRentalCompany(carRental.getRentalCompany());
            }
            if (numberOfDays == 0) {
                modifyNumberOfDays(carRental.getNumberOfDays());
            }
        }
    }

    // }}

    @Override
    protected boolean mandatorySubClassFieldsComplete() {
        return numberOfDays > 0 && rentalCompany != null & !rentalCompany.equals("");
    }

}
