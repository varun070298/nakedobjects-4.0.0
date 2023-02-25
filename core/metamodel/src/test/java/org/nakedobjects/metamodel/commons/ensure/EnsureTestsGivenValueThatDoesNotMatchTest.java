package org.nakedobjects.metamodel.commons.ensure;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;


public class EnsureTestsGivenValueThatDoesNotMatchTest {

    @Test
    public void whenCallEnsureThatArgShouldThrowIllegalArgumentException() {
        try {
            Ensure.ensureThatArg("foo", is(nullValue()));
            fail();
        } catch(IllegalArgumentException ex) {
            assertThat(ex.getMessage(), is("illegal argument, expected: is null"));
        }
    }

    @Test
    public void whenCallEnsureThatArgOverloadedShouldThrowIllegalArgumentExceptionUsingSuppliedMessage() {
        try {
            Ensure.ensureThatArg("foo", is(nullValue()), "my message");
            fail();
        } catch(IllegalArgumentException ex) {
            assertThat(ex.getMessage(), is("my message"));
        }
    }

    @Test
    public void whenCallEnsureThatStateShouldThrowIllegalStateException() {
        try {
            Ensure.ensureThatState("foo", is(nullValue()));
            fail();
        } catch(IllegalStateException ex) {
            assertThat(ex.getMessage(), is("illegal argument, expected: is null"));
        }
    }

    @Test
    public void whenCallEnsureThatStateOverloadedShouldThrowIllegalStateExceptionUsingSuppliedMessage() {
        try {
            Ensure.ensureThatState("foo", is(nullValue()), "my message");
            fail();
        } catch(IllegalStateException ex) {
            assertThat(ex.getMessage(), is("my message"));
        }
    }

    @Test
    public void whenCallEnsureThatContextShouldThrowIllegalThreadStateException() {
        try {
            Ensure.ensureThatContext("foo", is(nullValue()));
            fail();
        } catch(IllegalThreadStateException ex) {
            assertThat(ex.getMessage(), is("illegal argument, expected: is null"));
        }
    }

    @Test
    public void whenCallEnsureThatContextOverloadedShouldThrowIllegalThreadStateExceptionUsingSuppliedMessage() {
        try {
            Ensure.ensureThatContext("foo", is(nullValue()), "my message");
            fail();
        } catch(IllegalThreadStateException ex) {
            assertThat(ex.getMessage(), is("my message"));
        }
    }


}
