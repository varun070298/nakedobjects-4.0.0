package org.nakedobjects.metamodel.commons.matchers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;


public class NofMatcherNonEmptyStringOrNullTest {

    private Matcher<String> fooMatcher;

    @Before
    public void setUp() {
        fooMatcher = NofMatchers.nonEmptyStringOrNull();
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
    public void shouldMatchNullString() {
        assertThat(fooMatcher.matches(null), is(true));
    }

}
