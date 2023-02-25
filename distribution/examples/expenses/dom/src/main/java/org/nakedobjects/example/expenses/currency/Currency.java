package org.nakedobjects.example.expenses.currency;

import org.nakedobjects.applib.AbstractDomainObject;
import org.nakedobjects.applib.annotation.Bounded;
import org.nakedobjects.applib.annotation.Immutable;
import org.nakedobjects.applib.annotation.When;


@Bounded
@Immutable(When.ONCE_PERSISTED)
public class Currency extends AbstractDomainObject {

    private String currencyCode;
    private String currencyName;
    private String currencyCountry;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(final String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyCountry() {
        return currencyCountry;
    }

    public void setCurrencyCountry(final String currencyCountry) {
        this.currencyCountry = currencyCountry;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(final String currencyName) {
        this.currencyName = currencyName;
    }

    @Override
    public String toString() {
        return currencyCode;
    }

}
