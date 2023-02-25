package org.nakedobjects.metamodel.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Locale;

import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.applib.value.Money;
import org.nakedobjects.metamodel.adapter.TextEntryParseException;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetHolderImpl;


@Ignore // TODO once the sematics provide has a way to reset the formatters for the new local then this test can be reinstated. 
@RunWith(JMock.class)
public class PolishMoneyValueSemanticsProviderTest extends ValueSemanticsProviderAbstractTestCase {

    private static final String CURRENCY_SPACE = "\u00a0";
    private static final String ZLOTYCH_SYMBOL = "\u007a\u0142";
    private static final String EURO_SYMBOL = "\u20AC";
    private MoneyValueSemanticsProvider adapter;
    private Object originalMoney;
    private FacetHolder holder;

    @Before
    public void setUpObjects() throws Exception {
        Locale.setDefault(new Locale("pl", "PL"));
        setupSpecification(Money.class);
        originalMoney = new Money(10.50, "pln");
        holder = new FacetHolderImpl();
        setValue(adapter = new MoneyValueSemanticsProvider(holder, mockConfiguration, mockSpecificationLoader, mockRuntimeContext));
    }

    private Object createMoney(final double amount, final String currency) {
        return new Money(amount, currency);
    }

    @Test
    public void testLocale() {
        assertEquals("PL", Locale.getDefault().getCountry());
        assertEquals("pl", Locale.getDefault().getLanguage());
    }

    @Test
    public void testEncoding() {
        final String data = adapter.toEncodedString(originalMoney);
        assertEquals("10.5 PLN", new String(data));
    }

    @Test
    public void testDecoding() {
        final Object restored = adapter.fromEncodedString("23.77 FFR");
        final Money expected = new Money(23.77, "FFR");
        assertEquals(expected, restored);
    }

    @Test
    public void testTitleOfWithZlotych() {
        assertEquals("10,5 " + ZLOTYCH_SYMBOL, adapter.displayTitleOf(originalMoney));
    }

    @Test
    public void testTitleOfWithNonLocalCurrency() {
        assertEquals("10,5 USD", adapter.displayTitleOf(createMoney(10.50, "usd")));
    }

    @Test
    public void testTitleWithUnknownCurrency() {
        assertEquals("10,5 UNK", adapter.displayTitleOf(createMoney(10.50, "UNK")));
    }

    @Test
    public void testUserEntryWithCurrency() {
        final Object money = createMoney(10.5, "gbp");
        final Object parsed = adapter.parseTextEntry(money, "22,45 USD");
        assertEquals(new Money(22.45, "usd"), parsed);
    }

    @Test
    public void testUserEntryUsesPreviousCurrency() {
        final Object parsed = adapter.parseTextEntry(originalMoney, "22,45");
        assertEquals(new Money(22.45, "pln"), parsed);
    }

    @Test
    public void testReplacementEntryForDefaultCurrency() {
        final Object parsed = adapter.parseTextEntry(originalMoney, "80,90 " + ZLOTYCH_SYMBOL);
        assertEquals(new Money(80.90, "pln"), parsed);
    }

    @Test
    public void testSpecifyingCurrencyInEntry() {
        final Object parsed = adapter.parseTextEntry(originalMoney, "3021,50 cad");
        assertEquals("3" + CURRENCY_SPACE + "021,5 CAD", adapter.displayTitleOf(parsed));
    }

    @Test
    public void testUsingLocalCurrencySymbol() {
        // MoneyValueSemanticsProvider adapter = new MoneyValueSemanticsProvider(new Money(0L, "gbp"));
        final Object parsed = adapter.parseTextEntry(originalMoney, "3021,50 " + ZLOTYCH_SYMBOL);
        assertEquals("3" + CURRENCY_SPACE + "021,5 " + ZLOTYCH_SYMBOL, adapter.titleString(parsed));
    }

    @Test
    public void testInvalidCurrencySuffixRejected() throws Exception {
        final Object parsed = adapter.parseTextEntry(originalMoney, "3" + CURRENCY_SPACE + "021,50  Dm");
        assertEquals("3" + CURRENCY_SPACE + "021,5 " + ZLOTYCH_SYMBOL, adapter.titleString(parsed));
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
        final Object parsed = adapter.parseTextEntry(originalMoney, "3021,50");
        assertEquals("3" + CURRENCY_SPACE + "021,5 " + ZLOTYCH_SYMBOL, adapter.displayTitleOf(parsed));
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
