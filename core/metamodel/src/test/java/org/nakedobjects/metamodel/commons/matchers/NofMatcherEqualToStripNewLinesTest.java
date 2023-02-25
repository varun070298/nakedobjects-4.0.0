package org.nakedobjects.metamodel.commons.matchers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;


public class NofMatcherEqualToStripNewLinesTest {

    private Matcher<String> fooMatcher;

    @Before
    public void setUp() {
        fooMatcher = NofMatchers.equalToStripNewLines("foo");
    }

    @Test
    public void shouldMatchExactString() {
        assertThat(fooMatcher.matches("foo"), is(true));
    }

    @Test
    public void shouldNotMatchIfStartsWithAnything() {
        assertThat(fooMatcher.matches("afoo"), is(false));
    }

    @Test
    public void shouldNotMatchIfEndsWithAnything() {
        assertThat(fooMatcher.matches("fooz"), is(false));
    }

    @Test
    public void shouldMatchIfEqualToStringHasNewLinesWithin() {
        assertThat(fooMatcher.matches("f\ro\no"), is(true));
    }

    @Test
    public void shouldNotMatchIfDoesNotStartWithString() {
        assertThat(fooMatcher.matches("fob"), is(false));
    }

}
