package org.nakedobjects.metamodel.commons.ensure;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;


public class EnsureTestsGivenValueThatDoesMatchTest {

    @Test
    public void whenCallEnsureThatArgThenShouldReturnOriginalObject() {
        String object = "foo";
        String returnedObject = Ensure.ensureThatArg(object, is(not(nullValue(String.class))));
        assertThat(returnedObject, sameInstance(object));
    }

    @Test
    public void whenCallEnsureThatArgWithOverloadedShouldReturnOriginalObject() {
        String object = "foo";
        String returnedObject = Ensure.ensureThatArg(object, is(not(nullValue(String.class))), "some message");
        assertThat(returnedObject, sameInstance(object));
    }

    @Test
    public void whenCallEnsureThatStateThenShouldReturnOriginalObject() {
        String object = "foo";
        String returnedObject = Ensure.ensureThatState(object, is(not(nullValue(String.class))));
        assertThat(returnedObject, sameInstance(object));
    }

    @Test
    public void whenCallEnsureThatStateWithOverloadedShouldReturnOriginalObject() {
        String object = "foo";
        String returnedObject = Ensure.ensureThatState(object, is(not(nullValue(String.class))), "some message");
        assertThat(returnedObject, sameInstance(object));
    }

    @Test
    public void whenCallEnsureThatContextThenShouldReturnOriginalObject() {
        String object = "foo";
        String returnedObject = Ensure.ensureThatContext(object, is(not(nullValue(String.class))));
        assertThat(returnedObject, sameInstance(object));
    }

    @Test
    public void whenCallEnsureThatContextWithOverloadedShouldReturnOriginalObject() {
        String object = "foo";
        String returnedObject = Ensure.ensureThatContext(object, is(not(nullValue(String.class))), "some message");
        assertThat(returnedObject, sameInstance(object));
    }


}
