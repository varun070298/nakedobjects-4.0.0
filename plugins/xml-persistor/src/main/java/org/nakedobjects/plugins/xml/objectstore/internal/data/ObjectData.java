package org.nakedobjects.plugins.xml.objectstore.internal.data;

import java.util.HashMap;
import java.util.Map;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.util.CollectionFacetUtils;
import org.nakedobjects.plugins.xml.objectstore.internal.version.FileVersion;
import org.nakedobjects.runtime.persistence.oidgenerator.simple.SerialOid;

/**
 * A logical collection of elements of a specified type
 */
public class ObjectData extends Data {
	private final Map<String, Object> fieldById;

	public ObjectData(
			final NakedObjectSpecification noSpec, 
			final SerialOid oid,
			final FileVersion version) {
		super(noSpec, oid, version);
		fieldById = new HashMap<String, Object>();
	}

	public Iterable<String> fields() {
		return fieldById.keySet();
	}

	//////////////////////////////////////////////////////////
	// id
	//////////////////////////////////////////////////////////

	public String id(final String fieldId) {
		final Object field = get(fieldId);
		return field == null ? null : "" + ((SerialOid) field).getSerialNo();
	}

	
	//////////////////////////////////////////////////////////
	// value
	//////////////////////////////////////////////////////////

	public void set(final String fieldId, final String value) {
		fieldById.put(fieldId, value);
	}

	public void saveValue(final String fieldId, final boolean isEmpty,
			final String encodedString) {
		if (isEmpty) {
			fieldById.remove(fieldId);
		} else {
			fieldById.put(fieldId, encodedString);
		}
	}

	public String value(final String fieldId) {
		return (String) get(fieldId);
	}

	
	//////////////////////////////////////////////////////////
	// reference
	//////////////////////////////////////////////////////////

	public Object get(final String fieldId) {
		return fieldById.get(fieldId);
	}

	public void set(final String fieldId, final Object oid) {
		if (oid == null) {
			fieldById.remove(fieldId);
		} else {
			fieldById.put(fieldId, oid);
		}
	}

	//////////////////////////////////////////////////////////
	// collection
	//////////////////////////////////////////////////////////

	public void initCollection(final String fieldId) {
		fieldById.put(fieldId, new ReferenceVector());
	}

	public void addElement(final String fieldId, final SerialOid elementOid) {
		if (!fieldById.containsKey(fieldId)) {
			throw new NakedObjectException("Field " + fieldId
					+ " not found  in hashtable");
		}

		final ReferenceVector v = (ReferenceVector) fieldById.get(fieldId);
		v.add(elementOid);
	}

	public ReferenceVector elements(final String fieldId) {
		return (ReferenceVector) fieldById.get(fieldId);
	}

	public void addAssociation(final NakedObject fieldContent,
			final String fieldId, final boolean ensurePersistent) {
		final boolean notAlreadyPersistent = fieldContent != null
				&& fieldContent.isTransient();
		if (ensurePersistent && notAlreadyPersistent) {
			throw new IllegalStateException(
					"Cannot save an object that is not persistent: "
							+ fieldContent);
		}
		// LOG.debug("adding reference field " + fieldId +" " + fieldContent);
		set(fieldId, fieldContent == null ? null : fieldContent.getOid());
	}

	public void addInternalCollection(final NakedObject collection,
			final String fieldId, final boolean ensurePersistent) {
		/*
		 * if (ensurePersistent && collection != null && collection.getOid() ==
		 * null) { throw new
		 * IllegalStateException("Cannot save a collection that is not persistent: "
		 * + collection); }
		 */

		initCollection(fieldId);

		// int size = collection.size();

		final CollectionFacet facet = CollectionFacetUtils
				.getCollectionFacetFromSpec(collection);
		for (NakedObject element : facet.iterable(collection)) {
			// LOG.debug("adding element to internal collection field " +
			// fieldId +" " + element);
			final Object elementOid = element.getOid();
			if (elementOid == null) {
				throw new IllegalStateException("Element is not persistent "
						+ element);
			}

			addElement(fieldId, (SerialOid) elementOid);
		}

		/*
		 * while (e.hasMoreElements()) { NakedObject element = (NakedObject)
		 * e.nextElement(); //
		 * LOG.debug("adding element to internal collection field " + fieldId
		 * +" " + element); Object elementOid = element.getOid(); if (elementOid
		 * == null) { throw new IllegalStateException("Element is not persistent
		 * "+element); }
		 * 
		 * addElement(fieldId, (SimpleOid) elementOid); }
		 */}


	//////////////////////////////////////////////////////////
	// toString
	//////////////////////////////////////////////////////////

	@Override
	public String toString() {
		return "ObjectData[type=" + getTypeName() + ",oid=" + getOid()
				+ ",fields=" + fieldById + "]";
	}


}
// Copyright (c) Naked Objects Group Ltd.
