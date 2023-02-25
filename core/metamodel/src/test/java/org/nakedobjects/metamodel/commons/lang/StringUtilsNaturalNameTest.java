package org.nakedobjects.metamodel.commons.lang;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;


public class StringUtilsNaturalNameTest {

    @Test
    public void shouldCreateANaturalNameForABooleanPropertyAccessorName() {
        assertThat(StringUtils.naturalName("isOutOfStock"), is("Out Of Stock"));
    }

    @Test
    public void shouldCreateANaturalNameForABooleanPropertyAccessorNameSingleWord() {
        assertThat(StringUtils.naturalName("isBlacklisted"), is("Blacklisted"));
    }

    @Test
    public void shouldCreateANaturalNameForANonBooleanPropertyAccessorName() {
        assertThat(StringUtils.naturalName("getFirstName"), is("First Name"));
    }

    @Test
    public void shouldCreateANaturalNameForANonBooleanPropertyAccessorNameSingleWord() {
        assertThat(StringUtils.naturalName("getAge"), is("Age"));
    }

    @Test
    public void shouldCreateANaturalNameForAPropertyMutatorName() {
        assertThat(StringUtils.naturalName("setFirstName"), is("First Name"));
    }

    @Test
    public void shouldCreateANaturalNameForAPropertyMutatorNameSingleWord() {
        assertThat(StringUtils.naturalName("setAge"), is("Age"));
    }

}
// Copyright (c) Naked Objects Group Ltd.
