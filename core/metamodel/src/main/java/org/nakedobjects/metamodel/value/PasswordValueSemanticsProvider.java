package org.nakedobjects.metamodel.value;

import org.nakedobjects.applib.adapters.EncoderDecoder;
import org.nakedobjects.applib.adapters.Parser;
import org.nakedobjects.applib.value.Password;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.value.PasswordValueFacet;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public class PasswordValueSemanticsProvider extends ValueSemanticsProviderAbstract implements PasswordValueFacet {

    public static Class<? extends Facet> type() {
        return PasswordValueFacet.class;
    }

    private static final int TYPICAL_LENGTH = 12;
    private static final boolean IMMUTABLE = true;
    private static final boolean EQUAL_BY_CONTENT = true;
    private static final Object DEFAULT_VALUE = null; // no default

    /**
     * Required because implementation of {@link Parser} and {@link EncoderDecoder}.
     */
    public PasswordValueSemanticsProvider() {
        this(null, null, null, null);
    }

    public PasswordValueSemanticsProvider(
    		final FacetHolder holder,
    		final NakedObjectConfiguration configuration,
    		final SpecificationLoader specificationLoader,
    		final RuntimeContext runtimeContext) {
        super(type(), holder, Password.class, TYPICAL_LENGTH, IMMUTABLE, EQUAL_BY_CONTENT, DEFAULT_VALUE, configuration, specificationLoader, runtimeContext);
    }

    // //////////////////////////////////////////////////////////////////
    // Parser
    // //////////////////////////////////////////////////////////////////

    @Override
    protected Object doParse(final Object original, final String text) {
        return new Password(text);
    }

    @Override
    public String titleString(final Object object) {
        return object == null ? "" : password(object).toString();
    }

    public String titleStringWithMask(final Object object, final String usingMask) {
        return titleString(object);
    }

    // //////////////////////////////////////////////////////////////////
    // EncoderDecoder
    // //////////////////////////////////////////////////////////////////

    @Override
    protected String doEncode(final Object object) {
        return password(object).getPassword();
    }

    @Override
    protected Object doRestore(final String data) {
        return new Password(data);
    }

    // //////////////////////////////////////////////////////////////////
    // PasswordValueFacet
    // //////////////////////////////////////////////////////////////////

    public boolean checkPassword(final NakedObject object, final String password) {
        return password(object.getObject()).checkPassword(password);
    }

    public String getEditText(final NakedObject object) {
        return object == null ? "" : password(object).getPassword();
    }

    public NakedObject createValue(final String password) {
        return getRuntimeContext().adapterFor(new Password(password));
    }

    private Password password(final Object object) {
        return (Password) object;
    }


    // /////// toString ///////

    @Override
    public String toString() {
        return "PasswordValueSemanticsProvider";
    }


}

// Copyright (c) Naked Objects Group Ltd.
