package org.nakedobjects.example.expenses.claims.items;

import org.nakedobjects.applib.annotation.MemberOrder;
import org.nakedobjects.applib.annotation.Named;
import org.nakedobjects.example.expenses.claims.ExpenseItem;


public class Airfare extends Journey {

    // {{ Airline
    private String airlineAndFlight;

    public void setAirlineAndFlight(final String airline) {
        this.airlineAndFlight = airline;
    }

    @MemberOrder(sequence = "2.4")
    @Named("Airline & Flight No.")
    public String getAirlineAndFlight() {
        return airlineAndFlight;
    }

    public void modifyAirlineAndFlight(final String newAirline) {
        setAirlineAndFlight(newAirline);
        checkIfComplete();
    }

    public void clearAirlineAndFlight() {
        setAirlineAndFlight(null);
        checkIfComplete();
    }

    public String disableAirlineAndFlight() {
        return disabledIfLocked();
    }

    // }}

    // {{ Copying
    @Override
    protected void copyAnyEmptyFieldsSpecificToSubclassOfJourney(final ExpenseItem otherItem) {
        if (otherItem instanceof Airfare) {
            final Airfare airfare = (Airfare) otherItem;
            if (airlineAndFlight == null || airlineAndFlight.length() == 0) {
                modifyAirlineAndFlight(airfare.getAirlineAndFlight());
            }
        }
    }

    // }}

    @Override
    protected boolean mandatoryJourneySubClassFieldsComplete() {
        return airlineAndFlight != null & !airlineAndFlight.equals("");
    }

}
