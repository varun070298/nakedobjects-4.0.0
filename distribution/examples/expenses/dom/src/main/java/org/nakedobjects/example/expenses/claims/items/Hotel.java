package org.nakedobjects.example.expenses.claims.items;

import org.nakedobjects.applib.annotation.Disabled;
import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.applib.annotation.MemberOrder;
import org.nakedobjects.applib.annotation.Optional;
import org.nakedobjects.applib.value.Money;
import org.nakedobjects.example.expenses.claims.ExpenseItem;


public class Hotel extends AbstractExpenseItem {

    // {{ Hotel URL
    private String hotelURL;

    @MemberOrder(sequence = "2.1")
    @Optional
    public String getHotelURL() {
        return hotelURL;
    }

    public void setHotelURL(final String hotelURL) {
        this.hotelURL = hotelURL;
    }

    public void modifyHotelURL(final String newHotelURL) {
        setHotelURL(newHotelURL);
        checkIfCompleteAndRecalculateClaimTotalIfPersistent();
    }

    public String disableHotelURL() {
        return disabledIfLocked();
    }

    // }}

    // {{ Number of Nights
    private int numberOfNights;

    public void setNumberOfNights(final int numberOfNights) {
        this.numberOfNights = numberOfNights;
    }

    @MemberOrder(sequence = "2.2")
    public int getNumberOfNights() {
        return numberOfNights;
    }

    public void modifyNumberOfNights(final int newNumberOfNights) {
        setNumberOfNights(newNumberOfNights);
        checkIfCompleteAndRecalculateClaimTotalIfPersistent();
    }

    public String disableNumberOfNights() {
        return disabledIfLocked();
    }

    // }}

    // {{ Accommodation
    private Money accommodation;

    @MemberOrder(sequence = "2.3")
    public Money getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(final Money room) {
        this.accommodation = room;
    }

    public void modifyAccommodation(final Money room) {
        setAccommodation(room);
        checkIfComplete();
        recalculateAmount();
    }

    public String disableAccommodation() {
        return disabledIfLocked();
    }

    public String validateAccommodation(final Money newAmount) {
        return validateAnyAmountField(newAmount);
    }

    // }}

    // {{ Food
    private Money food;

    public void setFood(final Money meals) {
        this.food = meals;
    }

    @MemberOrder(sequence = "2.4")
    @Optional
    public Money getFood() {
        return food;
    }

    public void modifyFood(final Money newMeals) {
        setFood(newMeals);
        checkIfComplete();
        recalculateAmount();
    }

    public String disableFood() {
        return disabledIfLocked();
    }

    public String validateFood(final Money newAmount) {
        return validateAnyAmountField(newAmount);
    }

    // }}

    // {{ Other
    private Money other;

    public void setOther(final Money other) {
        this.other = other;
    }

    @MemberOrder(sequence = "2.5")
    @Optional
    public Money getOther() {
        return other;
    }

    public void modifyOther(final Money newOther) {
        setOther(newOther);
        checkIfComplete();
        recalculateAmount();
    }

    public String disableOther() {
        return disabledIfLocked();
    }

    public String validateOther(final Money newAmount) {
        return validateAnyAmountField(newAmount);
    }

    // }}

    // {{ Amount
    @Override
    @Disabled
    public Money getAmount() {
        return super.getAmount();
    }

    @Override
    @Hidden
    public void initialiseAmount() {
        final Money zero = new Money(0.0, getClaim().currencyCode());
        setAccommodation(zero);
        setFood(zero);
        setOther(zero);
        setAmount(zero);
    }

    private void recalculateAmount() {
        Money newAmount = new Money(0, getClaim().currencyCode());
        for (int i = 0; i < numberOfNights; i++) {
            newAmount = addIfNotNull(accommodation, newAmount);
        }
        newAmount = addIfNotNull(food, newAmount);
        newAmount = addIfNotNull(other, newAmount);
        modifyAmount(newAmount);
    }

    private Money addIfNotNull(final Money amountToAdd, final Money sum) {
        if (amountToAdd != null) {
            return sum.add(amountToAdd);
        }
        return sum;
    }

    // }}

    // {{ Copying
    @Override
    protected void copyAllSameClassFields(final ExpenseItem otherItem) {
        super.copyAllSameClassFields(otherItem);
        if (otherItem instanceof Hotel) {

        }
    }

    @Override
    protected void copyAnyEmptyFieldsSpecificToSubclassOfAbstractExpenseItem(final ExpenseItem otherItem) {
        if (otherItem instanceof Hotel) {
            final Hotel otherHotel = (Hotel) otherItem;
            if (hotelURL == null || hotelURL.length() == 0) {
                modifyHotelURL(otherHotel.getHotelURL());
            }
            if (numberOfNights == 0) {
                modifyNumberOfNights(otherHotel.getNumberOfNights());
            }
            if (accommodation == null) {
                modifyAccommodation(otherHotel.getAccommodation());
            }
            if (food == null) {
                modifyFood(otherHotel.getFood());
            }
            if (other == null) {
                modifyOther(otherHotel.getOther());
            }
        }
    }

    // }}

    @Override
    protected boolean mandatorySubClassFieldsComplete() {
        return hotelURL != null && !hotelURL.equals("") && accommodation.isGreaterThanZero();
    }

}
