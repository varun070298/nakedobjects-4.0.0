package org.nakedobjects.metamodel.facets;

import java.lang.reflect.Method;


/**
 * A {@link FacetFactory} which filters out arbitrary {@link Method method}s.
 * 
 * <p>
 * Used by Java5 Reflector's <tt>ProgrammingModel#recognizes(Method)</tt>.
 */
public interface MethodFilteringFacetFactory extends FacetFactory {

    public boolean recognizes(Method method);
}

// Copyright (c) Naked Objects Group Ltd.
