package org.nakedobjects.metamodel.commons.lang;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;


public class StringUtilsStripNewLinesTest {

    @Test
    public void shouldDoNothingIfNone() {
        assertThat(StringUtils.stripNewLines("abc"), is("abc"));
    }

    @Test
    public void shouldStripIfJustBackslashN() {
        assertThat(StringUtils.stripNewLines("abc\n"), is("abc"));
    }

    @Test
    public void shouldStripIfBackslashRBackslashN() {
        assertThat(StringUtils.stripNewLines("abc\r\n"), is("abc"));
    }

    @Test
    public void shouldStripIfJustBackslashR() {
        assertThat(StringUtils.stripNewLines("abc\r"), is("abc"));
    }

    @Test
    public void shouldStripIfSeveral() {
        assertThat(StringUtils.stripNewLines("abc\r\ndef\r\n"), is("abcdef"));
    }

    @Test
    public void shouldStripIfBackslashRBackslashNBackslashR() {
        assertThat(StringUtils.stripNewLines("abc\n\r\ndef"), is("abcdef"));
    }

}
// Copyright (c) Naked Objects Group Ltd.
