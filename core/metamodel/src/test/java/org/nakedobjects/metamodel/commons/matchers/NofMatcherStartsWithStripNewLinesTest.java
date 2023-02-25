package org.nakedobjects.metamodel.commons.matchers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;


public class NofMatcherStartsWithStripNewLinesTest {

    private Matcher<String> fooMatcher;

    @Before
    public void setUp() {
        fooMatcher = NofMatchers.startsWithStripNewLines("foo");
    }

    @Test
    public void shouldMatchExactString() {
        assertThat(fooMatcher.matches("foo"), is(true));
    }

    @Test
    public void shouldMatchIfStartsWithAndStringNoNewLines() {
        assertThat(fooMatcher.matches("foodef"), is(true));
    }

    @Test
    public void shouldMatchIfStartsWithStringHasNewLinesAfter() {
        assertThat(fooMatcher.matches("food\ne\rfan\rg"), is(true));
    }

    @Test
    public void shouldMatchIfStartsWithStringHasNewLinesWithin() {
        assertThat(fooMatcher.matches("f\ro\nodef"), is(true));
    }

    @Test
    public void shouldNotMatchIfDoesNotStartWithString() {
        assertThat(fooMatcher.matches("fob"), is(false));
    }

}
