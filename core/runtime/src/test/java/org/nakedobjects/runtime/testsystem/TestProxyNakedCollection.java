package org.nakedobjects.runtime.testsystem;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.EnumerationUtils;
import org.apache.commons.collections.iterators.IteratorEnumeration;
import org.nakedobjects.metamodel.adapter.Instance;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.NakedObjectList;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.metamodel.commons.exceptions.NotYetImplementedException;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.actcoll.typeof.TypeOfFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.Specification;
import org.nakedobjects.runtime.persistence.adaptermanager.ObjectToNakedObjectTransformer;


public class TestProxyNakedCollection implements NakedObject {
    
    private Vector collection = new Vector();
    private Oid oid;
    private ResolveState resolveState = ResolveState.GHOST;
    private NakedObjectSpecification specification;
    private Version version;
    private String removeValidMessage;
    private String addValidMessgae;

    public TestProxyNakedCollection() {}

    public TestProxyNakedCollection(final Object wrappedCollection) {
        if (wrappedCollection instanceof Collection) {
            collection = new Vector((Collection) wrappedCollection);
        } else if (wrappedCollection.getClass().isArray()) {
            final Object[] array = (Object[]) wrappedCollection;
            collection = new Vector(array.length);
            for (int i = 0; i < array.length; i++) {
                collection.add(array[i]);
            }
        } else if (wrappedCollection instanceof NakedObjectList) {
            final Enumeration elements = ((NakedObjectList) wrappedCollection).elements();
            collection = new Vector();
            while (elements.hasMoreElements()) {
                collection.add(elements.nextElement());
            }
        }
    }

    public void checkLock(final Version version) {}

    boolean contains(final NakedObject object) {
        return collection.contains(object);
    }

    public Enumeration elements() {
        return collection.elements();
    }

    public NakedObject firstElement() {
        if (collection.size() == 0) {
            return null;
        } else {
            return (NakedObject) collection.elementAt(0);
        }
    }

    public NakedObjectSpecification getElementSpecification() {
        return null;
    }

    public String getIconName() {
        return null;
    }

    public Object getObject() {
        return collection;
    }

    public Oid getOid() {
        return oid;
    }

    public ResolveState getResolveState() {
        return resolveState;
    }

	public boolean isPersistent() {
		return getResolveState().isPersistent();
	}

	public boolean isTransient() {
		return getResolveState().isTransient();
	}

    public NakedObjectSpecification getSpecification() {
        return specification;
    }

    public Version getVersion() {
        return version;
    }

    public void init(final NakedObject[] initElements) {
        Assert.assertEquals("Collection not empty", 0, this.collection.size());
        for (int i = 0; i < initElements.length; i++) {
            collection.addElement(initElements[i]);
        }
    }

    public void replacePojo(final Object pojo) {
        throw new NotYetImplementedException();
    }

    public void setOptimisticLock(final Version version) {}

    public void setupResolveState(final ResolveState resolveState) {
        this.resolveState = resolveState;
    }

    public void setupSpecification(final NakedObjectSpecification specification) {
        this.specification = specification;
    }

    public void setupOid(final Oid oid) {
        this.oid = oid;
    }

    public void setupElement(final NakedObject element) {
        collection.addElement(element);
    }

    public int size() {
        return collection.size();
    }

    public String titleString() {
        return "title";
    }

    public void setupVersion(final Version version) {
        this.version = version;
    }

    public void changeState(final ResolveState newState) {}

    public void add(final NakedObject element) {
        collection.add(element);
    }

    public void clear() {
        collection.clear();
    }

    public String isAddValid(final NakedObject element) {
        return addValidMessgae;
    }

    public String isRemoveValid(final NakedObject element) {
        return removeValidMessage;
    }

    public void remove(final NakedObject element) {
        collection.remove(element);
    }

    public void setupAddValidMessage(final String addValidMessage) {
        this.addValidMessgae = addValidMessage;
    }

    public void setupRemoveValidMessage(final String removeValidMessage) {
        this.removeValidMessage = removeValidMessage;
    }

    public void fireChangedEvent() {}

    public void setTypeOfFacet(final TypeOfFacet typeOfFacet) {}

    public TypeOfFacet getTypeOfFacet() {
        return null;
    }

    public NakedObject getOwner() {
        return null;
    }

    public Instance getInstance(Specification specification) {
        return null;
    }

    public boolean isAggregated() {
        return true;
    }

}

class TestProxyCollectionFacet implements CollectionFacet {

    private TestProxyNakedCollection collectionDowncasted(final NakedObject collection) {
        final TestProxyNakedCollection coll = (TestProxyNakedCollection) collection;
        return coll;
    }

    public boolean contains(final NakedObject collection, final NakedObject element) {
        return collectionDowncasted(collection).contains(element);
    }

    public Enumeration elements(final NakedObject collection) {
        TestProxyNakedCollection collectionDowncasted = collectionDowncasted(collection);
        List list = EnumerationUtils.toList(collectionDowncasted.elements());
        Collection transformedCollection = CollectionUtils.collect(list, new ObjectToNakedObjectTransformer());
        return new IteratorEnumeration(transformedCollection.iterator());
    }

    public NakedObject firstElement(final NakedObject collection) {
        return collectionDowncasted(collection).firstElement();
    }

    public void init(final NakedObject collection, final NakedObject[] initData) {}

    public int size(final NakedObject collection) {
        return collectionDowncasted(collection).size();
    }

    public Class<? extends Facet> facetType() {
        return CollectionFacet.class;
    }

    public void setFacetHolder(final FacetHolder facetHolder) {}

    public boolean alwaysReplace() {
        return false;
    }
    
    public boolean isDerived() {
    	return false;
    }

    public boolean isNoop() {
        return false;
    }

    public FacetHolder getFacetHolder() {
        throw new NotYetImplementedException();
    }

    public TypeOfFacet getTypeOfFacet() {
        throw new NotYetImplementedException();
    }

    public Iterator<NakedObject> iterator(NakedObject nakedObjectWrappingCollection) {
        throw new NotYetImplementedException();
    }

    public Collection<NakedObject> collection(NakedObject nakedObjectWrappingCollection) {
        throw new NotYetImplementedException();
    }

    public Iterable<NakedObject> iterable(NakedObject nakedObjectRepresentingCollection) {
        throw new NotYetImplementedException();
    }

	public Facet getUnderlyingFacet() {
		return null;
	}
	public void setUnderlyingFacet(Facet underlyingFacet) {
		throw new UnsupportedOperationException();
	}

}
// Copyright (c) Naked Objects Group Ltd.
