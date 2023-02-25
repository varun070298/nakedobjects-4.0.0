package org.nakedobjects.applib.value;

import java.math.BigDecimal;

import org.nakedobjects.applib.annotation.Value;


@Value(semanticsProviderName = "org.nakedobjects.metamodel.value.MoneyValueSemanticsProvider")
public class Money extends Magnitude {

    private static final long serialVersionUID = 1L;
    private static final int[] cents = new int[] { 1, 10, 100, 100 };
    private final long amount;
    private final String currency;

    public Money(final double amount, final String currency) {
        assertCurrencySet(currency);
        this.currency = currency.toUpperCase();
        this.amount = Math.round(amount * centFactor());
    }

    public Money(final long amount, final String currency) {
        assertCurrencySet(currency);
        this.currency = currency.toUpperCase();
        this.amount = amount * centFactor();
    }

    private void assertCurrencySet(final String currency) {
        if (currency == null || currency.equals("")) {
            throw new IllegalArgumentException("Currency must be specified");
        }
        if (currency.length() != 3) {
            throw new IllegalArgumentException("Invalid currency code '" + currency + "'");
        }
    }

    /**
     * Add the specified money to this money.
     */
    public Money add(final Money money) {
        assertSameCurrency(money);
        return newMoney(amount + money.amount);
    }

    private void assertSameCurrency(final Money money) {
        if (!money.getCurrency().equals(getCurrency())) {
            throw new IllegalArgumentException("Not the same currency: " + getCurrency() + " & " + money.getCurrency());
        }
    }

    private int centFactor() {
        return cents[getFractionalDigits()];
    }

    /**
     * Returns this value as a double.
     */
    public double doubleValue() {
        return amount / (double) centFactor();
    }

    /**
     * Returns this value as a float.
     */
    public float floatValue() {
        return amount;
    }

    public BigDecimal getAmount() {
        return BigDecimal.valueOf(amount, getFractionalDigits());
    }

    public String getCurrency() {
        return currency;
    }

    private int getFractionalDigits() {
        return 2;
    }

    public boolean hasSameCurrency(final Money money) {
        return currency.equals(money.currency);
    }

    /**
     * Returns this value as an int.
     */
    public int intValue() {
        return (int) amount;
    }

    @Override
    public boolean isEqualTo(final Magnitude magnitude) {
        if (magnitude instanceof Money && hasSameCurrency((Money) magnitude)) {
            return ((Money) magnitude).amount == amount;
        } else {
            throw new IllegalArgumentException("Parameter must be of type Money and have the same currency");
        }
    }

    public boolean isGreaterThanZero() {
        return amount > 0;
    }

    @Override
    public boolean isLessThan(final Magnitude magnitude) {
        if (magnitude instanceof Money && hasSameCurrency((Money) magnitude)) {
            return amount < ((Money) magnitude).amount;
        } else {
            throw new IllegalArgumentException("Parameter must be of type Money and have the same currency");
        }
    }

    /**
     * Returns true if this value is less than zero.
     */
    public boolean isLessThanZero() {
        return amount < 0;
    }

    public boolean isZero() {
        return amount == 0;
    }

    /**
     * Returns this value as an long.
     */
    public long longValue() {
        return amount;
    }

    private Money newMoney(final long amount) {
        return new Money(amount / (centFactor() * 1.0), this.currency);
    }

    /**
     * Subtract the specified amount from this value.
     */
    public Money subtract(final Money money) {
        assertSameCurrency(money);
        return newMoney(amount - money.amount);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        return other.getClass() == this.getClass() && equals((Money) other);
    }

    public boolean equals(final Money other) {
        return other.currency.equals(currency) && other.amount == amount;
    }

    @Override
    public int hashCode() {
        return (int) amount;
    }

    @Override
    public String toString() {
        return amount / (centFactor() * 1.0) + " " + currency;
    }
}
// Copyright (c) Naked Objects Group Ltd.
