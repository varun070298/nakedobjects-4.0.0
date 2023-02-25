package org.nakedobjects.remoting.client.facets;

import org.apache.log4j.Logger;
import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.facets.DecoratingFacet;
import org.nakedobjects.metamodel.facets.actions.executed.ExecutedFacet;
import org.nakedobjects.metamodel.facets.actions.executed.ExecutedFacet.Where;
import org.nakedobjects.metamodel.facets.actions.invoke.ActionInvocationFacet;
import org.nakedobjects.metamodel.facets.actions.invoke.ActionInvocationFacetAbstract;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionConstants;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionParameter;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;
import org.nakedobjects.remoting.data.Data;
import org.nakedobjects.remoting.data.common.NullData;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.data.common.ReferenceData;
import org.nakedobjects.remoting.exchange.ExecuteServerActionRequest;
import org.nakedobjects.remoting.exchange.ExecuteServerActionResponse;
import org.nakedobjects.remoting.exchange.KnownObjectsRequest;
import org.nakedobjects.remoting.facade.ServerFacade;
import org.nakedobjects.remoting.protocol.encoding.internal.ObjectEncoderDecoder;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.ConcurrencyException;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;
import org.nakedobjects.runtime.transaction.messagebroker.MessageBroker;
import org.nakedobjects.runtime.transaction.updatenotifier.UpdateNotifier;


/**
 * A reflection peer for executing actions remotely, instead of on the local machine. Any calls to
 * <code>execute</code> are passed over the network to the server for invocation. There are two cases where
 * the request is not passed to the server, ie it is executed locally: 1) where the method is static, ie is on
 * the class rather than an instance; 2) if the instance is not persistent; 3) if the method is marked as
 * 'local'. If a method is marked as being 'remote' then static methods and methods on transient objects will
 * be passed to the server.
 * 
 * <p>
 * If any of the objects involved have been changed on the server by another process then a
 * ConcurrencyException will be passed back to the client and re-thrown.
 * </p>
 */
public final class ActionInvocationFacetWrapProxy extends ActionInvocationFacetAbstract implements
        DecoratingFacet<ActionInvocationFacet> {

    private final static Logger LOG = Logger.getLogger(ActionInvocationFacetWrapProxy.class);
    private final ServerFacade serverFacade;
    private final ObjectEncoderDecoder encoder;
    private final ActionInvocationFacet underlyingFacet;
    private final NakedObjectAction nakedObjectAction;

    public ActionInvocationFacetWrapProxy(
            final ActionInvocationFacet underlyingFacet,
            final ServerFacade connection,
            final ObjectEncoderDecoder encoder,
            final NakedObjectAction nakedObjectAction) {
        super(underlyingFacet.getFacetHolder());
        this.underlyingFacet = underlyingFacet;
        this.serverFacade = connection;
        this.encoder = encoder;
        this.nakedObjectAction = nakedObjectAction;
    }

    public ActionInvocationFacet getDecoratedFacet() {
        return underlyingFacet;
    }

    public NakedObject invoke(final NakedObject target, final NakedObject[] parameters) {
        if (isToBeExecutedRemotely(target)) {
            /*
             * NOTE - only remotely executing actions on objects not collection - due to collections not
             * having OIDs yet
             */
            return executeRemotely(target, parameters);
        } else {
            LOG.debug(debug("execute locally", getIdentifier(), target, parameters));
            return underlyingFacet.invoke(target, parameters);
        }
    }

    public NakedObjectSpecification getReturnType() {
        return underlyingFacet.getReturnType();
    }

    public NakedObjectSpecification getOnType() {
        return underlyingFacet.getOnType();
    }

    public Identifier getIdentifier() {
        return nakedObjectAction.getIdentifier();
    }

    private NakedObject executeRemotely(final NakedObject targetAdapter, final NakedObject[] parameterAdapters) {
    	if (LOG.isDebugEnabled()) {
    		LOG.debug(debug("execute remotely", getIdentifier(), targetAdapter, parameterAdapters));
    	}
    	
        final KnownObjectsRequest knownObjects = new KnownObjectsRequest();
        final Data[] parameterObjectData = parameterValues(parameterAdapters, knownObjects);
        final ReferenceData targetReference = targetAdapter == null ? null : encoder.encodeActionTarget(targetAdapter, knownObjects);
        ExecuteServerActionResponse response;
        try {
            ExecuteServerActionRequest request = 
            	new ExecuteServerActionRequest(
            			getAuthenticationSession(), 
            			NakedObjectActionConstants.USER, 
            			nakedObjectAction.getIdentifier().toNameParmsIdentityString(),
            			targetReference, 
            			parameterObjectData);
			response = serverFacade.executeServerAction(request);
        } catch (final ConcurrencyException e) {
            final Oid source = e.getSource();
            if (source == null) {
                throw e;
            } else {
                final NakedObject failedObject = getAdapterManager().getAdapterFor(source);
                getPersistenceSession().reload(failedObject);
                if (LOG.isInfoEnabled()) {
                	LOG.info("concurrency conflict: " + e.getMessage());
                }
                throw new ConcurrencyException("Object automatically reloaded: " + failedObject.titleString(), e);
            }
        } catch (final NakedObjectException e) {
            LOG.error("remoting exception", e);
            throw e;
        }

        // must deal with transient-now-persistent objects first
        if (targetAdapter.isTransient()) {
            encoder.madePersistent(targetAdapter, response.getPersistedTarget());
        }

        final NakedObjectActionParameter[] parameters2 = nakedObjectAction.getParameters();
        for (int i = 0; i < parameterAdapters.length; i++) {
            if (parameters2[i].getSpecification().isObject()) {
                encoder.madePersistent(parameterAdapters[i], response.getPersistedParameters()[i]);
            }
        }

        final Data returned = response.getReturn();
        NakedObject returnedObject = returned instanceof NullData ? null : encoder.decode(returned);

        final ObjectData[] updates = response.getUpdates();
        for (int i = 0; i < updates.length; i++) {
        	if (LOG.isDebugEnabled()) {
        		LOG.debug("update " + updates[i].getOid());
        	}
            encoder.decode(updates[i]);
        }

        final ReferenceData[] disposed = response.getDisposed();
        for (int i = 0; i < disposed.length; i++) {
            final Oid oid = disposed[i].getOid();
            if (LOG.isDebugEnabled()) {
            	LOG.debug("disposed " + oid);
            }
            final NakedObject adapter = getAdapterManager().getAdapterFor(oid);
            getUpdateNotifier().addDisposedObject(adapter);
        }

        String[] messages = response.getMessages();
        for (int i = 0; i < messages.length; i++) {
            getMessageBroker().addMessage(messages[i]);
        }

        messages = response.getWarnings();
        for (int i = 0; i < messages.length; i++) {
            getMessageBroker().addWarning(messages[i]);
        }

        return returnedObject;
    }

    
    private boolean isToBeExecutedRemotely(final NakedObject targetAdapter) {
        final ExecutedFacet facet = nakedObjectAction.getFacet(ExecutedFacet.class);
        final boolean remoteOverride = (facet.value() == Where.REMOTELY);
        final boolean localOverride = (facet.value() == Where.LOCALLY); 

        if (localOverride) {
            return false;
        }

        if (remoteOverride) {
            return true;
        }

        if (targetAdapter.getSpecification().isService()) {
            return true;
        }

        if (targetAdapter == null) {
            // for static methods there is no target
            return false;
        }

        final boolean remoteAsPersistent = targetAdapter.isPersistent();
        return remoteAsPersistent;
    }

    private Data[] parameterValues(final NakedObject[] parameters, final KnownObjectsRequest knownObjects) {
        final NakedObjectSpecification[] parameterTypes = new NakedObjectSpecification[parameters.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            parameterTypes[i] = parameters[i].getSpecification();
        }
        return encoder.encodeActionParameters(parameterTypes, parameters, knownObjects);
    }

    private String debug(
            final String message,
            final Identifier identifier,
            final NakedObject target,
            final NakedObject[] parameters) {
        if (LOG.isDebugEnabled()) {
            final StringBuffer str = new StringBuffer();
            str.append(message);
            str.append(" ");
            str.append(identifier);
            str.append(" on ");
            str.append(target);
            for (int i = 0; i < parameters.length; i++) {
                if (i > 0) {
                    str.append(',');
                }
                str.append(parameters[i]);
            }
            return str.toString();
        } else {
            return "";
        }
    }

    ///////////////////////////////////////////////////////////
    // Dependencies (from context)
    ///////////////////////////////////////////////////////////
    
    private static PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }

    private static AdapterManager getAdapterManager() {
        return getPersistenceSession().getAdapterManager();
    }

    private static AuthenticationSession getAuthenticationSession() {
        return NakedObjectsContext.getAuthenticationSession();
    }

	protected static MessageBroker getMessageBroker() {
		return NakedObjectsContext.getMessageBroker();
	}

	protected static UpdateNotifier getUpdateNotifier() {
		return NakedObjectsContext.getUpdateNotifier();
	}



}
// Copyright (c) Naked Objects Group Ltd.
