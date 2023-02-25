package org.nakedobjects.applib.spec;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;


import org.junit.Test;
import org.nakedobjects.applib.spec.AbstractSpecification.Nullability;
import org.nakedobjects.applib.spec.AbstractSpecification.TypeChecking;

public class SpecificationAbstractTest {

    private static class SomeDomainObject {}
    private static class SomeOtherDomainObject {}
    
    private AbstractSpecification<SomeDomainObject> specAbstractSomeDomainObject;

	@Test
	public void shouldSatisfyByDefaultForNull() {
	    specAbstractSomeDomainObject = new AbstractSpecification<SomeDomainObject>() {
            @Override
            public String satisfiesSafely(SomeDomainObject obj) {
                return null;
            }};
        assertThat(specAbstractSomeDomainObject.satisfies(null), is(nullValue()));
	}

    @Test
    public void shouldNotSatisfyForNullIfConfiguredAsSuch() {
        specAbstractSomeDomainObject = new AbstractSpecification<SomeDomainObject>(Nullability.ENSURE_NOT_NULL, TypeChecking.IGNORE_INCORRECT_TYPE) {
            @Override
            public String satisfiesSafely(SomeDomainObject obj) {
                return null;
            }};
        assertThat(specAbstractSomeDomainObject.satisfies(null), is(not(nullValue())));
    }


    @Test
    public void shouldSatisfyByDefaultForIncorrectType() {
        specAbstractSomeDomainObject = new AbstractSpecification<SomeDomainObject>() {
            @Override
            public String satisfiesSafely(SomeDomainObject obj) {
                return null;
            }};
        assertThat(specAbstractSomeDomainObject.satisfies(new SomeOtherDomainObject()), is(nullValue()));
    }

    @Test
    public void shouldNotSatisfyForIncorrectTypeIfConfiguredAsSuch() {
        specAbstractSomeDomainObject = new AbstractSpecification<SomeDomainObject>(Nullability.IGNORE_IF_NULL, TypeChecking.ENSURE_CORRECT_TYPE) {
            @Override
            public String satisfiesSafely(SomeDomainObject obj) {
                return null;
            }};
        assertThat(specAbstractSomeDomainObject.satisfies(new SomeOtherDomainObject()), is(not(nullValue())));
    }

    @Test
    public void shouldSatisfyForNonNullCorrectTypeIfConfiguredAsSuch() {
        specAbstractSomeDomainObject = new AbstractSpecification<SomeDomainObject>(Nullability.ENSURE_NOT_NULL, TypeChecking.ENSURE_CORRECT_TYPE) {
            @Override
            public String satisfiesSafely(SomeDomainObject obj) {
                return null;
            }};
        assertThat(specAbstractSomeDomainObject.satisfies(new SomeDomainObject()), is(nullValue()));
    }


}
