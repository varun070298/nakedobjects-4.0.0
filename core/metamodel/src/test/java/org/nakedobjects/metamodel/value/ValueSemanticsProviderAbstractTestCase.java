package org.nakedobjects.metamodel.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.nakedobjects.metamodel.testutil.ReturnArgumentJMockAction.returnArgument;

import java.util.Locale;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.object.encodeable.EncodableFacet;
import org.nakedobjects.metamodel.facets.object.encodeable.EncodableFacetUsingEncoderDecoder;
import org.nakedobjects.metamodel.facets.object.parseable.ParseableFacet;
import org.nakedobjects.metamodel.facets.object.parseable.ParseableFacetUsingParser;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


@RunWith(JMock.class)
public abstract class ValueSemanticsProviderAbstractTestCase {
    
    protected Mockery mockery = new JUnit4Mockery();

    private ValueSemanticsProviderAbstract value;
    private EncodableFacetUsingEncoderDecoder encodeableFacet;
    private ParseableFacetUsingParser parseableFacet;
    
    protected FacetHolder mockFacetHolder;
    
    protected NakedObjectConfiguration mockConfiguration;
    protected SpecificationLoader mockSpecificationLoader;
    protected RuntimeContext mockRuntimeContext;
    protected NakedObject mockAdapter;

    @Before
    public void setUp() throws Exception {
        Locale.setDefault(Locale.UK);
        
        mockFacetHolder = mockery.mock(FacetHolder.class);
        mockRuntimeContext = mockery.mock(RuntimeContext.class);
        mockSpecificationLoader = mockery.mock(SpecificationLoader.class);
        mockConfiguration = mockery.mock(NakedObjectConfiguration.class);

        mockery.checking(new Expectations(){{
        	allowing(mockConfiguration).getString(with(any(String.class)), with(any(String.class)));
        	will(returnArgument(1));
        	
        	allowing(mockConfiguration).getBoolean(with(any(String.class)), with(any(Boolean.class)));
        	will(returnArgument(1));

        	allowing(mockConfiguration).getString("nakedobjects.locale");
        	will(returnValue(null));
        	
        	allowing(mockRuntimeContext).injectDependenciesInto(with(any(Object.class)));
        }});

        mockAdapter = mockery.mock(NakedObject.class);
    }

    @After
    public void tearDown() throws Exception {
        mockery.assertIsSatisfied();
    }

	protected void allowMockAdapterToReturn(final Object pojo) {
		mockery.checking(new Expectations(){{
        	allowing(mockAdapter).getObject();
			will(returnValue( pojo ));
        }});
	}

    protected void setValue(final ValueSemanticsProviderAbstract value) {
        this.value = value;
        this.encodeableFacet = new EncodableFacetUsingEncoderDecoder(value, mockFacetHolder, mockRuntimeContext);
        this.parseableFacet = new ParseableFacetUsingParser(value, mockFacetHolder, mockRuntimeContext);
    }

	protected ValueSemanticsProviderAbstract getValue() {
        return value;
    }

    protected EncodableFacet getEncodeableFacet() {
        return encodeableFacet;
    }

    protected ParseableFacet getParseableFacet() {
        return parseableFacet;
    }


    protected void setupSpecification(final Class<?> type) {
//        final TestProxySpecification specification = system.getSpecification(cls);
//        specification.setupHasNoIdentity(true);
    }

    protected NakedObject createAdapter(final Object object) {
        //return system.createAdapterForTransient(object);
    	return mockAdapter;
    }

    @Test
    public void testParseNull() throws Exception {
        try {
            value.parseTextEntry(null, null);
            fail();
        } catch (final IllegalArgumentException expected) {}
    }

    @Test
    public void testParseEmptyString() throws Exception {
        final Object newValue = value.parseTextEntry(null, "");
        assertNull(newValue);
    }

    @Test
    public void testDecodeNULL() throws Exception {
        final Object newValue = encodeableFacet.fromEncodedString(EncodableFacetUsingEncoderDecoder.ENCODED_NULL);
        assertNull(newValue);
    }

    @Test
    public void testEmptyEncoding() {
        assertEquals(EncodableFacetUsingEncoderDecoder.ENCODED_NULL, encodeableFacet.toEncodedString(null));
    }

    @Test
    public void testTitleOfForNullObject() {
        assertEquals("", value.displayTitleOf(null));
    }
    
    

}
// Copyright (c) Naked Objects Group Ltd.
