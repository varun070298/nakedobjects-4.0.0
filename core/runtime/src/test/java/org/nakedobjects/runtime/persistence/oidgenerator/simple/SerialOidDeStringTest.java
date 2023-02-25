package org.nakedobjects.runtime.persistence.oidgenerator.simple;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.nakedobjects.metamodel.adapter.oid.Oid;

public class SerialOidDeStringTest {

    
    @Test
    public void whenValidTransientWithPullsOutTransient() {
    	SerialOid oid = SerialOid.deString("TOID:1");
    	assertThat(oid.isTransient(), is(true));
    }

    @Test
    public void whenValidWithNoPreviousThenNoPrevious() {
    	SerialOid oid = SerialOid.deString("TOID:1");
    	assertThat(oid.getPrevious(), is(nullValue()));
    }

    @Test
    public void whenValidThenPullsOutSerialNumber() {
    	SerialOid oid = SerialOid.deString("TOID:1");
    	assertThat(oid.getSerialNo(), is(1L));
    }

    @Test
    public void whenValidTransientWithHexDigitsThenSerialNumberConverted() {
    	SerialOid oid = SerialOid.deString("TOID:2F");
    	assertThat(oid.getSerialNo(), is(2*16+15L));
    }

    @Test
    public void whenValidTransientWithNonHexDigitsThenDigitsAreNeverthelessParsedAsHex() {
    	SerialOid oid = SerialOid.deString("TOID:10");
    	assertThat(oid.getSerialNo(), is(16L));
    }

    @Test
    public void whenValidTransientWithBigHexDigitsThenConverted() {
    	SerialOid oid = SerialOid.deString("TOID:12ABF3");
    	assertThat(oid.getSerialNo(), is(1223667L));
    }

    @Test
    public void whenValidPersistentThenPullsOutPersistent() {
    	SerialOid oid = SerialOid.deString("OID:1");
    	assertThat(oid.isTransient(), is(false));
    	assertThat(oid.getSerialNo(), is(1L));
    }

    @Test
    public void whenValidPersistentWithBigHexDigitsThenPullsOutSerialNumber() {
    	SerialOid oid = SerialOid.deString("OID:12ABF3");
    	assertThat(oid.isTransient(), is(false));
    	assertThat(oid.getSerialNo(), is(1223667L));
    }

    @Test
    public void whenValidPersistentWithNoPreviousThenNoPrevious() {
    	SerialOid oid = SerialOid.deString("OID:12ABF3");
    	assertThat(oid.hasPrevious(), is(false));
    }

    @Test
    public void whenValidPersistentWithTransientPreviousThenPullsOutPrevious() {
    	SerialOid oid = SerialOid.deString("OID:12ABF3~TOID:12");
    	assertThat(oid.hasPrevious(), is(true));
    }

    @Test
    public void whenValidPersistentWithTransientPreviousThenPreviousDoesNotItselfHaveAPrevious() {
    	SerialOid oid = SerialOid.deString("OID:12ABF3~TOID:12");
    	Oid previousOid = oid.getPrevious();
    	assertThat(previousOid.hasPrevious(), is(false));
    }

    @Test
    public void whenValidPersistentWithTransientPreviousThenPreviousIsTransient() {
    	SerialOid oid = SerialOid.deString("OID:12ABF3~TOID:12");
    	Oid previousOid = oid.getPrevious();
    	assertThat(previousOid.isTransient(), is(true));
    }

    @Test
    public void whenValidPersistentWithPersistentPreviousThenPreviousIsPersistent() {
    	SerialOid oid = SerialOid.deString("TOID:12ABF3~OID:12");
    	Oid previousOid = oid.getPrevious();
    	assertThat(previousOid.isTransient(), is(false));
    }

    @Test
    public void whenValidPersistentWithPersistentPreviousThenPreviousSerialNumber() {
    	SerialOid oid = SerialOid.deString("OID:12ABF3~TOID:12");
    	SerialOid previousOid = (SerialOid) oid.getPrevious();
    	assertThat(previousOid.getSerialNo(), is(18L));
    }

    @Test(expected=IllegalArgumentException.class)
    public void whenInvalidTransientModifierThrowsException() {
    	SerialOid.deString("QOID:12ABF3");
    }

    @Test(expected=IllegalArgumentException.class)
    public void whenInvalidOidLiteralModifierThrowsException() {
    	SerialOid.deString("TOiD:12ABF3");
    }

    @Test(expected=IllegalArgumentException.class)
    public void whenInvalidHexDigitsThrowsException() {
    	SerialOid.deString("TOID:1G");
    }

    @Test(expected=IllegalArgumentException.class)
    public void whenInvalidPreviousConcatenationCharacterThrowsException() {
    	SerialOid.deString("OID:1F-OID#1A");
    }

    @Test
    public void shouldRoundtripOk() {
    	SerialOid oid = SerialOid.createTransient(Long.MIN_VALUE+1);
    	String enString = oid.enString();
    	assertThat(enString, is("TOID:-7FFFFFFFFFFFFFFF"));
    	
    	SerialOid oid2 = SerialOid.deString(enString);
    	assertThat(oid2.getSerialNo(), is(oid.getSerialNo()));
    	assertThat(oid2.isTransient(), is(oid.isTransient()));
    }
    
}
// Copyright (c) Naked Objects Group Ltd.
