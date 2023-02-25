package org.nakedobjects.runtime.memento;

import java.util.ArrayList;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.encoding.DataInputStreamExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputStreamExtended;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.exceptions.UnknownTypeException;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.facets.object.encodeable.EncodableFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.OneToManyAssociation;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.metamodel.util.CollectionFacetUtils;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.PersistenceSessionHydrator;
import org.nakedobjects.runtime.persistence.PersistorUtil;


public class Memento {
    private final static long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(Memento.class);
    private Data state;
    private List<Oid> transientObjects = new ArrayList<Oid>(); 

    /**
     * Creates a memento that hold the state for the specified object. This object is Serializable and can be
     * passed over the network easily. Also for a persistent only the reference's Oids are held, avoiding the
     * need for serializing the whole object graph.
     */
    public Memento(final NakedObject object) {
        state = object == null ? null : createData(object);
        LOG.debug("created memento for " + this);
    }

    private Data createData(final NakedObject object) {
        if (object.getSpecification().isCollection()) {
            return createCollectionData(object);
        } else {
            return createObjectData(object);
        }
    }

    private Data createCollectionData(final NakedObject object) {
        final CollectionFacet facet = CollectionFacetUtils.getCollectionFacetFromSpec(object);
        final Data[] collData = new Data[facet.size(object)];
        final Iterator elements = facet.iterator(object);
        int i = 0;
        while (elements.hasNext()) {
            final NakedObject ref = (NakedObject) elements.next();
            collData[i++] = new Data(ref.getOid(), ref.getResolveState().name(), ref.getSpecification().getFullName());
        }
        //String elementType = facet.getTypeOfFacet().valueSpec().getFullName();
        String elementType = object.getSpecification().getFullName();
        return new CollectionData(object.getOid(), object.getResolveState(), elementType , collData);
    }

    private ObjectData createObjectData(final NakedObject adapter) {
        final NakedObjectSpecification cls = adapter.getSpecification();
        final NakedObjectAssociation[] fields = cls.getAssociations();
        final ObjectData data = new ObjectData(adapter.getOid(), adapter.getResolveState().name(), cls.getFullName());
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isDerived()) {
                continue;
            }
            createFieldData(adapter, data, fields[i]);
        }
        return data;
    }

    private void createFieldData(final NakedObject object, final ObjectData data, final NakedObjectAssociation field) {
        Object fieldData;
        if (field.isOneToManyAssociation()) {
            final NakedObject coll = field.get(object);
            fieldData = createCollectionData(coll);
        } else if (field.getSpecification().isEncodeable()) {
            final EncodableFacet facet = field.getSpecification().getFacet(EncodableFacet.class);
            final NakedObject value = field.get(object);
            fieldData = facet.toEncodedString(value);
        } else if (field.isOneToOneAssociation()) {
            final NakedObject ref = ((OneToOneAssociation) field).get(object);
            fieldData = createReferenceData(ref);
        } else {
            throw new UnknownTypeException(field);
        }
        data.addField(field.getId(), fieldData);
    }

    private Data createReferenceData(final NakedObject ref) {
        if (ref == null) {
            return null;
        }
        
        Oid refOid = ref.getOid();
        if (refOid == null) {
        	return createStandaloneData(ref);
        }
        
        if (refOid.isTransient() && !transientObjects.contains(refOid)) {
            transientObjects.add(refOid);
            return createObjectData(ref);
        }
        
        final String resolvedState = ref.getResolveState().name();
        final String specification = ref.getSpecification().getFullName();
        return new Data(refOid, resolvedState, specification);
        
    }

    private Data createStandaloneData(NakedObject adapter) {
    	return new StandaloneData(adapter);
	}

	public Oid getOid() {
        return state.getOid();
    }

    public NakedObject recreateObject() {
        if (state == null) {
            return null;
        } else {
            final NakedObjectSpecification spec = getSpecificationLoader().loadSpecification(state.getClassName());
            NakedObject object;
            ResolveState targetState;
            if (getOid().isTransient()) {
                object = getHydrator().recreateAdapter(getOid(), spec);
                targetState = ResolveState.SERIALIZING_TRANSIENT;
            } else {
                object = getHydrator().recreateAdapter(getOid(), spec);
                targetState = ResolveState.UPDATING;
            }
            if (object.getSpecification().isCollection()) {
                populateCollection(object, (CollectionData) state, targetState);
            } else {
                updateObject(object, state, targetState);
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("recreated object " + object.getOid());
            }
            return object;
        }
    }

    private void populateCollection(NakedObject collection, CollectionData state, ResolveState targetState) {
        NakedObject[] initData = new NakedObject[state.elements.length];
        int i = 0;
        for (Data elementData : state.elements) {
           initData[i++] = recreateReference(elementData);
        }
        CollectionFacet facet = collection.getSpecification().getFacet(CollectionFacet.class);
        facet.init(collection, initData);
    }

    private NakedObject recreateReference(final Data data) {
    	final NakedObjectSpecification spec = getSpecificationLoader().loadSpecification(data.getClassName());
    	
    	if (data instanceof StandaloneData) {
			StandaloneData standaloneData = (StandaloneData) data;
			return standaloneData.getAdapter();
    	}
    	
        final Oid oid = data.getOid();
        if (oid == null) {
            return null;
        }
        
    	NakedObject ref;
        ResolveState resolveState;
        if (oid.isTransient()) {
            ref = getHydrator().recreateAdapter(oid, spec);
            resolveState = ResolveState.getResolveState(data.getResolveState());
        } else {
            ref = getHydrator().recreateAdapter(oid, spec);
            resolveState = ResolveState.GHOST;
            if (ref.getResolveState().isValidToChangeTo(resolveState)) {
                ref.changeState(resolveState);
            }
            // REVIEW is this needed, or is the object set up at this point
            if (data instanceof ObjectData) {
                updateObject(ref, data, resolveState);
            }
        }
        return ref;
    }

    @Override
    public String toString() {
        return "[" + (state == null ? null : state.getClassName() + "/" + state.getOid() + state) + "]";
    }

    /**
     * Updates the specified object (assuming it is the correct object for this memento) with the state held
     * by this memento.
     * 
     * @throws IllegalArgumentException
     *             if the memento was created from different logical object to the one specified (i.e. its oid
     *             differs).
     */
    public void updateObject(final NakedObject object) {
        updateObject(object, state, ResolveState.RESOLVING);
    }

    private void updateObject(final NakedObject object, final Data state, final ResolveState resolveState) {
        final Object oid = object.getOid();
        if (oid != null && !oid.equals(state.getOid())) {
            throw new IllegalArgumentException("This memento can only be used to update the naked object with the Oid "
                    + state.getOid() + " but is " + oid);

        } else {
            if (!(state instanceof ObjectData)) {
                throw new NakedObjectException("Expected an ObjectData but got " + state.getClass());
            } else {
                updateObject(object, resolveState, state);
            }
            LOG.debug("object updated " + object.getOid());
        }

    }

    private void updateObject(final NakedObject object, final ResolveState resolveState, final Data state) {
        if (object.getResolveState().isValidToChangeTo(resolveState)) {
            PersistorUtil.start(object, resolveState);
            updateFields(object, state);
            PersistorUtil.end(object);
        } else if (object.getResolveState() == ResolveState.TRANSIENT && resolveState == ResolveState.TRANSIENT) {
            updateFields(object, state);
        } else {
            final ObjectData od = (ObjectData) state;
            if (od.containsField()) {
                throw new NakedObjectException("Resolve state (for " + object
                        + ") inconsistent with fact that data exists for fields");
            }
        }
    }

    private void updateFields(final NakedObject object, final Data state) {
        final ObjectData od = (ObjectData) state;
        final NakedObjectAssociation[] fields = object.getSpecification().getAssociations();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isDerived()) {
                continue;
            }
            updateField(object, od, fields[i]);
        }
    }

    private void updateField(final NakedObject object, final ObjectData od, final NakedObjectAssociation field) {
        final Object fieldData = od.getEntry(field.getId());
        if (field.isOneToManyAssociation()) {
            updateOneToManyAssociation(object, (OneToManyAssociation) field, (CollectionData) fieldData);
        } else if (field.getSpecification().containsFacet(EncodableFacet.class)) {
            final EncodableFacet facet = field.getSpecification().getFacet(EncodableFacet.class);
            final NakedObject value = facet.fromEncodedString((String) fieldData);
            ((OneToOneAssociation) field).initAssociation(object, value);
        } else if (field.isOneToOneAssociation()) {
            updateOneToOneAssociation(object, (OneToOneAssociation) field, (Data) fieldData);
        }
    }

    private void updateOneToManyAssociation(
            final NakedObject object,
            final OneToManyAssociation field,
            final CollectionData collectionData) {
        final NakedObject collection = field.get(object);
        final CollectionFacet facet = CollectionFacetUtils.getCollectionFacetFromSpec(collection);
        final Vector original = new Vector();
        final Enumeration elements = facet.elements(collection);
        while (elements.hasMoreElements()) {
            original.addElement(elements.nextElement());
        }

        for (int j = 0; j < collectionData.elements.length; j++) {
            final NakedObject element = recreateReference(collectionData.elements[j]);
            if (!facet.contains(collection, element)) {
                LOG.debug("  association " + field + " changed, added " + element.getOid());
                field.addElement(object, element);
            } else {
                field.removeElement(object, element);
            }
        }

        final int size = original.size();
        for (int i = 0; i < size; i++) {
            final NakedObject element = (NakedObject) original.elementAt(i);
            LOG.debug("  association " + field + " changed, removed " + element.getOid());
            field.removeElement(object, element);
        }
    }

    private void updateOneToOneAssociation(final NakedObject object, final OneToOneAssociation field, final Data fieldData) {
        if (fieldData == null) {
            field.initAssociation(object, null);
        } else {
            final NakedObject ref = recreateReference(fieldData);
            if (field.get(object) != ref) {
                LOG.debug("  association " + field + " changed to " + ref.getOid());
                field.initAssociation(object, ref);
            }
        }
    }

    protected Data getData() {
        return state;
    }
    
    public void encodedData(final DataOutputStreamExtended outputImpl) throws IOException {
    	outputImpl.writeEncodable(state);
    }
    
    public void restore(final DataInputStreamExtended inputImpl) throws IOException {
        state = inputImpl.readEncodable(Data.class);
    }
 
    public void debug(final DebugString debug) {
        state.debug(debug);
    }

    
    /////////////////////////////////////////////////////////////////
    // Dependencies (from context)
    /////////////////////////////////////////////////////////////////

    private static SpecificationLoader getSpecificationLoader() {
        return NakedObjectsContext.getSpecificationLoader();
    }

    private static PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }

    private static PersistenceSessionHydrator getHydrator() {
        return getPersistenceSession();
    }
    

}
// Copyright (c) Naked Objects Group Ltd.
