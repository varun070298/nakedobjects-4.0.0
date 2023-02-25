package org.nakedobjects.metamodel.commons.matchers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;


public class NofMatcherEndsWithStripNewLinesTest {

    private Matcher<String> fooMatcher;

    @Before
    public void setUp() {
        fooMatcher = NofMatchers.endsWithStripNewLines("foo");
    }

    @Test
    public void shouldMatchExactString() {
        assertThat(fooMatcher.matches("foo"), is(true));
    }

    @Test
    public void shouldMatchIfEndsWithAndStringNoNewLines() {
        assertThat(fooMatcher.matches("abcfoo"), is(true));
    }

    @Test
    public void shouldMatchIfEndsWithStringHasNewLinesAfter() {
        assertThat(fooMatcher.matches("a\nb\rc\r\nfoo"), is(true));
    }

    @Test
    public void shouldMatchIfEndsWithStringHasNewLinesWithin() {
        assertThat(fooMatcher.matches("abcf\ro\no"), is(true));
    }

    @Test
    public void shouldNotMatchIfDoesNotEndsWithString() {
        assertThat(fooMatcher.matches("fob"), is(false));
    }

}
