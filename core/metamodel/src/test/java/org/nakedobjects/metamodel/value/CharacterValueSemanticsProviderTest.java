package org.nakedobjects.metamodel.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.adapter.InvalidEntryException;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetHolderImpl;

@RunWith(JMock.class)
public class CharacterValueSemanticsProviderTest extends ValueSemanticsProviderAbstractTestCase {

    private CharValueSemanticsProviderAbstract value;

    private Character character;
    private NakedObject characterNO;

    private FacetHolder holder;

    @Before
    public void setUpObjects() throws Exception {
        character = new Character('r');
        characterNO = createAdapter(character);
        holder = new FacetHolderImpl();
        setValue(value = new CharWrapperValueSemanticsProvider(holder, mockConfiguration, mockSpecificationLoader, mockRuntimeContext));
    }

    @Test
    public void testParseLongString() throws Exception {
        try {
            value.parseTextEntry(null, "one");
            fail();
        } catch (final InvalidEntryException expected) {}
    }

    @Test
    public void testTitleOf() {
        assertEquals("r", value.displayTitleOf(character));
    }

    @Test
    public void testValidParse() throws Exception {
        final Object parse = value.parseTextEntry(null, "t");
        assertEquals(new Character('t'), parse);
    }

    @Test
    public void testEncode() throws Exception {
        assertEquals("r", value.toEncodedString(character));
    }

    @Test
    public void testDecode() throws Exception {
        final Object restore = value.fromEncodedString("Y");
        assertEquals(new Character('Y'), restore);
    }
}
// Copyright (c) Naked Objects Group Ltd.
