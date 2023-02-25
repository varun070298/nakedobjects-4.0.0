package org.nakedobjects.metamodel.value;

import org.nakedobjects.applib.adapters.EncoderDecoder;
import org.nakedobjects.applib.adapters.Parser;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.value.StringValueFacet;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public class StringValueSemanticsProvider extends ValueSemanticsProviderAbstract implements StringValueFacet {

    public static Class<? extends Facet> type() {
        return StringValueFacet.class;
    }

    private static final int TYPICAL_LENGTH = 25;
    private static final boolean IMMUTABLE = true;
    private static final boolean EQUAL_BY_CONTENT = true;
    private static final Object DEFAULT_VALUE = null; // no default

    /**
     * Required because implementation of {@link Parser} and {@link EncoderDecoder}.
     */
    public StringValueSemanticsProvider() {
        this(null, null, null, null);
    }

    public StringValueSemanticsProvider(
    		final FacetHolder holder,
            final NakedObjectConfiguration configuration, 
            final SpecificationLoader specificationLoader, 
            final RuntimeContext runtimeContext) {
        super(type(), holder, String.class, TYPICAL_LENGTH, IMMUTABLE, EQUAL_BY_CONTENT, DEFAULT_VALUE, configuration, specificationLoader, runtimeContext);
    }

    // //////////////////////////////////////////////////////////////////
    // Parser
    // //////////////////////////////////////////////////////////////////

    @Override
    protected Object doParse(final Object original, final String entry) {
        if (entry.trim().equals("")) {
            return null;
        } else {
            return entry;
        }
    }

    @Override
    public String titleString(final Object object) {
        final String string = (String) (object == null ? "" : object);
        return string;
    }

    public String titleStringWithMask(final Object object, final String usingMask) {
        return titleString(object);
    }

    // //////////////////////////////////////////////////////////////////
    // EncoderDecoder
    // //////////////////////////////////////////////////////////////////

    @Override
    protected String doEncode(final Object object) {
        final String text = (String) object;
        if (text.equals("NULL") || isEscaped(text)) {
            return escapeText(text);
        } else {
            return text;
        }
    }

    @Override
    protected Object doRestore(final String data) {
        if (isEscaped(data)) {
            return data.substring(1);
        } else {
            return data;
        }
    }

    private boolean isEscaped(final String text) {
        return text.startsWith("/");
    }

    private String escapeText(final String text) {
        return "/" + text;
    }

    // //////////////////////////////////////////////////////////////////
    // StringValueFacet
    // //////////////////////////////////////////////////////////////////

    public String stringValue(final NakedObject object) {
        return object == null ? "" : (String) object.getObject();
    }

    public NakedObject createValue(final String value) {
        return getRuntimeContext().adapterFor(value);
    }


    // /////// toString ///////

    @Override
    public String toString() {
        return "StringValueSemanticsProvider";
    }


}
// Copyright (c) Naked Objects Group Ltd.
