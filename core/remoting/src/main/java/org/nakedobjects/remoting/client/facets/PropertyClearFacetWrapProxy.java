package org.nakedobjects.remoting.client.facets;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.facets.DecoratingFacet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.properties.modify.PropertyClearFacet;
import org.nakedobjects.metamodel.facets.properties.modify.PropertyClearFacetAbstract;
import org.nakedobjects.remoting.data.common.IdentityData;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.exchange.ClearAssociationRequest;
import org.nakedobjects.remoting.exchange.ClearAssociationResponse;
import org.nakedobjects.remoting.facade.ServerFacade;
import org.nakedobjects.remoting.protocol.encoding.internal.ObjectEncoderDecoder;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.ConcurrencyException;


/**
 * A reflection peer for changing one-to-many fields remotely, instead of on the local machine.
 * 
 * <p>
 * Any requests to add or remove elements from the field will be passed over the network to the server for
 * completion. Only requests on persistent objects are passed to the server; on a transient object the request
 * will always be dealt with locally.
 * 
 * <p>
 * If any of the objects involved have been changed on the server by another process then a
 * {@link ConcurrencyException} will be passed back to the client and re-thrown.
 * </p>
 */
public final class PropertyClearFacetWrapProxy extends PropertyClearFacetAbstract implements DecoratingFacet<PropertyClearFacet> {

    private final ServerFacade serverFacade;
    private final ObjectEncoderDecoder encoder;
    private final PropertyClearFacet underlyingFacet;
    private final String name;

    public PropertyClearFacetWrapProxy(
            final PropertyClearFacet underlyingFacet,
            final ServerFacade serverFacade,
            final ObjectEncoderDecoder encoderDecoder,
            final String name) {
    	super(underlyingFacet.getFacetHolder());
        this.underlyingFacet = underlyingFacet;
        this.serverFacade = serverFacade;
        this.encoder = encoderDecoder;
        this.name = name;
    }

    public PropertyClearFacet getDecoratedFacet() {
        return underlyingFacet;
    }

    public void clearProperty(final NakedObject inObject) {
        if (inObject.isPersistent()) {
            final IdentityData targetReference = encoder.encodeIdentityData(inObject);
            ObjectData[] updates;
            try {
                IdentityData nullData = encoder.encodeIdentityData(null); // not used.
				ClearAssociationRequest request = 
                	new ClearAssociationRequest(getAuthenticationSession(), name, targetReference, nullData);
                ClearAssociationResponse response = serverFacade.clearAssociation(request);
				updates = response.getUpdates();
            } catch (final ConcurrencyException e) {
                throw ProxyUtil.concurrencyException(e);
            }
            encoder.decode(updates);
        } else {
            underlyingFacet.clearProperty(inObject);
        }
    }

	protected static AuthenticationSession getAuthenticationSession() {
		return NakedObjectsContext.getAuthenticationSession();
	}

}
// Copyright (c) Naked Objects Group Ltd.
