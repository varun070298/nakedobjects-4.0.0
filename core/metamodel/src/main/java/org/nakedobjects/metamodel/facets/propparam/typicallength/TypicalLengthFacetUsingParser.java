package org.nakedobjects.metamodel.facets.propparam.typicallength;

import org.nakedobjects.applib.adapters.Parser;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;


public class TypicalLengthFacetUsingParser extends FacetAbstract implements TypicalLengthFacet {

    private final Parser parser;
	private final RuntimeContext runtimeContext;

    public TypicalLengthFacetUsingParser(final Parser parser, final FacetHolder holder, final RuntimeContext runtimeContext) {
        super(TypicalLengthFacet.class, holder, false);
        this.parser = parser;
        this.runtimeContext = runtimeContext;
    }

    @Override
    protected String toStringValues() {
    	getRuntimeContext().injectDependenciesInto(parser);
        return parser.toString();
    }

    public int value() {
    	getRuntimeContext().injectDependenciesInto(parser);
        return parser.typicalLength();
    }

    @Override
    public String toString() {
        return "typicalLength=" + value();
    }

    
    ////////////////////////////////////////////////////////
    // Dependencies (from constructor)
    ////////////////////////////////////////////////////////
    

    private RuntimeContext getRuntimeContext() {
        return runtimeContext;
    }

}

// Copyright (c) Naked Objects Group Ltd.
