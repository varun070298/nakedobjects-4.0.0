package org.nakedobjects.metamodel.facets.value;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;


public interface PasswordValueFacet extends Facet {

    boolean checkPassword(NakedObject object, String password);

    String getEditText(NakedObject object);

    NakedObject createValue(String value);

}
// Copyright (c) Naked Objects Group Ltd.
