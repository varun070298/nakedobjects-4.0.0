package org.nakedobjects.metamodel.util;

import org.nakedobjects.metamodel.spec.NakedObjectSpecification;

public final class SpecUtils {
	
	private SpecUtils(){}

	public static String typeNameFor(final NakedObjectSpecification specification) {
	    return specification.isCollection() ? "Collection" : "Object";
	}

}
