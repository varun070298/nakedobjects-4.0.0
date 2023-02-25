package org.nakedobjects.remoting.client.facetdecorator;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facetdecorator.FacetDecoratorAbstract;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.actions.invoke.ActionInvocationFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionAddToFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionRemoveFromFacet;
import org.nakedobjects.metamodel.facets.properties.modify.PropertyClearFacet;
import org.nakedobjects.metamodel.facets.properties.modify.PropertySetterFacet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.metamodel.spec.identifier.Identified;
import org.nakedobjects.remoting.client.facets.ActionInvocationFacetWrapProxy;
import org.nakedobjects.remoting.client.facets.CollectionAddToFacetWrapProxy;
import org.nakedobjects.remoting.client.facets.CollectionRemoveFromFacetWrapProxy;
import org.nakedobjects.remoting.client.facets.PropertyClearFacetWrapProxy;
import org.nakedobjects.remoting.client.facets.PropertySetterFacetWrapProxy;
import org.nakedobjects.remoting.facade.ServerFacade;
import org.nakedobjects.remoting.protocol.encoding.internal.ObjectEncoderDecoder;

public class ProxyFacetDecorator  extends FacetDecoratorAbstract {
	
	@SuppressWarnings("unused")
	private final NakedObjectConfiguration configuration;
    private final ServerFacade serverFacade;
    private final ObjectEncoderDecoder encoderDecoder;
    
    public ProxyFacetDecorator(
            final NakedObjectConfiguration configuration,
            final ServerFacade serverFacade, 
            final ObjectEncoderDecoder encoderDecoder) {
    	this.configuration = configuration;
        this.serverFacade = serverFacade;
        this.encoderDecoder = encoderDecoder;
    }

    public Facet decorate(final Facet facet, FacetHolder requiredHolder) {
    	if (!(requiredHolder instanceof Identified)) {
            return null;
        }
        Identified identified = (Identified) requiredHolder;
        @SuppressWarnings("unused")
		final Identifier identifier = identified.getIdentifier();
        
        final Class<? extends Facet> facetType = facet.facetType();
        @SuppressWarnings("unused")
		final FacetHolder facetHolder = facet.getFacetHolder();

        if (facetType == PropertySetterFacet.class) {
            final PropertySetterFacet propertySetterFacet = (PropertySetterFacet) facet;
            PropertySetterFacetWrapProxy decoratingFacet = new PropertySetterFacetWrapProxy(
            		propertySetterFacet, serverFacade, encoderDecoder, 
            		identified.getIdentifier().getMemberName());
            return replaceFacetWithDecoratingFacet(facet, decoratingFacet, requiredHolder);
        }

        if (facetType == PropertyClearFacet.class) {
            final PropertyClearFacet propertyClearFacet = (PropertyClearFacet) facet;
            PropertyClearFacetWrapProxy decoratingFacet = new PropertyClearFacetWrapProxy(
            		propertyClearFacet, serverFacade, encoderDecoder, 
            		identified.getIdentifier().getMemberName());
            return replaceFacetWithDecoratingFacet(facet, decoratingFacet, requiredHolder);
        }

        if (facetType == CollectionAddToFacet.class) {
            final CollectionAddToFacet collectionAddToFacet = (CollectionAddToFacet) facet;
            CollectionAddToFacetWrapProxy decoratingFacet = new CollectionAddToFacetWrapProxy(
            		collectionAddToFacet, serverFacade, encoderDecoder, 
            		identified.getIdentifier().getMemberName());
            return replaceFacetWithDecoratingFacet(facet, decoratingFacet, requiredHolder);
        }

        if (facetType == CollectionRemoveFromFacet.class) {
            final CollectionRemoveFromFacet collectionRemoveFromFacet = (CollectionRemoveFromFacet) facet;
            CollectionRemoveFromFacetWrapProxy decoratingFacet = new CollectionRemoveFromFacetWrapProxy(
            		collectionRemoveFromFacet, serverFacade, encoderDecoder, 
            		identified.getIdentifier().getMemberName());
            return replaceFacetWithDecoratingFacet(facet, decoratingFacet, requiredHolder);
        }

        if (facetType == ActionInvocationFacet.class) {
            ActionInvocationFacet invocationFacet = (ActionInvocationFacet) facet;
			NakedObjectAction nakedObjectAction = (NakedObjectAction) requiredHolder;
			ActionInvocationFacetWrapProxy decoratingFacet = new ActionInvocationFacetWrapProxy(
            		invocationFacet, serverFacade, encoderDecoder, nakedObjectAction);
			return replaceFacetWithDecoratingFacet(facet, decoratingFacet, requiredHolder);
        }

        return facet;
    }

    @SuppressWarnings("unchecked")
	public Class<? extends Facet>[] getFacetTypes() {
        return new Class[] { PropertySetterFacet.class, PropertyClearFacet.class, CollectionAddToFacet.class, CollectionRemoveFromFacet.class, ActionInvocationFacet.class };
    }

}

// Copyright (c) Naked Objects Group Ltd.
