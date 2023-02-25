package org.nakedobjects.remoting.protocol.encoding.internal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.metamodel.commons.exceptions.UnknownTypeException;
import org.nakedobjects.metamodel.commons.factory.InstanceFactory;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.util.CollectionFacetUtils;
import org.nakedobjects.remoting.NakedObjectsRemoteException;
import org.nakedobjects.remoting.client.facets.ActionInvocationFacetWrapProxy;
import org.nakedobjects.remoting.client.facets.PropertySetterFacetWrapProxy;
import org.nakedobjects.remoting.client.persistence.ClientSideTransactionManager;
import org.nakedobjects.remoting.client.persistence.PersistenceSessionProxy;
import org.nakedobjects.remoting.data.Data;
import org.nakedobjects.remoting.data.DataFactory;
import org.nakedobjects.remoting.data.DataFactoryDefault;
import org.nakedobjects.remoting.data.common.CollectionData;
import org.nakedobjects.remoting.data.common.EncodableObjectData;
import org.nakedobjects.remoting.data.common.IdentityData;
import org.nakedobjects.remoting.data.common.NullData;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.data.common.ReferenceData;
import org.nakedobjects.remoting.data.query.PersistenceQueryData;
import org.nakedobjects.remoting.exchange.AuthorizationResponse;
import org.nakedobjects.remoting.exchange.ClearAssociationRequest;
import org.nakedobjects.remoting.exchange.ClearValueRequest;
import org.nakedobjects.remoting.exchange.ExecuteClientActionRequest;
import org.nakedobjects.remoting.exchange.ExecuteClientActionResponse;
import org.nakedobjects.remoting.exchange.ExecuteServerActionRequest;
import org.nakedobjects.remoting.exchange.ExecuteServerActionResponse;
import org.nakedobjects.remoting.exchange.FindInstancesRequest;
import org.nakedobjects.remoting.exchange.GetObjectRequest;
import org.nakedobjects.remoting.exchange.KnownObjectsRequest;
import org.nakedobjects.remoting.exchange.ResolveFieldRequest;
import org.nakedobjects.remoting.exchange.ResolveObjectRequest;
import org.nakedobjects.remoting.exchange.SetAssociationRequest;
import org.nakedobjects.remoting.exchange.SetValueRequest;
import org.nakedobjects.remoting.facade.impl.ServerFacadeImpl;
import org.nakedobjects.remoting.protocol.encoding.EncodingProtocolConstants;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistorUtil;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;

public class ObjectEncoderDecoderDefault implements ObjectEncoderDecoder {
	
	private final static Logger LOG = Logger.getLogger(ObjectEncoderDecoderDefault.class);
	
	public static final int DEFAULT_CLIENT_SIDE_ADD_OBJECT_GRAPH_DEPTH = 1;
	public static final int DEFAULT_CLIENT_SIDE_UPDATE_OBJECT_GRAPH_DEPTH = 1;
	public static final int DEFAULT_CLIENT_SIDE_ACTION_TARGET_GRAPH_DEPTH = 0;
	public static final int DEFAULT_CLIENT_SIDE_ACTION_PARAMETER_GRAPH_DEPTH = 0;
	
	public static final int DEFAULT_SERVER_SIDE_RETRIEVED_OBJECT_GRAPH_DEPTH = 100;
	public static final int DEFAULT_SERVER_SIDE_TOUCHED_OBJECT_GRAPH_DEPTH = 1;

    private final ObjectSerializer serializer;
    private final ObjectDeserializer deserializer;
    private final FieldOrderCache fieldOrderCache;
    private final DataFactory dataFactory;
    
    private final Map<Class<?>, PersistenceQueryEncoder> persistenceEncoderByClass = new HashMap<Class<?>, PersistenceQueryEncoder>();
    
    private int clientSideAddGraphDepth = DEFAULT_CLIENT_SIDE_ADD_OBJECT_GRAPH_DEPTH;
    private int clientSideUpdateGraphDepth = DEFAULT_CLIENT_SIDE_UPDATE_OBJECT_GRAPH_DEPTH;
    private int clientSideActionTargetRemotelyGraphDepth = DEFAULT_CLIENT_SIDE_ACTION_TARGET_GRAPH_DEPTH;
    private int clientSideActionParameterGraphDepth = DEFAULT_CLIENT_SIDE_ACTION_PARAMETER_GRAPH_DEPTH;
    
    private int serverSideTouchedObjectGraphDepth = DEFAULT_SERVER_SIDE_TOUCHED_OBJECT_GRAPH_DEPTH;
    private int serverSideRetrievedObjectGraphDepth = DEFAULT_SERVER_SIDE_RETRIEVED_OBJECT_GRAPH_DEPTH;


    /**
     * Factory method.
     */
	public static ObjectEncoderDecoderDefault create(final NakedObjectConfiguration configuration) {
		
		ObjectEncoderDecoderDefault encoderDecoder = new ObjectEncoderDecoderDefault();
		addPersistenceEncoders(configuration, encoderDecoder, EncodingProtocolConstants.ENCODER_CLASS_NAME_LIST);
		addPersistenceEncoders(configuration, encoderDecoder, EncodingProtocolConstants.ENCODER_CLASS_NAME_LIST_DEPRECATED);
		return encoderDecoder;
	}
	
	private static void addPersistenceEncoders(
			final NakedObjectConfiguration configuration,
			final ObjectEncoderDecoderDefault encoder, String encoderClassNameList) {
		String[] encoders = configuration.getList(encoderClassNameList);
	    for (int i = 0; i < encoders.length; i++) {
	        final PersistenceQueryEncoder encoding = InstanceFactory.createInstance(encoders[i], PersistenceQueryEncoder.class);
	        encoder.addPersistenceQueryEncoder(encoding);
	    }
	}

	/**
	 * Package-level visibility (for tests to use only)
	 */
    public ObjectEncoderDecoderDefault(){
    	this.fieldOrderCache = new FieldOrderCache();
    	this.dataFactory = new DataFactoryDefault();
        this.serializer = new ObjectSerializer(dataFactory, fieldOrderCache);
        this.deserializer = new ObjectDeserializer(fieldOrderCache);

        
        addPersistenceQueryEncoder(new PersistenceQueryFindAllInstancesEncoder());
        addPersistenceQueryEncoder(new PersistenceQueryFindByTitleEncoder());
        addPersistenceQueryEncoder(new PersistenceQueryFindByPatternEncoder());
        addPersistenceQueryEncoder(new PersistenceQueryFindUsingApplibQueryDefaultEncoder());
        addPersistenceQueryEncoder(new PersistenceQueryFindUsingApplibQuerySerializableEncoder());
        
        // TODO: look up overrides of depths from Configuration.
    }
    
    public void addPersistenceQueryEncoder(final PersistenceQueryEncoder encoder) {
    	encoder.setObjectEncoder(this);
        persistenceEncoderByClass.put(encoder.getPersistenceQueryClass(), encoder);
    }

    
    ///////////////////////////////////////////////////////////
    // called both client- and server-side only
    ///////////////////////////////////////////////////////////

    /**
     * Creates a ReferenceData that contains the type, version and OID for the specified object. This can only
     * be used for persistent objects.
     * 
     * <p>
     * Called both client and server-side, in multiple locations. 
     */
    public final IdentityData encodeIdentityData(final NakedObject object) {
        Assert.assertNotNull("OID needed for reference", object, object.getOid());
        return dataFactory.createIdentityData(object.getSpecification().getFullName(), object.getOid(), object.getVersion());
    }


    ///////////////////////////////////////////////////////////
    // client-side encoding
    ///////////////////////////////////////////////////////////

    /**
     * Called client-side only:
     * <ul>
     * <li>by {@link ClientSideTransactionManager#endTransaction()}
     * </ul>
     */
    public ObjectData encodeMakePersistentGraph(final NakedObject adapter, final KnownObjectsRequest knownObjects) {
        Assert.assertTrue("transient", adapter.isTransient());
        return (ObjectData) encode(adapter, clientSideAddGraphDepth, knownObjects);
    }

    /**
     * Called client-side only:
     * <ul>
     * <li>by {@link ClientSideTransactionManager#endTransaction()}
     * </ul>
     */
    public ObjectData encodeGraphForChangedObject(final NakedObject object, final KnownObjectsRequest knownObjects) {
        return (ObjectData) encode(object, clientSideUpdateGraphDepth, knownObjects);
    }

    /**
     * Called client-side only:
     * <ul>
     * <li>by {@link PropertySetterFacetWrapProxy#setProperty(NakedObject, NakedObject)}
     * </ul>
     */
    public EncodableObjectData encodeAsValue(final NakedObject value) {
        return serializer.serializeEncodeable(value);
    }

    /**
     * Called client-side only:
     * <ul>
     * <li> by {@link ActionInvocationFacetWrapProxy#invoke(NakedObject, NakedObject[])} (calling remotely)
     * </ul>
     */
    public ReferenceData encodeActionTarget(final NakedObject target, final KnownObjectsRequest knownObjects) {
        return serializer.serializeAdapter(target, clientSideActionTargetRemotelyGraphDepth, knownObjects);
    }

    /**
     * Called client-side only:
     * <ul>
     * <li>by {@link ActionInvocationFacetWrapProxy#invoke(NakedObject, NakedObject[])}
     * <li>by {@link PersistenceQueryFindByPatternEncoder#encode(PersistenceQuery)}
     * <li>by hibernate's equivalent encoder
     * </ul>
     */
    public final Data[] encodeActionParameters(
            final NakedObjectSpecification[] parameterTypes,
            final NakedObject[] parameters,
            final KnownObjectsRequest knownObjects) {
        final Data parameterData[] = new Data[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            final NakedObject parameter = parameters[i];
            final String type = parameterTypes[i].getFullName();
            parameterData[i] = createParameter(type, parameter, knownObjects);
        }
        return parameterData;
    }

    /**
     * Called client-side only:
     * <ul>
     * <li>by {@link PersistenceSessionProxy#findInstances(PersistenceQuery)}
     * </ul>
     */
    public PersistenceQueryData encodePersistenceQuery(final PersistenceQuery criteria) {
        final PersistenceQueryEncoder strategy = findPersistenceQueryEncoder(criteria.getClass());
        return strategy.encode(criteria);
    }

    
    ///////////////////////////////////////////////////////////
    // client-side decoding
    ///////////////////////////////////////////////////////////

    /**
     * Called client-side only:
     * <ul>
     * <li>by {@link ActionInvocationFacetWrapProxy#invoke(NakedObject, NakedObject[])}
     * </ul>
     */
    public void madePersistent(final NakedObject target, final ObjectData persistedTarget) {
        deserializer.madePersistent(target, persistedTarget);
    }

    /**
     * Called client-side only, in multiple locations.
     */
    public NakedObject decode(final Data data) {
        return deserializer.deserialize(data);
    }

    public void decode(final ObjectData[] dataArray) {
        for (int i = 0; i < dataArray.length; i++) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("update " + dataArray[i].getOid());
			}
		    this.decode(dataArray[i]);
		}
    }


    ///////////////////////////////////////////////////////////
    // server-side decoding
    ///////////////////////////////////////////////////////////

    /**
     * Called server-side only:
     * <ul>
     * <li>by {@link ServerFacadeImpl#executeClientAction(ExecuteClientActionRequest)}
     * <li>by {@link ServerFacadeImpl#executeServerAction(ExecuteServerActionRequest)
     * </ul>
     */
    public NakedObject decode(final Data data, final KnownObjectsRequest knownObjects) {
        return deserializer.deserialize(data, knownObjects);
    }

    /**
     * Called server-side only:
     * <ul>
     * <li>by {@link ServerFacadeImpl#findInstances(FindInstancesRequest)
     * </ul>
     */
    public PersistenceQuery decodePersistenceQuery(final PersistenceQueryData persistenceQueryData) {
        final Class<?> criteriaClass = persistenceQueryData.getPersistenceQueryClass();
        final PersistenceQueryEncoder encoderDecoder = findPersistenceQueryEncoder(criteriaClass);
        return encoderDecoder.decode(persistenceQueryData);
    }

    private PersistenceQueryEncoder findPersistenceQueryEncoder(final Class<?> persistenceQueryClass) {
        final PersistenceQueryEncoder encoder = persistenceEncoderByClass.get(persistenceQueryClass);
        if (encoder == null) {
            throw new NakedObjectsRemoteException("No encoder for " + persistenceQueryClass.getName());
        }
        return encoder;
    }


    ///////////////////////////////////////////////////////////
    // server-side encoding
    ///////////////////////////////////////////////////////////

	public AuthorizationResponse encodeAuthorizeResponse(boolean authorized) {
		return new AuthorizationResponse(authorized);
	}

    /**
     * Called server-side only:
     * <ul>
     * <li> by {@link ServerFacadeImpl#executeClientAction(ExecuteClientActionRequest)}
     * </ul>
     */
    public ExecuteClientActionResponse encodeClientActionResult(
            final ReferenceData[] madePersistent,
            final Version[] changedVersion,
            final ObjectData[] updates) {
    	return new ExecuteClientActionResponse(madePersistent, changedVersion, updates);
    }

    /**
     * Encodes a complete set of data for the specified object.
     * 
     * <p>
     * Called server-side only, in several locations:
     * <ul>
     * <li> by {@link ServerFacadeImpl#findInstances(FindInstancesRequest)}
     * <li> by {@link ServerFacadeImpl#executeServerAction(ExecuteServerActionRequest)}
     * <li> by {@link ServerFacadeImpl#resolveImmediately(ResolveObjectRequest)}
     * </ul>
     */
    public final ObjectData encodeCompletePersistentGraph(final NakedObject object) {
        return encode(object, serverSideRetrievedObjectGraphDepth);
    }

    /**
     * Encodes a minimal set of data for the specified object.
     * 
     * <p>
     * Called server-side only:
     * <ul>
     * <li>by {@link ServerFacadeImpl#getObject(GetObjectRequest)}
     * <li>by {@link ServerFacadeImpl#clearAssociation(ClearAssociationRequest)
     * <li>by {@link ServerFacadeImpl#clearValue(ClearValueRequest)
     * <li>by {@link ServerFacadeImpl#executeClientAction(ExecuteClientActionRequest)
     * <li>by {@link ServerFacadeImpl#executeServerAction(ExecuteServerActionRequest)
     * <li>by {@link ServerFacadeImpl#setAssociation(SetAssociationRequest)
     * <li>by {@link ServerFacadeImpl#setValue(SetValueRequest)
     * </ul>
     */
    public ObjectData encodeForUpdate(final NakedObject object) {
        final ResolveState resolveState = object.getResolveState();
        if (resolveState.isSerializing() || resolveState.isGhost()) {
            throw new NakedObjectsRemoteException("Illegal resolve state: " + object);
        }
        return encode(object, serverSideTouchedObjectGraphDepth);
    }

    /**
     * Called server-side only:
     * <ul>
     * <li>by {@link ServerFacadeImpl#resolveField(ResolveFieldRequest)}
     * </ul>
     */
    public Data encodeForResolveField(final NakedObject adapter, final String fieldName) {
        final Oid oid = adapter.getOid();
        final NakedObjectSpecification specification = adapter.getSpecification();
        final String type = specification.getFullName();
        final ResolveState resolveState = adapter.getResolveState();

        Data[] fieldContent;
        final NakedObjectAssociation[] fields = getFieldOrder(specification);
        fieldContent = new Data[fields.length];

        PersistorUtil.start(adapter, adapter.getResolveState().serializeFrom());
        final KnownObjectsRequest knownObjects = new KnownObjectsRequest();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getId().equals(fieldName)) {
                final NakedObject field = fields[i].get(adapter);
                if (field == null) {
                    fieldContent[i] = dataFactory.createNullData(fields[i].getSpecification().getFullName());
                } else if (fields[i].getSpecification().isEncodeable()) {
                    fieldContent[i] = serializer.serializeEncodeable(field);
                } else if (fields[i].isOneToManyAssociation()) {
                    fieldContent[i] = serializer.serializeCollection(field, serverSideRetrievedObjectGraphDepth, knownObjects);
                } else {
                    NakedObjectsContext.getPersistenceSession().resolveImmediately(field);
                    fieldContent[i] = serializer.serializeAdapter(field, serverSideRetrievedObjectGraphDepth, knownObjects);
                }
                break;
            }
        }
        PersistorUtil.end(adapter);

        // TODO remove the fudge - needed as collections are part of parents, hence parent object gets set as
        // resolving (is not a ghost) yet it has no version number
        // return createObjectData(oid, type, fieldContent, resolveState.isResolved(),
        // !resolveState.isGhost(), object.getVersion());
        final ObjectData data = dataFactory.createObjectData(type, oid, resolveState.isResolved(), adapter.getVersion());
        data.setFieldContent(fieldContent);
        return data;
        // return createObjectData(oid, type, fieldContent, resolveState.isResolved(), object.getVersion());
    }

    /**
     * Called server-side only:
     * <ul>
     * <li>by {@link ServerFacadeImpl#executeServerAction(ExecuteServerActionRequest)}
     * </ul>
     */
    public ObjectData encodeMadePersistentGraph(final ObjectData data, final NakedObject object) {
        final Oid objectsOid = object.getOid();
        Assert.assertNotNull(objectsOid);
        if (objectsOid.hasPrevious()) {
            final Version version = object.getVersion();
            final String type = data.getType();
            final ObjectData persistedData = dataFactory.createObjectData(type, objectsOid, true, version);

            final Data[] allContents = data.getFieldContent();
            if (allContents != null) {
                final int contentLength = allContents.length;
                final Data persistentContents[] = new Data[contentLength];
                final NakedObjectAssociation[] fields = getFieldOrder(object.getSpecification());
                for (int i = 0; i < contentLength; i++) {
                    final Data fieldData = allContents[i];
                    if (fieldData instanceof NullData) {
                        persistentContents[i] = null;
                    } else if (fields[i].isOneToOneAssociation()) {
                        if (fieldData instanceof ObjectData) {
                            final NakedObject fieldReference = fields[i].get(object);
                            persistentContents[i] = encodeMadePersistentGraph((ObjectData) fieldData, fieldReference);
                        } else {
                            persistentContents[i] = null;
                        }
                    } else if (fields[i].isOneToManyAssociation()) {
                        final NakedObject fieldReference = fields[i].get(object);
                        persistentContents[i] = createMadePersistentCollection((CollectionData) fieldData, fieldReference);
                    }
                }
                persistedData.setFieldContent(persistentContents);
            }

            return persistedData;
        } else {
            return null;
        }
    }

    private Data createMadePersistentCollection(final CollectionData collectionData, final NakedObject collection) {
        final ReferenceData[] elementData = collectionData.getElements();
        final CollectionFacet facet = CollectionFacetUtils.getCollectionFacetFromSpec(collection);
        final Iterator elements = facet.iterator(collection);
        for (int i = 0; i < elementData.length; i++) {
            final NakedObject element = (NakedObject) elements.next();
            final Oid oid = element.getOid();
            Assert.assertNotNull(oid);
            elementData[i] = encodeMadePersistentGraph((ObjectData) elementData[i], element);
        }
        return collectionData;
    }


    /**
     * Called server-side only:
     * <ul>
     * <li>by {@link ServerFacadeImpl#executeServerAction(ExecuteServerActionRequest)}
     * </ul>
     */
    public ExecuteServerActionResponse encodeServerActionResult(
            final NakedObject result,
            final ObjectData[] updatesData,
            final ReferenceData[] disposedData,
            final ObjectData persistedTargetData,
            final ObjectData[] persistedParametersData,
            final String[] messages,
            final String[] warnings) {
        Data resultData;
        if (result == null) {
            resultData = dataFactory.createNullData("");
        } else if (result.getSpecification().isCollection()) {
            resultData = serializer.serializeCollection(result, serverSideRetrievedObjectGraphDepth, new KnownObjectsRequest());
        } else if (result.getSpecification().isObject()) {
            resultData = encodeCompletePersistentGraph(result);
        } else {
            throw new UnknownTypeException(result);
        }

        return new ExecuteServerActionResponse(resultData, updatesData, disposedData, persistedTargetData, persistedParametersData, messages, warnings);
    }

    /**
     * Called server-side only:
     * <ul>
     * <li>by {@link ServerFacadeImpl#resolveField(ResolveFieldRequest)}
     * <li>by {@link ServerFacadeImpl#executeServerAction(ExecuteServerActionRequest)
     * </ul>
     */
    public NakedObjectAssociation[] getFieldOrder(final NakedObjectSpecification specification) {
        return fieldOrderCache.getFields(specification);
    }


    /////////////////////////////////////////////////////////////////
    // Helpers
    /////////////////////////////////////////////////////////////////

    private final Data createParameter(final String type, final NakedObject adapter, final KnownObjectsRequest knownObjects) {
        if (adapter == null) {
            return dataFactory.createNullData(type);
        }

        if (!adapter.getSpecification().isObject()) {
            throw new UnknownTypeException(adapter.getSpecification());
        }
        
        if (adapter.getSpecification().isEncodeable()) {
		    return serializer.serializeEncodeable(adapter);
		} else {
			return encode(adapter, clientSideActionParameterGraphDepth, knownObjects);
		}
    }

    private ObjectData encode(final NakedObject adapter, int depth) {
    	return (ObjectData) encode(adapter, depth, new KnownObjectsRequest());
    }
    
	private ReferenceData encode(final NakedObject adapter,
			int depth, final KnownObjectsRequest knownObjects) {
		return serializer.serializeAdapter(adapter, depth, knownObjects);
	}

}

// Copyright (c) Naked Objects Group Ltd.
