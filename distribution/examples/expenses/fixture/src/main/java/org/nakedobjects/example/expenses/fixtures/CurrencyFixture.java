package org.nakedobjects.example.expenses.fixtures;

import org.nakedobjects.applib.fixtures.AbstractFixture;
import org.nakedobjects.example.expenses.currency.Currency;


public class CurrencyFixture extends AbstractFixture {


    public static Currency EUR;
    public static Currency USD;
    public static Currency GBP;

    @Override
    public void install() {
        EUR = createCurrency("EUR", "Euro Member Countries", "Euro");
        GBP = createCurrency("GBP", "United Kingdom", "Pounds");
        USD = createCurrency("USD", "United States of America", "Dollars");
    }

    private Currency createCurrency(final String code, final String country, final String name) {
        final Currency currency = newTransientInstance(Currency.class);
        currency.setCurrencyCode(code);
        currency.setCurrencyCountry(country);
        currency.setCurrencyName(name);
        persist(currency);
        return currency;
    }

}
