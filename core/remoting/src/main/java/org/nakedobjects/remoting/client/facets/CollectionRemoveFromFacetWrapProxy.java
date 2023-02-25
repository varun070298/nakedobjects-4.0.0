package org.nakedobjects.remoting.client.facets;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.facets.DecoratingFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionRemoveFromFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionRemoveFromFacetAbstract;
import org.nakedobjects.remoting.data.common.IdentityData;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.exchange.ClearAssociationRequest;
import org.nakedobjects.remoting.exchange.ClearAssociationResponse;
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
public final class CollectionRemoveFromFacetWrapProxy extends CollectionRemoveFromFacetAbstract implements
        DecoratingFacet<CollectionRemoveFromFacet> {

    private final static Logger LOG = Logger.getLogger(CollectionRemoveFromFacetWrapProxy.class);
    private final ServerFacade serverFacade;
    private final ObjectEncoderDecoder encoder;
    private final CollectionRemoveFromFacet underlyingFacet;
    private final String name;

    public CollectionRemoveFromFacetWrapProxy(
            final CollectionRemoveFromFacet underlyingFacet,
            final ServerFacade connection,
            final ObjectEncoderDecoder encoder,
            final String name) {
        super(underlyingFacet.getFacetHolder());
        this.underlyingFacet = underlyingFacet;
        this.serverFacade = connection;
        this.encoder = encoder;
        this.name = name;
    }

    public CollectionRemoveFromFacet getDecoratedFacet() {
        return underlyingFacet;
    }

    /**
     * Remove an associated object (the element) from the specified NakedObject in the association field
     * represented by this object.
     */
    public void remove(final NakedObject inObject, final NakedObject associate) {
        if (inObject.isPersistent()) {
            LOG.debug("clear association remotely " + inObject + "/" + associate);
            try {
                final IdentityData targetReference = encoder.encodeIdentityData(inObject);
                final IdentityData associateReference = encoder.encodeIdentityData(associate);
                ClearAssociationRequest request = new ClearAssociationRequest(getAuthenticationSession(), name, targetReference, associateReference);
				ClearAssociationResponse response = serverFacade.clearAssociation(request);
				ObjectData[] updates = response.getUpdates();
				encoder.decode(updates);
            } catch (final ConcurrencyException e) {
                throw ProxyUtil.concurrencyException(e);
            } catch (final NakedObjectException e) {
                LOG.error("remote exception: " + e.getMessage(), e);
                throw e;
            }
        } else {
            LOG.debug("clear association locally " + inObject + "/" + associate);
            underlyingFacet.remove(inObject, associate);
        }
    }

	protected static AuthenticationSession getAuthenticationSession() {
		return NakedObjectsContext.getAuthenticationSession();
	}
}
// Copyright (c) Naked Objects Group Ltd.
