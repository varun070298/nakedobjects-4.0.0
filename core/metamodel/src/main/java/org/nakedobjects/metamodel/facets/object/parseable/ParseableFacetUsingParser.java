package org.nakedobjects.metamodel.facets.object.parseable;

import org.nakedobjects.applib.adapters.Parser;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;
import org.nakedobjects.metamodel.consent.InteractionResultSet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.object.value.ValueFacet;
import org.nakedobjects.metamodel.interactions.InteractionUtils;
import org.nakedobjects.metamodel.interactions.ObjectValidityContext;
import org.nakedobjects.metamodel.interactions.ParseValueContext;
import org.nakedobjects.metamodel.interactions.ValidityContext;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.util.NakedObjectUtils;


/**
 * TODO: need to fix genericity of using Parser<?>, for now suppressing warnings.
 */
public class ParseableFacetUsingParser extends FacetAbstract implements ParseableFacet {

    @SuppressWarnings("unchecked")
	private final Parser parser;
	private final RuntimeContext runtimeContext;

    public ParseableFacetUsingParser(
    	    @SuppressWarnings("unchecked")
    		final Parser parser, 
    		final FacetHolder holder, 
    		final RuntimeContext runtimeContext) {
        super(ParseableFacet.class, holder, false);
        this.parser = parser;
        this.runtimeContext = runtimeContext;
    }

    @Override
    protected String toStringValues() {
		runtimeContext.injectDependenciesInto(parser);
        return parser.toString();
    }

    public NakedObject parseTextEntry(final NakedObject contextAdapter, final String entry) {
		if (entry == null) {
            throw new IllegalArgumentException("An entry must be provided");
        }
		
		// check string is valid
		// (eg pick up any @RegEx on value type)
		if (getFacetHolder().containsFacet(ValueFacet.class)) {
			NakedObject entryAdapter = getRuntimeContext().adapterFor(entry);
			ParseValueContext parseValueContext = new ParseValueContext(getRuntimeContext().getAuthenticationSession(), InteractionInvocationMethod.BY_USER, contextAdapter, getIdentified().getIdentifier(), entryAdapter);
			validate(parseValueContext);
		}

        Object context = NakedObjectUtils.unwrap(contextAdapter);

        getRuntimeContext().injectDependenciesInto(parser);

        @SuppressWarnings("unchecked")
		final Object parsed = parser.parseTextEntry(context, entry);
        if (parsed == null) {
			return null;
		}
        
        // check resultant object is also valid
        // (eg pick up any validate() methods on it)
		NakedObject adapter = getRuntimeContext().adapterFor(parsed);
		NakedObjectSpecification specification = adapter.getSpecification();
		ObjectValidityContext validateContext = specification.createValidityInteractionContext(getRuntimeContext().getAuthenticationSession(), InteractionInvocationMethod.BY_USER, adapter);
		validate(validateContext);
		
		return adapter;
	}

	private void validate(ValidityContext<?> validityContext) {
		InteractionResultSet resultSet = new InteractionResultSet();
		InteractionUtils.isValidResultSet(getFacetHolder(), validityContext, resultSet);
		if (resultSet.isVetoed()) {
			throw new IllegalArgumentException(resultSet.getInteractionResult().getReason());
		}
	}

    @SuppressWarnings("unchecked")
	public String parseableTitle(final NakedObject contextAdapter) {
        Object pojo = NakedObjectUtils.unwrap(contextAdapter);
        
        getRuntimeContext().injectDependenciesInto(parser);
		return parser.parseableTitleOf(pojo);
	}

    
    
    ///////////////////////////////////////////////////////////
    // Dependencies (from constructor)
    ///////////////////////////////////////////////////////////

    private RuntimeContext getRuntimeContext() {
        return runtimeContext;
    }

}

// Copyright (c) Naked Objects Group Ltd.
