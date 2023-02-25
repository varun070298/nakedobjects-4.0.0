package org.nakedobjects.metamodel.value;

import static org.junit.Assert.assertEquals;

import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.applib.value.Password;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetHolderImpl;

@RunWith(JMock.class)
public class PasswordValueSemanticsProviderTest extends ValueSemanticsProviderAbstractTestCase {

    private PasswordValueSemanticsProvider adapter;
    private Object password;
    private FacetHolder holder;

    @Before
    public void setUpObjects() throws Exception {
        holder = new FacetHolderImpl();
        setValue(adapter = new PasswordValueSemanticsProvider(holder, mockConfiguration, mockSpecificationLoader, mockRuntimeContext));
        password = new Password("secret");
    }

    @Test
    public void testEncoding() {
        assertEquals("secret", new String(adapter.toEncodedString(password)));
    }
}

// Copyright (c) Naked Objects Group Ltd.
