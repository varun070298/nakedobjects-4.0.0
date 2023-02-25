package org.nakedobjects.metamodel.commons.matchers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;


public class NofMatcherContainsStripNewLinesTest {

    private Matcher<String> fooMatcher;

    @Before
    public void setUp() {
        fooMatcher = NofMatchers.containsStripNewLines("foo");
    }

    @Test
    public void shouldMatchExactString() {
        assertThat(fooMatcher.matches("foo"), is(true));
    }

    @Test
    public void shouldMatchIfContainsStringNoNewLines() {
        assertThat(fooMatcher.matches("abcfoodef"), is(true));
    }

    @Test
    public void shouldMatchIfContainsStringHasNewLinesBefore() {
        assertThat(fooMatcher.matches("a\nb\rc\r\ndfoodef"), is(true));
    }

    @Test
    public void shouldMatchIfContainsStringHasNewLinesAfter() {
        assertThat(fooMatcher.matches("abrdfood\ne\rfan\rg"), is(true));
    }

    @Test
    public void shouldMatchIfContainsStringHasNewLinesWithin() {
        assertThat(fooMatcher.matches("abcf\ro\nodef"), is(true));
    }

    @Test
    public void shouldNotMatchIfDoesNotContainsString() {
        assertThat(fooMatcher.matches("fob"), is(false));
    }

}
