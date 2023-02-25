package org.nakedobjects.example.expenses.fixtures;

import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.applib.fixtures.AbstractFixture;
import org.nakedobjects.example.expenses.claims.ExpenseType;
import org.nakedobjects.example.expenses.claims.items.Airfare;
import org.nakedobjects.example.expenses.claims.items.CarRental;
import org.nakedobjects.example.expenses.claims.items.GeneralExpense;
import org.nakedobjects.example.expenses.claims.items.Hotel;
import org.nakedobjects.example.expenses.claims.items.PrivateCarJourney;
import org.nakedobjects.example.expenses.claims.items.Taxi;


public class ExpenseTypeFixture extends AbstractFixture {

    public static ExpenseType GENERAL;
    public static ExpenseType AIRFARE;
    public static ExpenseType CAR_RENTAL;
    public static ExpenseType HOTEL;
    public static ExpenseType MEAL;
    public static ExpenseType MOBILE_PHONE;
    public static ExpenseType PRIVATE_CAR;
    public static ExpenseType TAXI;

    @Override
    public void install() {
        GENERAL = createType(GeneralExpense.class, "General Expense");
        AIRFARE = createType(Airfare.class, "Airfare");
        CAR_RENTAL = createType(CarRental.class, "Car Rental");
        HOTEL = createType(Hotel.class, "Hotel");
        MEAL = createType(GeneralExpense.class, "Meal");
        MOBILE_PHONE = createType(GeneralExpense.class, "Mobile Phone");
        PRIVATE_CAR = createType(PrivateCarJourney.class, "Private Car Journey");
        TAXI = createType(Taxi.class, "Taxi");
    }

    @Hidden
    public ExpenseType createType(final Class<?> correspondingClass, final String titleString) {
        final ExpenseType type = newTransientInstance(ExpenseType.class);
        type.setCorrespondingClassName(correspondingClass.getName());
        type.setTitleString(titleString);
        persist(type);
        return type;
    }
}
