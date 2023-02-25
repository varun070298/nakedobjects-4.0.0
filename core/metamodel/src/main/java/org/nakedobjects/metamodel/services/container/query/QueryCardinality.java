package org.nakedobjects.metamodel.services.container.query;


public enum QueryCardinality {
	/**
	 * Query can return multiple instances.
	 */
	MULTIPLE,
	/**
	 * Query should return only a single instance (or possible none).
	 */
	SINGLE
}