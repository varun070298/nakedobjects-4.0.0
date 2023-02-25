package org.nakedobjects.remoting.client.facets;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.facets.DecoratingFacet;
import org.nakedobjects.metamodel.facets.properties.modify.PropertySetterFacet;
import org.nakedobjects.metamodel.facets.properties.modify.PropertySetterFacetAbstract;
import org.nakedobjects.remoting.data.common.EncodableObjectData;
import org.nakedobjects.remoting.data.common.IdentityData;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.exchange.SetAssociationRequest;
import org.nakedobjects.remoting.exchange.SetAssociationResponse;
import org.nakedobjects.remoting.exchange.SetValueRequest;
import org.nakedobjects.remoting.exchange.SetValueResponse;
import org.nakedobjects.remoting.facade.ServerFacade;
import org.nakedobjects.remoting.protocol.encoding.internal.ObjectEncoderDecoder;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.ConcurrencyException;


/**
 * A reflection peer for changing one-to-many fields remotely, instead of on the local machine. Any requests
 * to add or remove elements from the field will be passed over the network to the server for completion. Only
 * requests on persistent objects are passed to the server; on a transient object the request will always be
 * dealt with locally.
 * 
 * <p>
 * If any of the objects involved have been changed on the server by another process then a
 * ConcurrencyException will be passed back to the client and re-thrown.
 * </p>
 */
public final class PropertySetterFacetWrapProxy extends PropertySetterFacetAbstract implements
        DecoratingFacet<PropertySetterFacet> {

    private final ServerFacade serverFacade;
    private final ObjectEncoderDecoder encoder;
    private final PropertySetterFacet underlyingFacet;
    private final String name;

    public PropertySetterFacetWrapProxy(
            final PropertySetterFacet underlyingFacet,
            final ServerFacade distribution,
            final ObjectEncoderDecoder encoder,
            final String name) {
        super(underlyingFacet.getFacetHolder());
        this.underlyingFacet = underlyingFacet;
        this.serverFacade = distribution;
        this.encoder = encoder;
        this.name = name;
    }

    public PropertySetterFacet getDecoratedFacet() {
        return underlyingFacet;
    }

    public void setProperty(final NakedObject targetAdapter, final NakedObject associateAdapter) {
        if (targetAdapter.isPersistent()) {
            final IdentityData targetReference = encoder.encodeIdentityData(targetAdapter);
            ObjectData[] updates;
            try {
                if (associateAdapter.getSpecification().isObject()) {
                    final IdentityData associateReference = encoder.encodeIdentityData(associateAdapter);
                    SetAssociationRequest request = new SetAssociationRequest(getAuthenticationSession(), name, targetReference, associateReference);
                    SetAssociationResponse response = 
                    	serverFacade.setAssociation(request);
					updates = response.getUpdates();
                } else {
                    final EncodableObjectData val = associateAdapter == null ? null : encoder.encodeAsValue(associateAdapter);
                    SetValueRequest request = new SetValueRequest(getAuthenticationSession(), name, targetReference, val);
					SetValueResponse response = serverFacade.setValue(request);
					updates = response.getUpdates();
                }
            } catch (final ConcurrencyException e) {
                throw ProxyUtil.concurrencyException(e);
            }
            encoder.decode(updates);
        } else {
            underlyingFacet.setProperty(targetAdapter, associateAdapter);
        }
    }

	private static AuthenticationSession getAuthenticationSession() {
		return NakedObjectsContext.getAuthenticationSession();
	}

}
// Copyright (c) Naked Objects Group Ltd.
