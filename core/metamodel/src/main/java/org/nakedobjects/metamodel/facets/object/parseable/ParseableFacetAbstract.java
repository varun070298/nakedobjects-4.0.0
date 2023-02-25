package org.nakedobjects.metamodel.facets.object.parseable;

import org.nakedobjects.applib.adapters.Parser;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.util.ClassUtil;


public abstract class ParseableFacetAbstract extends FacetAbstract implements ParseableFacet {

    private final Class<?> parserClass;

    // to delegate to
    private final ParseableFacetUsingParser parseableFacetUsingParser;

	private final RuntimeContext runtimeContext;

    public ParseableFacetAbstract(
    		final String candidateParserName, 
    		final Class<?> candidateParserClass, 
    		final FacetHolder holder, 
    		final RuntimeContext runtimeContext) {
        super(ParseableFacet.class, holder, false);
        
        this.runtimeContext = runtimeContext;
        

        this.parserClass = ParserUtil.parserOrNull(candidateParserClass, candidateParserName);
        if (isValid()) {
            Parser parser = (Parser) ClassUtil.newInstance(parserClass, FacetHolder.class, holder);
            this.parseableFacetUsingParser = new ParseableFacetUsingParser(parser,
                    holder, runtimeContext);
        } else {
            this.parseableFacetUsingParser = null;
        }
    }

    /**
     * Discover whether either of the candidate parser name or class is valid.
     */
    public boolean isValid() {
        return parserClass != null;
    }

    /**
     * Guaranteed to implement the {@link Parser} class, thanks to generics in the applib.
     */
    public Class<?> getParserClass() {
        return parserClass;
    }

    @Override
    protected String toStringValues() {
        return parserClass.getName();
    }

    public NakedObject parseTextEntry(final NakedObject original, final String entryText) {
        return parseableFacetUsingParser.parseTextEntry(original, entryText);
    }

    public String parseableTitle(final NakedObject existing) {
        return parseableFacetUsingParser.parseableTitle(existing);
    }
    
    
    public RuntimeContext getRuntimeContext() {
		return runtimeContext;
	}

}

// Copyright (c) Naked Objects Group Ltd.
