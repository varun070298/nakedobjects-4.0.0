package org.nakedobjects.runtime.transaction.updatenotifier;

import java.util.List;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.component.Injectable;
import org.nakedobjects.metamodel.commons.component.TransactionScopedComponent;

/**
 * UpdateNotifier provides updates to client making available lists of the
 * latest changed and disposed objects.
 */
public interface UpdateNotifier extends TransactionScopedComponent, Injectable {

	// //////////////////////////////////////////////////
	// Changed Objects
	// //////////////////////////////////////////////////

	/**
	 * Used by the framework to add objects that have just changed.
	 */
	void addChangedObject(NakedObject object);

	/**
	 * Returns an immutable {@link List} of changed objects.
	 * 
	 * <p>
	 * Each changed object that was added is only ever provided during one call
	 * to this method so the list must be processed fully to avoid missing
	 * updates.
	 */
	List<NakedObject> getChangedObjects();

	// //////////////////////////////////////////////////
	// Disposed Objects
	// //////////////////////////////////////////////////

	/**
	 * Used by the framework to add objects that have just been disposed of.
	 */
	void addDisposedObject(NakedObject adapter);

	/**
	 * Returns an immutable {@link List} of disposed objects.
	 * 
	 * <p>
	 * Each object that was disposed of is only ever provided during one call to
	 * this method so the list must be processed fully to avoid missing
	 * deletions.
	 */
	public List<NakedObject> getDisposedObjects();

	// //////////////////////////////////////////////////
	// Empty, Clear
	// //////////////////////////////////////////////////

	void ensureEmpty();

	void clear();

}

// Copyright (c) Naked Objects Group Ltd.
