package org.nakedobjects.example.expenses.claims.items;

import org.nakedobjects.applib.annotation.Disabled;
import org.nakedobjects.applib.annotation.MemberOrder;
import org.nakedobjects.applib.value.Money;
import org.nakedobjects.example.expenses.claims.ExpenseItem;


public class PrivateCarJourney extends Journey {

    // {{ Total Miles field
    private int totalMiles;

    public void setTotalMiles(final int miles) {
        this.totalMiles = miles;
    }

    @MemberOrder(sequence = "2.4")
    public int getTotalMiles() {
        return totalMiles;
    }

    public void modifyTotalMiles(final int newMiles) {
        setTotalMiles(newMiles);
        checkIfComplete();
        recalculateAmount();
    }

    public String disableTotalMiles() {
        return disabledIfLocked();
    }

    // }}

    // {{ MileageRate field;
    private double mileageRate;

    public void setMileageRate(final double rate) {
        this.mileageRate = rate;
    }

    @MemberOrder(sequence = "2.5")
    public double getMileageRate() {
        return mileageRate;
    }

    public void modifyMileageRate(final double newRate) {
        setMileageRate(newRate);
        checkIfComplete();
        recalculateAmount();
    }

    public String disableMileageRate() {
        return disabledIfLocked();
    }

    // }}

    @Disabled
    @Override
    public Money getAmount() {
        return super.getAmount();
    }

    private void recalculateAmount() {
        modifyAmount(new Money(totalMiles * mileageRate, getClaim().currencyCode()));
    }

    // {{ Copying
    @Override
    protected void copyAnyEmptyFieldsSpecificToSubclassOfJourney(final ExpenseItem otherItem) {
        if (otherItem instanceof PrivateCarJourney) {
            final PrivateCarJourney carJourney = (PrivateCarJourney) otherItem;
            if (totalMiles == 0) {
                modifyTotalMiles(carJourney.getTotalMiles());
            }
            if (mileageRate == 0) {
                modifyMileageRate(carJourney.getMileageRate());
            }
        }
    }

    // }}

    @Override
    protected boolean mandatoryJourneySubClassFieldsComplete() {
        return totalMiles > 0 && mileageRate > 0;
    }
}
