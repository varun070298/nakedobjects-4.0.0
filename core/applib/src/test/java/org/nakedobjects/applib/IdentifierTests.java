package org.nakedobjects.applib;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.applib.Identifier;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;


@RunWith(JMock.class)
public class IdentifierTests {

    @SuppressWarnings("unused")
	private final Mockery mockery = new JUnit4Mockery();

    private Identifier identifier;

    @Before
    public void setUp() {
    }

    @Test
    public void canInstantiateClassIdentifier() {
        identifier = Identifier.classIdentifier(SomeDomainClass.class);
        assertThat(identifier, is(not(nullValue())));
    }

    @Test
    public void classIdentifierClassNameIsSet() {
        Class<?> domainClass = SomeDomainClass.class;
        String domainClassFullyQualifiedName = domainClass.getCanonicalName();
        identifier = Identifier.classIdentifier(domainClass);
        assertThat(identifier.getClassName(), is(domainClassFullyQualifiedName));
    }

}
