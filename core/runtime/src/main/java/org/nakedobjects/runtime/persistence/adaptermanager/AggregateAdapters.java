package org.nakedobjects.runtime.persistence.adaptermanager;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.metamodel.spec.feature.OneToManyAssociation;

public class AggregateAdapters implements Iterable<NakedObject> {
	
	private final NakedObject rootAdapter;
	private final Map<OneToManyAssociation,NakedObject> collectionAdapters = new LinkedHashMap<OneToManyAssociation,NakedObject>();
	
	public AggregateAdapters(NakedObject rootAdapter) {
        Assert.assertNotNull(rootAdapter);
		this.rootAdapter = rootAdapter;
	}
	public NakedObject getRootAdapter() {
		return rootAdapter;
	}
	public void addCollectionAdapter(OneToManyAssociation otma, NakedObject collectionAdapter) {
        Assert.assertNotNull(otma);
        Assert.assertNotNull(collectionAdapter);
		collectionAdapters.put(otma, collectionAdapter);
	}
	public Map<OneToManyAssociation, NakedObject> getCollectionAdapters() {
		return Collections.unmodifiableMap(collectionAdapters);
	}
	
	/**
	 * Iterate over the {@link #addCollectionAdapter(OneToManyAssociation, NakedObject) collection adapter}s 
	 * (does not include the {@link #getRootAdapter() root adapter}.
	 */
	public Iterator<NakedObject> iterator() {
		return getCollectionAdapters().values().iterator();
	}
	
	public Set<OneToManyAssociation> getCollections() {
		return getCollectionAdapters().keySet();
	}
	public NakedObject getCollectionAdapter(OneToManyAssociation otma) {
		return collectionAdapters.get(otma);
	}
}