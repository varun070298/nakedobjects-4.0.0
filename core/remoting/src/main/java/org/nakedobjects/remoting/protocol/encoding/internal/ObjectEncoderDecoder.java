package org.nakedobjects.remoting.protocol.encoding.internal;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.remoting.data.Data;
import org.nakedobjects.remoting.data.common.EncodableObjectData;
import org.nakedobjects.remoting.data.common.IdentityData;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.data.common.ReferenceData;
import org.nakedobjects.remoting.data.query.PersistenceQueryData;
import org.nakedobjects.remoting.exchange.OpenSessionRequest;
import org.nakedobjects.remoting.exchange.AuthorizationResponse;
import org.nakedobjects.remoting.exchange.ExecuteClientActionResponse;
import org.nakedobjects.remoting.exchange.ExecuteServerActionResponse;
import org.nakedobjects.remoting.exchange.KnownObjectsRequest;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;

public interface ObjectEncoderDecoder {

	// ////////////////////////////////////////////////
	// Authorization
	// ////////////////////////////////////////////////

	AuthorizationResponse encodeAuthorizeResponse(boolean allowed);

	// ////////////////////////////////////////////////
	// Field Order
	// ////////////////////////////////////////////////

	/**
	 * Returns the agreed order to transfer fields within data objects. Both
	 * remote parties need to process the fields in the same order, this is that
	 * order.
	 */
	NakedObjectAssociation[] getFieldOrder(
			NakedObjectSpecification specification);

	// ////////////////////////////////////////////////
	// Identity
	// ////////////////////////////////////////////////

	IdentityData encodeIdentityData(NakedObject object);

	// ////////////////////////////////////////////////
	// Resolves
	// ////////////////////////////////////////////////

	Data encodeForResolveField(
			NakedObject targetAdapter, 
			String fieldName);

	// ////////////////////////////////////////////////
	// Actions & Parameters
	// ////////////////////////////////////////////////

	ReferenceData encodeActionTarget(
			NakedObject targetAdapter,
			KnownObjectsRequest knownObjects);

	Data[] encodeActionParameters(
			NakedObjectSpecification[] parameterTypes,
			NakedObject[] parameterAdapters, 
			KnownObjectsRequest knownObjects);

	ExecuteServerActionResponse encodeServerActionResult(
			NakedObject resultAdapter,
			ObjectData[] updatedData, 
			ReferenceData[] disposedData,
			ObjectData persistedTargetData, 
			ObjectData[] persistedParameterData,
			String[] messages, 
			String[] warnings);

	ExecuteClientActionResponse encodeClientActionResult(
			ReferenceData[] madePersistent, Version[] changedVersion,
			ObjectData[] updates);

	// ////////////////////////////////////////////////
	// Graphs
	// ////////////////////////////////////////////////

	/**
	 * Creates an {@link ObjectData} that contains all the data for all the
	 * transient objects in the specified transient object.
	 * 
	 * <p>
	 * For any referenced persistent object in the graph, only the reference is
	 * passed across.
	 */
	ObjectData encodeMakePersistentGraph(NakedObject adapter,
			KnownObjectsRequest knownObjects);

	ObjectData encodeGraphForChangedObject(NakedObject adapter,
			KnownObjectsRequest knownObjects);

	/**
	 * Creates a graph of ReferenceData objects (mirroring the graph of
	 * transient objects) to transfer the OIDs and Versions for each object that
	 * was made persistent during the makePersistent call.
	 */
	ObjectData encodeMadePersistentGraph(
			ObjectData originalData, NakedObject adapter);

	/**
	 * Creates an ObjectData that contains all the data for all the objects in
	 * the graph. This allows the client to receive all data it might need
	 * without having to return to the server to get referenced objects.
	 */
	ObjectData encodeCompletePersistentGraph(NakedObject object);

	// ////////////////////////////////////////////////
	// Value
	// ////////////////////////////////////////////////

	EncodableObjectData encodeAsValue(NakedObject value);

	// ////////////////////////////////////////////////
	// Update
	// ////////////////////////////////////////////////

	/**
	 * Creates an {@link ObjectData} that contains the data for the specified
	 * object, but not the data for any referenced objects.
	 * 
	 * <p>
	 * For each referenced object only the reference is passed across.
	 */
	ObjectData encodeForUpdate(NakedObject object);

	// ////////////////////////////////////////////////
	// decode
	// ////////////////////////////////////////////////

	NakedObject decode(Data data);

	void decode(ObjectData[] dataArray);

	NakedObject decode(Data data, KnownObjectsRequest knownObjects);

	// ////////////////////////////////////////////////
	// PersistenceQuery
	// ////////////////////////////////////////////////

	PersistenceQueryData encodePersistenceQuery(
			PersistenceQuery persistenceQuery);

	PersistenceQuery decodePersistenceQuery(
			PersistenceQueryData persistenceQueryData);

	// ////////////////////////////////////////////////
	// makePersistent
	// ////////////////////////////////////////////////

	void madePersistent(NakedObject target, ObjectData persistedTarget);



	
}

// Copyright (c) Naked Objects Group Ltd.
