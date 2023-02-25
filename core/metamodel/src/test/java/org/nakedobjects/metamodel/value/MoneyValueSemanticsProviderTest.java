package org.nakedobjects.metamodel.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Locale;

import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.applib.value.Money;
import org.nakedobjects.metamodel.adapter.TextEntryParseException;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetHolderImpl;

@RunWith(JMock.class)
public class MoneyValueSemanticsProviderTest extends ValueSemanticsProviderAbstractTestCase {

    private static final String POUND_SYMBOL = "\u00A3";
    private static final String EURO_SYMBOL = "\u20AC";
    private MoneyValueSemanticsProvider adapter;
    private Object originalMoney;
    private FacetHolder holder;

    @Before
    public void setUpObjects() throws Exception {
        Locale.setDefault(Locale.UK);
        setupSpecification(Money.class);
      //  originalMoney = new Money(10.5, "gbp");
        holder = new FacetHolderImpl();
        setValue(adapter = new MoneyValueSemanticsProvider(holder, mockConfiguration, mockSpecificationLoader, mockRuntimeContext));
    }

    private Object createMoney(final double amount, final String currency) {
        return new Money(amount, currency);
    }

    @Test
    public void testLocale() {
        assertEquals(Locale.UK, Locale.getDefault());
    }

    @Test
    public void testEncoding() {
        originalMoney = new Money(10.5, "gbp");
        final String data = adapter.toEncodedString(originalMoney);
        assertEquals("10.5 GBP", new String(data));
    }

    @Test
    public void testDecoding() {
        final Object restored = adapter.fromEncodedString("23.77 FFR");
        final Money expected = new Money(23.77, "FFR");
        assertEquals(expected, restored);
    }

    @Test
    public void testTitleOfWithPounds() {
        originalMoney = new Money(10.5, "gbp");
        assertEquals(POUND_SYMBOL + "10.50", adapter.displayTitleOf(originalMoney));
    }

    @Test
    public void testTitleOfWithNonLocalCurrency() {
        assertEquals("10.50 USD", adapter.displayTitleOf(createMoney(10.50, "usd")));
    }

    @Test
    public void testTitleWithUnknownCurrency() {
        assertEquals("10.50 UNK", adapter.displayTitleOf(createMoney(10.50, "UNK")));
    }

    @Test
    public void testUserEntryWithCurrency() {
        final Object money = createMoney(10.5, "gbp");
        final Object parsed = adapter.parseTextEntry(money, "22.45 USD");
        assertEquals(new Money(22.45, "usd"), parsed);
    }

    @Test
    public void testNewUserEntryUsesPreviousCurrency() {
        originalMoney = new Money(10.5, "gbp");
        final Object parsed = adapter.parseTextEntry(originalMoney, "22.45");
        assertEquals(new Money(22.45, "gbp"), parsed);
    }

    @Test
    public void testReplacementEntryForDefaultCurrency() {
        // MoneyValueSemanticsProvider adapter = new MoneyValueSemanticsProvider(new Money(10.3, "gbp"));
        final Object parsed = adapter.parseTextEntry(originalMoney, POUND_SYMBOL + "80.90");
        assertEquals(new Money(80.90, "gbp"), parsed);
    }

    @Test
    public void testSpecifyingCurrencyInEntry() {
        final Object parsed = adapter.parseTextEntry(originalMoney, "3021.50 EUr");
        assertEquals("3,021.50 EUR", adapter.displayTitleOf(parsed));
    }

    @Test
    public void testUsingLocalCurrencySymbol() {
        // MoneyValueSemanticsProvider adapter = new MoneyValueSemanticsProvider(new Money(0L, "gbp"));
        final Object parsed = adapter.parseTextEntry(originalMoney, POUND_SYMBOL + "3021.50");
        assertEquals(POUND_SYMBOL + "3,021.50", adapter.titleString(parsed));
    }

    @Test
    public void testInvalidCurrencyCodeIsRejected() throws Exception {
        try {
            adapter.parseTextEntry(originalMoney, "3021.50  XYZ");
            fail("invalid code accepted " + adapter);
        } catch (final TextEntryParseException expected) {}
    }

    @Test
    public void testInvalidCurrencySymbolIsRejected() throws Exception {
        try {
            adapter.parseTextEntry(originalMoney, EURO_SYMBOL + "3021.50");
            fail("invalid code accepted " + adapter);
        } catch (final TextEntryParseException expected) {}
    }
   
    @Test
    public void testNewValueDefaultsToLocalCurrency() throws Exception {
        final Object parsed = adapter.parseTextEntry(originalMoney, "3021.50");
        assertEquals(POUND_SYMBOL + "3,021.50", adapter.displayTitleOf(parsed));
    }

    @Test
    public void testUnrelatedCurrencySymbolIsRejected() throws Exception {
        final Object money = createMoney(1, "eur");
        try {
            adapter.parseTextEntry(money, "$3021.50");
            fail("invalid code accepted " + adapter);
        } catch (final TextEntryParseException expected) {}
    }

}

// Copyright (c) Naked Objects Group Ltd.
