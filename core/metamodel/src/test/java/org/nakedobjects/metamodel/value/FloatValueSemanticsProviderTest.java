package org.nakedobjects.metamodel.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.adapter.TextEntryParseException;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetHolderImpl;

@RunWith(JMock.class)
public class FloatValueSemanticsProviderTest extends ValueSemanticsProviderAbstractTestCase {

    private FloatValueSemanticsProviderAbstract value;
    private Float float1;
    private FacetHolder holder;

    @Before
    public void setUpObjects() throws Exception {
        mockery.checking(new Expectations(){{
        	allowing(mockConfiguration).getString("nakedobjects.value.format.float");
        	will(returnValue(null));
        }});

        float1 = new Float(32.5f);
        allowMockAdapterToReturn(float1);
        
        holder = new FacetHolderImpl();
        setValue(value = new FloatWrapperValueSemanticsProvider(holder, mockConfiguration, mockSpecificationLoader, mockRuntimeContext));
    }

    @Test
    public void testInvalidParse() throws Exception {
        try {
            value.parseTextEntry(null, "one");
            fail();
        } catch (final TextEntryParseException expected) {}
    }

    @Test
    public void testTitleOf() {
        assertEquals("32.5", value.displayTitleOf(float1));
    }

    @Test
    public void testParse() throws Exception {
        final Object parsed = value.parseTextEntry(null, "120.50");
        assertEquals(120.5f, ((Float) parsed).floatValue(), 0.0);
    }

    @Test
    public void testParseBadlyFormatedEntry() throws Exception {
        final Object parsed = value.parseTextEntry(null, "1,20.0");
        assertEquals(120.0f, ((Float) parsed).floatValue(), 0.0);
    }

    @Test
    public void testEncode() throws Exception {
        assertEquals("32.5", getEncodeableFacet().toEncodedString(createAdapter(float1)));
    }

    @Test
    public void testDecode() throws Exception {
        final Object restored = value.fromEncodedString("10.25");
        assertEquals(10.25, ((Float) restored).floatValue(), 0.0);
    }

}
// Copyright (c) Naked Objects Group Ltd.
