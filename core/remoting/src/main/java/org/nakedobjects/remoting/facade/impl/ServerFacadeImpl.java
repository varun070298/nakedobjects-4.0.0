package org.nakedobjects.remoting.facade.impl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.exceptions.UnexpectedCallException;
import org.nakedobjects.metamodel.commons.exceptions.UnknownTypeException;
import org.nakedobjects.metamodel.config.ConfigurationConstants;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.facets.object.encodeable.EncodableFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionConstants;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.NakedObjectMember;
import org.nakedobjects.metamodel.spec.feature.OneToManyAssociation;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;
import org.nakedobjects.metamodel.spec.identifier.IdentifierFactory;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.metamodel.specloader.internal.NakedObjectActionImpl;
import org.nakedobjects.metamodel.util.CollectionFacetUtils;
import org.nakedobjects.remoting.NakedObjectsRemoteException;
import org.nakedobjects.remoting.client.transaction.ClientTransactionEvent;
import org.nakedobjects.remoting.data.Data;
import org.nakedobjects.remoting.data.common.EncodableObjectData;
import org.nakedobjects.remoting.data.common.IdentityData;
import org.nakedobjects.remoting.data.common.NullData;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.data.common.ReferenceData;
import org.nakedobjects.remoting.data.query.PersistenceQueryData;
import org.nakedobjects.remoting.exchange.AuthorizationRequestUsability;
import org.nakedobjects.remoting.exchange.AuthorizationRequestVisibility;
import org.nakedobjects.remoting.exchange.ClearAssociationRequest;
import org.nakedobjects.remoting.exchange.ClearValueRequest;
import org.nakedobjects.remoting.exchange.CloseSessionRequest;
import org.nakedobjects.remoting.exchange.ExecuteClientActionRequest;
import org.nakedobjects.remoting.exchange.ExecuteServerActionRequest;
import org.nakedobjects.remoting.exchange.FindInstancesRequest;
import org.nakedobjects.remoting.exchange.GetObjectRequest;
import org.nakedobjects.remoting.exchange.GetPropertiesRequest;
import org.nakedobjects.remoting.exchange.GetPropertiesResponse;
import org.nakedobjects.remoting.exchange.HasInstancesRequest;
import org.nakedobjects.remoting.exchange.OidForServiceRequest;
import org.nakedobjects.remoting.exchange.OpenSessionRequest;
import org.nakedobjects.remoting.exchange.OpenSessionResponse;
import org.nakedobjects.remoting.exchange.AuthorizationResponse;
import org.nakedobjects.remoting.exchange.ClearAssociationResponse;
import org.nakedobjects.remoting.exchange.ClearValueResponse;
import org.nakedobjects.remoting.exchange.CloseSessionResponse;
import org.nakedobjects.remoting.exchange.ExecuteClientActionResponse;
import org.nakedobjects.remoting.exchange.ExecuteServerActionResponse;
import org.nakedobjects.remoting.exchange.FindInstancesResponse;
import org.nakedobjects.remoting.exchange.GetObjectResponse;
import org.nakedobjects.remoting.exchange.HasInstancesResponse;
import org.nakedobjects.remoting.exchange.KnownObjectsRequest;
import org.nakedobjects.remoting.exchange.OidForServiceResponse;
import org.nakedobjects.remoting.exchange.ResolveFieldRequest;
import org.nakedobjects.remoting.exchange.ResolveFieldResponse;
import org.nakedobjects.remoting.exchange.ResolveObjectRequest;
import org.nakedobjects.remoting.exchange.ResolveObjectResponse;
import org.nakedobjects.remoting.exchange.SetAssociationRequest;
import org.nakedobjects.remoting.exchange.SetAssociationResponse;
import org.nakedobjects.remoting.exchange.SetValueRequest;
import org.nakedobjects.remoting.exchange.SetValueResponse;
import org.nakedobjects.remoting.facade.ServerFacade;
import org.nakedobjects.remoting.protocol.encoding.internal.ObjectEncoderDecoder;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.authentication.AuthenticationRequestPassword;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceConstants;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManager;
import org.nakedobjects.runtime.transaction.messagebroker.MessageBroker;
import org.nakedobjects.runtime.transaction.updatenotifier.UpdateNotifier;


/**
 * previously called <tt>ServerDistribution</tt>.
 */
public class ServerFacadeImpl implements ServerFacade {
    
    private static final Logger LOG = Logger.getLogger(ServerFacadeImpl.class);
    
    private final AuthenticationManager authenticationManager;
    private ObjectEncoderDecoder encoderDecoder;
    


    public ServerFacadeImpl(final AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    ////////////////////////////////////////////////////////////////
    // init, shutdown
    ////////////////////////////////////////////////////////////////

    public void init() {}

    public void shutdown() {}


    ////////////////////////////////////////////////////////////////
    // Authentication
    ////////////////////////////////////////////////////////////////

    public OpenSessionResponse openSession(OpenSessionRequest request2) {
        final AuthenticationRequestPassword request = new AuthenticationRequestPassword(request2.getUsername(), request2.getPassword());
        final AuthenticationSession session = authenticationManager.authenticate(request);
		return new OpenSessionResponse(session);
    }

    public CloseSessionResponse closeSession(CloseSessionRequest request) {
        authenticationManager.closeSession(request.getSession());
        return new CloseSessionResponse();
    }


    ////////////////////////////////////////////////////////////////
    // Authorization
    ////////////////////////////////////////////////////////////////

    public AuthorizationResponse authorizeVisibility(
    		AuthorizationRequestVisibility request) {
    	NakedObject targetAdapter = encoderDecoder.decode(request.getTarget());
        boolean allowed = getMember(request.getIdentifier()).isVisible(request.getSession(), targetAdapter).isAllowed();
		return encoderDecoder.encodeAuthorizeResponse(allowed);
    }

    public AuthorizationResponse authorizeUsability(
    		AuthorizationRequestUsability request) {
    	NakedObject targetAdapter = encoderDecoder.decode(request.getTarget());
        boolean allowed = getMember(request.getIdentifier()).isUsable(request.getSession(), targetAdapter).isAllowed();
        return encoderDecoder.encodeAuthorizeResponse(allowed);
    }

    private NakedObjectMember getMember(final String memberName) {
        final Identifier id = IdentifierFactory.fromIdentityString(memberName);
        final NakedObjectSpecification specification = getSpecificationLoader().loadSpecification(id.getClassName());
        if (id.isPropertyOrCollection()) {
            return getAssociationElseThrowException(id, specification);
        } else {
            return getActionElseThrowException(id, specification);
        }
    }

    private NakedObjectMember getActionElseThrowException(final Identifier id, final NakedObjectSpecification specification) {
        NakedObjectMember member = 
            specification.getObjectAction(
                    NakedObjectActionConstants.USER, id.getMemberName(), getMemberParameterSpecifications(id));
        if (member == null) {
            throw new NakedObjectException("No user action found for id " + id);
        }
        return member;
    }

    private NakedObjectMember getAssociationElseThrowException(final Identifier id, final NakedObjectSpecification specification) {
        NakedObjectMember member = specification.getAssociation(id.getMemberName());
        if (member == null) {
            throw new NakedObjectException("No property or collection found for id " + id);
        }
        return member;
    }

    private NakedObjectSpecification[] getMemberParameterSpecifications(final Identifier id) {
        final String[] parameters = id.getMemberParameterNames();
        final NakedObjectSpecification[] specifications = new NakedObjectSpecification[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            specifications[i] = getSpecificationLoader().loadSpecification(parameters[i]);
        }
        return specifications;
    }


    ////////////////////////////////////////////////////////////////
    // Misc
    ////////////////////////////////////////////////////////////////

    public GetPropertiesResponse getProperties(GetPropertiesRequest request) {
        final Properties properties = new Properties();
        properties.put("test-client", "true");

        // pass over services
        final NakedObjectConfiguration configuration = NakedObjectsContext.getConfiguration();
        final NakedObjectConfiguration serviceProperties = configuration.getProperties(ConfigurationConstants.ROOT + "services");
        final Enumeration e = serviceProperties.propertyNames();
        while (e.hasMoreElements()) {
            final String name = (String) e.nextElement();
            properties.put(name, serviceProperties.getString(name));
        }

        // pass over OID generator
        final String oidGeneratorClass = getPersistenceSession().getOidGenerator().getClass().getName();
        if (oidGeneratorClass != null) {
            properties.put(PersistenceConstants.OID_GENERATOR_CLASS_NAME, oidGeneratorClass);
        }

        // TODO load up client-side properties
        return new GetPropertiesResponse(properties);
    }


    ////////////////////////////////////////////////////////////////
    // setAssociation, setValue, clearAssociation, clearValue
    ////////////////////////////////////////////////////////////////

    /**
     * Applies to both {@link OneToOneAssociation}s and {@link OneToManyAssociation}s.
     */
    public SetAssociationResponse setAssociation(
            final SetAssociationRequest request) {
    	
    	AuthenticationSession session = request.getSession();
    	String fieldIdentifier = request.getFieldIdentifier();
    	IdentityData targetData = request.getTarget();
    	IdentityData associateData = request.getAssociate();
    	
        if (LOG.isDebugEnabled()) {
			LOG.debug("request setAssociation " + fieldIdentifier + " on " + targetData + " with " + associateData + " for " + session);
        }
        
        final NakedObject targetAdapter = getPersistentNakedObject(session, targetData);
        final NakedObject associate = getPersistentNakedObject(session, associateData);
        final NakedObjectAssociation association = targetAdapter.getSpecification().getAssociation(fieldIdentifier);
        
        ensureAssociationModifiableElseThrowException(session, targetAdapter, association);
        
        if (association instanceof OneToOneAssociation) {
            ((OneToOneAssociation) association).setAssociation(targetAdapter, associate);
        } else {
            ((OneToManyAssociation) association).addElement(targetAdapter, associate);
        }
        
        return new SetAssociationResponse(getUpdates());
    }

    /**
     * Applies only for {@link OneToOneAssociation}s.
     */
    public SetValueResponse setValue(
            SetValueRequest request) {
    	
    	AuthenticationSession session = request.getSession();
    	String fieldIdentifier = request.getFieldIdentifier();
    	IdentityData targetIdentityData = request.getTarget();
    	EncodableObjectData encodeableObjectData = request.getValue();

        Assert.assertNotNull(encodeableObjectData);
        if (LOG.isDebugEnabled()) {
            LOG.debug("request setValue " + fieldIdentifier + " on " + targetIdentityData + " with " + encodeableObjectData + " for " + session);
        }
        
        final NakedObject targetAdapter = getPersistentNakedObject(session, targetIdentityData);
        final OneToOneAssociation association = (OneToOneAssociation) targetAdapter.getSpecification().getAssociation(fieldIdentifier);
        
        ensureAssociationModifiableElseThrowException(session, targetAdapter, association);

        final String encodedObject = encodeableObjectData.getEncodedObjectData();

        final NakedObjectSpecification specification = association.getSpecification();
        final NakedObject adapter = restoreLeafObject(encodedObject, specification);
        association.setAssociation(targetAdapter, adapter);
        
        return new SetValueResponse(getUpdates());
    }


    /**
     * Applies to both {@link OneToOneAssociation}s and {@link OneToManyAssociation}s.
     */
    public ClearAssociationResponse clearAssociation(
            final ClearAssociationRequest request) {
    	
    	AuthenticationSession session = request.getSession();
    	String fieldIdentifier = request.getFieldIdentifier();
    	IdentityData targetData = request.getTarget();
    	IdentityData associateData = request.getAssociate();
    	
        if (LOG.isDebugEnabled()) {
            LOG.debug("request clearAssociation " + fieldIdentifier + " on " + targetData + " of " + associateData + " for " + session);
        }
        
        final NakedObject targetAdapter = getPersistentNakedObject(session, targetData);
        final NakedObject associateAdapter = getPersistentNakedObject(session, associateData);
        final NakedObjectSpecification specification = targetAdapter.getSpecification();
        final NakedObjectAssociation association = specification.getAssociation(fieldIdentifier);

        if (!association.isVisible(session, targetAdapter).isAllowed() || 
        	 association.isUsable(session, targetAdapter).isVetoed()) {
            throw new NakedObjectException("can't modify field as not visible or editable");
        }
        ensureAssociationModifiableElseThrowException(session, targetAdapter, association);

        if (association instanceof OneToOneAssociation) {
            ((OneToOneAssociation) association).clearAssociation(targetAdapter);
        } else {
            ((OneToManyAssociation) association).removeElement(targetAdapter, associateAdapter);
        }
        return new ClearAssociationResponse(getUpdates());
    }

    /**
     * Applies only for {@link OneToOneAssociation}s.
     */
    public ClearValueResponse clearValue(
    		final ClearValueRequest request) {
    	
    	AuthenticationSession session = request.getSession();
    	String fieldIdentifier = request.getFieldIdentifier();
    	IdentityData targetIdentityData = request.getTarget();

    	if (LOG.isDebugEnabled()) {
    		LOG.debug("request clearValue " + fieldIdentifier + " on " + targetIdentityData + " for " + session);
    	}
    	
        final NakedObject targetAdapter = getPersistentNakedObject(session, targetIdentityData);
        final OneToOneAssociation association = (OneToOneAssociation) targetAdapter.getSpecification().getAssociation(fieldIdentifier);

        ensureAssociationModifiableElseThrowException(session, targetAdapter, association);

        association.clearAssociation(targetAdapter);
        return new ClearValueResponse(getUpdates());
    }

	private void ensureAssociationModifiableElseThrowException(
			final AuthenticationSession session,
			final NakedObject targetAdapter,
			final NakedObjectAssociation association) {
		if (!association.isVisible(session, targetAdapter).isAllowed() || 
        	 association.isUsable(session, targetAdapter).isVetoed()) {
            throw new NakedObjectException("can't modify field as not visible or editable");
        }
	}


    ////////////////////////////////////////////////////////////////
    // executeClientAction
    ////////////////////////////////////////////////////////////////

    public ExecuteClientActionResponse executeClientAction(
    		ExecuteClientActionRequest request) {
    	
    	AuthenticationSession session = request.getSession();
    	ReferenceData[] data = request.getData();
    	int[] types = request.getTypes();

        if (LOG.isDebugEnabled()) {
            LOG.debug("execute client action for " + session);
            LOG.debug("start transaction");
        }
        
        // although the PersistenceSession will also do xactn mgmt, because we
        // are potentially modifying several objects we should explicitly define
        // the xactn at this level.
        getTransactionManager().startTransaction();
        try {
            final KnownObjectsRequest knownObjects = new KnownObjectsRequest();
            final NakedObject[] persistedObjects = new NakedObject[data.length];
            final NakedObject[] disposedObjects = new NakedObject[data.length];
            final NakedObject[] changedObjects = new NakedObject[data.length];
            for (int i = 0; i < data.length; i++) {
                NakedObject object;
                switch (types[i]) {
                case ClientTransactionEvent.ADD:
                    object = encoderDecoder.decode(data[i], knownObjects);
                    persistedObjects[i] = object;
                    if (object.getOid().isTransient()) { // confirm that the graph has not been made
                        // persistent earlier in this loop
                        LOG.debug("  makePersistent " + data[i]);
                        getPersistenceSession().makePersistent(object);
                    }
                    break;

                case ClientTransactionEvent.CHANGE:
                    final NakedObject obj = getPersistentNakedObject(data[i]);
                    obj.checkLock(data[i].getVersion());

                    object = encoderDecoder.decode(data[i], knownObjects);
                    LOG.debug("  objectChanged " + data[i]);
                    getPersistenceSession().objectChanged(object);
                    changedObjects[i] = object;
                    break;

                case ClientTransactionEvent.DELETE:
                    final NakedObject inObject = getPersistentNakedObject(data[i]);
                    inObject.checkLock(data[i].getVersion());

                    LOG.debug("  destroyObject " + data[i] + " for " + session);
                    disposedObjects[i] = inObject;
                    getPersistenceSession().destroyObject(inObject);
                    break;
                }

            }

            if (LOG.isDebugEnabled()) {
            	LOG.debug("  end transaction");
            }
            getTransactionManager().endTransaction();

            final ReferenceData[] madePersistent = new ReferenceData[data.length];
            final Version[] changedVersion = new Version[data.length];

            for (int i = 0; i < data.length; i++) {
                switch (types[i]) {
                case ClientTransactionEvent.ADD:
                    madePersistent[i] = encoderDecoder.encodeIdentityData(persistedObjects[i]);
                    break;

                case ClientTransactionEvent.CHANGE:
                    changedVersion[i] = changedObjects[i].getVersion();
                    break;

                }
            }

            return encoderDecoder.encodeClientActionResult(madePersistent, changedVersion, getUpdates());
        } catch (final RuntimeException e) {
            LOG.info("abort transaction", e);
            getTransactionManager().abortTransaction();
            throw e;
        }
    }


    ////////////////////////////////////////////////////////////////
    // executeServerAction
    ////////////////////////////////////////////////////////////////

    public ExecuteServerActionResponse executeServerAction(
            ExecuteServerActionRequest request) {
    	
    	AuthenticationSession session = request.getSession();
    	NakedObjectActionType actionType = request.getActionType();
    	String actionIdentifier = request.getActionIdentifier();
    	ReferenceData targetData = request.getTarget();
    	Data[] parameterData = request.getParameters();

        if (LOG.isDebugEnabled()) {
            LOG.debug("request executeAction " + actionIdentifier + " on " + targetData + " for " + session);
        }

        NakedObject targetAdapter;
        final KnownObjectsRequest knownObjects = new KnownObjectsRequest();
        if (targetData instanceof IdentityData) {
            targetAdapter = getPersistentNakedObject(session, (IdentityData) targetData);
        } else if (targetData instanceof ObjectData) {
            targetAdapter = encoderDecoder.decode(targetData, knownObjects);
        } else if (targetData == null) {
            targetAdapter = null;
        } else {
            throw new NakedObjectException();
        }

        final NakedObjectAction action = 
        	targetAdapter.getSpecification().getObjectAction(actionType, actionIdentifier);
        final NakedObject[] parameters = getParameters(session, parameterData, knownObjects);

        if (action == null) {
            throw new NakedObjectsRemoteException("Could not find method " + actionIdentifier);
        }

        final NakedObject resultAdapter = action.execute(targetAdapter, parameters);

        ObjectData persistedTargetData;
        if (targetData == null) {
            persistedTargetData = null;
        } else if (targetData instanceof ObjectData) {
            persistedTargetData = encoderDecoder.encodeMadePersistentGraph((ObjectData) targetData, targetAdapter);
        } else {
            persistedTargetData = null;
        }

        final ObjectData[] persistedParameterData = new ObjectData[parameterData.length];
        for (int i = 0; i < persistedParameterData.length; i++) {
            if (action.getParameters()[i].getSpecification().isObject() && parameterData[i] instanceof ObjectData) {
                persistedParameterData[i] = encoderDecoder.encodeMadePersistentGraph((ObjectData) parameterData[i], parameters[i]);
            }
        }
        final List<String> messages = getMessageBroker().getMessages();
        final List<String> warnings = getMessageBroker().getWarnings();

        // TODO for efficiency, need to remove the objects in the results graph from the updates set
        return encoderDecoder.encodeServerActionResult(
        		resultAdapter, getUpdates(), getDisposed(), persistedTargetData, persistedParameterData,
                messages.toArray(new String[0]), warnings.toArray(new String[0]));
    }

    private NakedObjectAction getActionMethod(
            final String actionType,
            final String actionIdentifier,
            final Data[] parameterData,
            final NakedObject adapter) {
        final NakedObjectSpecification[] parameterSpecs = new NakedObjectSpecification[parameterData.length];
        for (int i = 0; i < parameterSpecs.length; i++) {
            parameterSpecs[i] = getSpecification(parameterData[i].getType());
        }

        final NakedObjectActionType type = NakedObjectActionImpl.getType(actionType);

        final int pos = actionIdentifier.indexOf('#');
        @SuppressWarnings("unused")
		final String className = actionIdentifier.substring(0, pos);
        final String methodName = actionIdentifier.substring(pos + 1);

        if (adapter == null) {
            throw new UnexpectedCallException("object not specified");
        }
        return adapter.getSpecification().getObjectAction(type, methodName, parameterSpecs);
    }

    private NakedObject[] getParameters(final AuthenticationSession session, final Data[] parameterData, final KnownObjectsRequest knownObjects) {
        final NakedObject[] parameters = new NakedObject[parameterData.length];
        for (int i = 0; i < parameters.length; i++) {
            final Data data = parameterData[i];
            if (data instanceof NullData) {
                continue;
            }

            if (data instanceof IdentityData) {
                parameters[i] = getPersistentNakedObject(session, (IdentityData) data);
            } else if (data instanceof ObjectData) {
                parameters[i] = encoderDecoder.decode(data, knownObjects);
            } else if (data instanceof EncodableObjectData) {
                final NakedObjectSpecification valueSpecification = getSpecificationLoader().loadSpecification(
                        data.getType());
                final String valueData = ((EncodableObjectData) data).getEncodedObjectData();

                final NakedObject value = restoreLeafObject(valueData, valueSpecification);
                /*
                 * NakedValue value =
                 * NakedObjectsContext.getObjectLoader().createValueInstance(valueSpecification);
                 * value.restoreFromEncodedString(valueData);
                 */
                parameters[i] = value;
            } else {
                throw new UnknownTypeException(data);
            }
        }
        return parameters;
    }

    private ReferenceData[] getDisposed() {
    	final List<ReferenceData> list = new ArrayList<ReferenceData>();
        for(NakedObject element: getUpdateNotifier().getDisposedObjects()) {
            list.add(encoderDecoder.encodeIdentityData(element));
        }
        return (ReferenceData[]) list.toArray(new ReferenceData[list.size()]);
    }


    ////////////////////////////////////////////////////////////////
    // getObject, resolve
    ////////////////////////////////////////////////////////////////

    public GetObjectResponse getObject(
    		GetObjectRequest request) {
    	
    	Oid oid = request.getOid();
    	String specificationName = request.getSpecificationName();

        final NakedObjectSpecification specification = getSpecification(specificationName);
        final NakedObject adapter = getPersistenceSession().loadObject(oid, specification);
        
        return new GetObjectResponse(encoderDecoder.encodeForUpdate(adapter));
    }

    public ResolveFieldResponse resolveField(
    		ResolveFieldRequest request) {
    	
    	AuthenticationSession session = request.getSession();
    	IdentityData targetData = request.getTarget();
    	String fieldIdentifier = request.getFieldIdentifier();

        if (LOG.isDebugEnabled()) {
            LOG.debug("request resolveField " + targetData + "/" + fieldIdentifier + " for " + session);
        }

        final NakedObjectSpecification spec = getSpecification(targetData.getType());
        final NakedObjectAssociation field = spec.getAssociation(fieldIdentifier);
        final NakedObject targetAdapter = getPersistenceSession().recreateAdapter(targetData.getOid(), spec);
        
        getPersistenceSession().resolveField(targetAdapter, field);
        Data data = encoderDecoder.encodeForResolveField(targetAdapter, fieldIdentifier);
		return new ResolveFieldResponse(data);
    }

    public ResolveObjectResponse resolveImmediately(
    		ResolveObjectRequest request) {
    	
    	AuthenticationSession session = request.getSession();
    	IdentityData targetData = request.getTarget();

    	
        if (LOG.isDebugEnabled()) {
            LOG.debug("request resolveImmediately " + targetData + " for " + session);
        }

        final NakedObjectSpecification spec = getSpecification(targetData.getType());
        final NakedObject object = getPersistenceSession().loadObject(targetData.getOid(), spec);
        
        if (object.getResolveState().canChangeTo(ResolveState.RESOLVING)) {
            // this is need when the object store does not load the object fully in the getObject() above
            getPersistenceSession().resolveImmediately(object);
        }

        return new ResolveObjectResponse(encoderDecoder.encodeCompletePersistentGraph(object));
    }

    
    ////////////////////////////////////////////////////////////////
    // findInstances, hasInstances
    ////////////////////////////////////////////////////////////////

    public FindInstancesResponse findInstances(
    		final FindInstancesRequest request) {
    	
    	AuthenticationSession session = request.getSession();
    	PersistenceQueryData criteriaData = request.getCriteria();
    	
        final PersistenceQuery criteria = encoderDecoder.decodePersistenceQuery(criteriaData);
        LOG.debug("request findInstances " + criteria + " for " + session);
        final NakedObject instances = getPersistenceSession().findInstances(criteria);
        ObjectData[] instancesData = convertToNakedCollection(instances);
		return new FindInstancesResponse(instancesData);
    }

    public HasInstancesResponse hasInstances(
    		HasInstancesRequest request) {
    	
    	AuthenticationSession session = request.getSession();
    	String specificationName = request.getSpecificationName();

    	if (LOG.isDebugEnabled()) {
    		LOG.debug("request hasInstances of " + specificationName + " for " + session);
    	}
        boolean hasInstances = getPersistenceSession().hasInstances(getSpecification(specificationName));
		return new HasInstancesResponse(hasInstances);
    }

    private ObjectData[] convertToNakedCollection(final NakedObject instances) {
        final CollectionFacet facet = CollectionFacetUtils.getCollectionFacetFromSpec(instances);
        final ObjectData[] data = new ObjectData[facet.size(instances)];
        final Enumeration elements = facet.elements(instances);
        int i = 0;
        while (elements.hasMoreElements()) {
            final NakedObject element = (NakedObject) elements.nextElement();
            data[i++] = encoderDecoder.encodeCompletePersistentGraph(element);
        }
        return data;
    }

    ////////////////////////////////////////////////////////////////
    // oidForService
    ////////////////////////////////////////////////////////////////

    public OidForServiceResponse oidForService(
    		OidForServiceRequest request) {
    	
    	String serviceId = request.getServiceId();

        final NakedObject serviceAdapter = getPersistenceSession().getService(serviceId);
        if (serviceAdapter == null) {
            throw new NakedObjectsRemoteException("Failed to find service " + serviceId);
        }
        return new OidForServiceResponse(
        		encoderDecoder.encodeIdentityData(serviceAdapter));
    }

    
    ////////////////////////////////////////////////////////////////
    // Helpers
    ////////////////////////////////////////////////////////////////

    private NakedObjectSpecification getSpecification(final String fullName) {
        return getSpecificationLoader().loadSpecification(fullName);
    }

    private NakedObject getPersistentNakedObject(final AuthenticationSession session, final IdentityData object) {
        final NakedObject obj = getPersistentNakedObject(object);
        if (LOG.isDebugEnabled()) {
            LOG.debug("get object " + object + " for " + session + " --> " + obj);
        }
        obj.checkLock(object.getVersion());
        return obj;
    }

    private NakedObject getPersistentNakedObject(final ReferenceData object) {
        final NakedObjectSpecification spec = getSpecification(object.getType());
        final NakedObject obj = getPersistenceSession().loadObject(object.getOid(), spec);
        Assert.assertNotNull(obj);
        return obj;
    }

    private NakedObject restoreLeafObject(final String encodedObject, final NakedObjectSpecification specification) {
        final EncodableFacet encoder = specification.getFacet(EncodableFacet.class);
        if (encoder == null) {
            throw new NakedObjectException("No encoder for " + specification.getFullName());
        }
        final NakedObject object = encoder.fromEncodedString(encodedObject);
        return object;
    }

    private ObjectData[] getUpdates() {
    	final List<ObjectData> list = new ArrayList<ObjectData>();
    	for(NakedObject element: getUpdateNotifier().getChangedObjects()) {
    		list.add(encoderDecoder.encodeForUpdate(element));	
    	}
        return (ObjectData[]) list.toArray(new ObjectData[list.size()]);
    }

    
    ////////////////////////////////////////////////////////////////
    // Dependencies (injected)
    ////////////////////////////////////////////////////////////////

    public void setEncoder(final ObjectEncoderDecoder objectEncoder) {
        this.encoderDecoder = objectEncoder;
    }


    ////////////////////////////////////////////////////////////////
    // Dependencies (from context)
    ////////////////////////////////////////////////////////////////
    
    private static SpecificationLoader getSpecificationLoader() {
        return NakedObjectsContext.getSpecificationLoader();
    }

    private static PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }

    private static NakedObjectTransactionManager getTransactionManager() {
        return getPersistenceSession().getTransactionManager();
    }
    
    private static UpdateNotifier getUpdateNotifier() {
        return NakedObjectsContext.getUpdateNotifier();
    }

    private static MessageBroker getMessageBroker() {
        return NakedObjectsContext.getMessageBroker();
    }



}
// Copyright (c) Naked Objects Group Ltd.
