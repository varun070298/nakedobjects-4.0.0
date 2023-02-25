package org.nakedobjects.metamodel.facets.object.ident.title;

import org.nakedobjects.applib.adapters.Parser;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;


public class TitleFacetUsingParser extends FacetAbstract implements TitleFacet {

    private final Parser parser;
	private final RuntimeContext runtimeContext;

    public TitleFacetUsingParser(final Parser parser, final FacetHolder holder, final RuntimeContext runtimeContext) {
        super(TitleFacet.class, holder, false);
        this.parser = parser;
        this.runtimeContext = runtimeContext;
    }

    @Override
    protected String toStringValues() {
    	getRuntimeContext().injectDependenciesInto(parser);
        return parser.toString();
    }

    public String title(final NakedObject nakedObject) {
        if (nakedObject == null) {
            return null;
        }
        final Object object = nakedObject.getObject();
        if (object == null) {
            return null;
        }
    	getRuntimeContext().injectDependenciesInto(parser);
        return parser.displayTitleOf(object);
    }

    public String title(final NakedObject nakedObject, String usingMask) {
        if (nakedObject == null) {
            return null;
        }
        final Object object = nakedObject.getObject();
        if (object == null) {
            return null;
        }
        getRuntimeContext().injectDependenciesInto(parser);
        return parser.displayTitleOf(object, usingMask);
    }

    
    ////////////////////////////////////////////////////////
    // Dependencies (from constructor)
    ////////////////////////////////////////////////////////
    

    private RuntimeContext getRuntimeContext() {
        return runtimeContext;
    }

}

// Copyright (c) Naked Objects Group Ltd.
