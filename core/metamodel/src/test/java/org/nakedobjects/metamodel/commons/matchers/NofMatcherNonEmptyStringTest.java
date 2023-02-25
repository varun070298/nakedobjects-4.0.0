package org.nakedobjects.metamodel.commons.matchers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;


public class NofMatcherNonEmptyStringTest {

    private Matcher<String> fooMatcher;

    @Before
    public void setUp() {
        fooMatcher = NofMatchers.nonEmptyString();
    }

    @Test
    public void shouldMatchNonEmptyString() {
        assertThat(fooMatcher.matches("foo"), is(true));
    }

    @Test
    public void shouldNotMatchEmptyString() {
        assertThat(fooMatcher.matches(""), is(false));
    }

    @Test
    public void shouldNotMatchNullString() {
        assertThat(fooMatcher.matches(null), is(false));
    }

}
