package org.nakedobjects.remoting.protocol.encoding.internal;

import java.util.Enumeration;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.exceptions.UnknownTypeException;
import org.nakedobjects.metamodel.facets.actcoll.typeof.TypeOfFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.facets.object.encodeable.EncodableFacet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.util.CollectionFacetUtils;
import org.nakedobjects.remoting.data.Data;
import org.nakedobjects.remoting.data.DataFactory;
import org.nakedobjects.remoting.data.common.CollectionData;
import org.nakedobjects.remoting.data.common.EncodableObjectData;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.data.common.ReferenceData;
import org.nakedobjects.remoting.exchange.KnownObjectsRequest;
import org.nakedobjects.runtime.persistence.PersistorUtil;

/**
 * Utility class to create Data objects representing a graph of NakedObjects.
 * 
 * As each object is serialised its resolved state is changed to SERIALIZING; any object that is marked as
 * SERIALIZING is skipped.
 */
final class ObjectSerializer {
	
	private final DataFactory dataFactory;
	private final FieldOrderCache fieldOrderCache;

    public ObjectSerializer(final DataFactory dataFactory, final FieldOrderCache fieldOrderCache) {
		this.fieldOrderCache = fieldOrderCache;
		this.dataFactory = dataFactory;
	}

    public final ReferenceData serializeAdapter(
            final NakedObject adapter,
            final int depth,
            final KnownObjectsRequest knownObjects) {
        Assert.assertNotNull(adapter);
        return (ReferenceData) serializeObject2(adapter, depth, knownObjects);
    }

    public final EncodableObjectData serializeEncodeable(final NakedObject adapter) {
        final EncodableFacet facet = adapter.getSpecification().getFacet(EncodableFacet.class);
        return this.dataFactory.createValueData(adapter.getSpecification().getFullName(), facet.toEncodedString(adapter));
    }

    private final Data serializeObject2(
            final NakedObject adapter,
            final int graphDepth,
            final KnownObjectsRequest knownObjects) {
        Assert.assertNotNull(adapter);

        final ResolveState resolveState = adapter.getResolveState();
        boolean isTransient = adapter.isTransient();

        if (!isTransient && (resolveState.isSerializing() || resolveState.isGhost() || graphDepth <= 0)) {
            Assert.assertNotNull("OID needed for reference", adapter, adapter.getOid());
            return this.dataFactory.createIdentityData(adapter.getSpecification().getFullName(), adapter.getOid(), adapter.getVersion());
        }
        if (isTransient && knownObjects.containsKey(adapter)) {
            return knownObjects.get(adapter);
        }

        boolean withCompleteData = resolveState == ResolveState.TRANSIENT || resolveState == ResolveState.RESOLVED;

        final String type = adapter.getSpecification().getFullName();
        final Oid oid = adapter.getOid();
        final ObjectData data = this.dataFactory.createObjectData(type, oid, withCompleteData, adapter.getVersion());
        if (isTransient) {
            knownObjects.put(adapter, data);
        }

        final NakedObjectAssociation[] fields = fieldOrderCache.getFields(adapter.getSpecification());
        final Data[] fieldContent = new Data[fields.length];
        PersistorUtil.start(adapter, adapter.getResolveState().serializeFrom());
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isDerived()) {
                continue;
            }
            final NakedObject field = fields[i].get(adapter);

            if (fields[i].getSpecification().isEncodeable()) {
                if (field == null) {
                    fieldContent[i] = this.dataFactory.createNullData(fields[i].getSpecification().getFullName());
                } else {
                    fieldContent[i] = serializeEncodeable(field);
                }

            } else if (fields[i].isOneToManyAssociation()) {
                fieldContent[i] = serializeCollection(field, graphDepth - 1, knownObjects);

            } else if (fields[i].isOneToOneAssociation()) {
                if (field == null) {
                    fieldContent[i] = !withCompleteData ? null : this.dataFactory.createNullData(fields[i].getSpecification()
                            .getFullName());
                } else {
                    fieldContent[i] = serializeObject2(field, graphDepth - 1, knownObjects);
                }

            } else {
                throw new UnknownTypeException(fields[i]);
            }
        }
        PersistorUtil.end(adapter);
        data.setFieldContent(fieldContent);
        return data;
    }

	public CollectionData serializeCollection(
            final NakedObject collectionAdapter,
            final int graphDepth,
            final KnownObjectsRequest knownObjects) {
        final Oid oid = collectionAdapter.getOid();
        final String collectionType = collectionAdapter.getSpecification().getFullName();
        final TypeOfFacet typeOfFacet = collectionAdapter.getSpecification().getFacet(TypeOfFacet.class);
        if (typeOfFacet == null) {
            throw new NakedObjectException("No type of facet for collection " + collectionAdapter);
        }
        final String elementType = typeOfFacet.value().getName();
        final boolean hasAllElements = collectionAdapter.isTransient() || 
                                       collectionAdapter.getResolveState().isResolved();
        ReferenceData[] elements;

        if (hasAllElements) {
            final CollectionFacet collectionFacet = CollectionFacetUtils.getCollectionFacetFromSpec(collectionAdapter);
            final Enumeration e = collectionFacet.elements(collectionAdapter);
            elements = new ReferenceData[collectionFacet.size(collectionAdapter)];
            int i = 0;
            while (e.hasMoreElements()) {
                final NakedObject element = (NakedObject) e.nextElement();
                elements[i++] = serializeAdapter(element, graphDepth, knownObjects);
            }
        } else {
            elements = new ObjectData[0];
        }

        return this.dataFactory.createCollectionData(collectionType, elementType, oid, elements, hasAllElements, collectionAdapter.getVersion());
    }


}
// Copyright (c) Naked Objects Group Ltd.
