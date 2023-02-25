package org.nakedobjects.metamodel.facets.value;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;


public interface MoneyValueFacet extends Facet {

    float getAmount(NakedObject object);

    String getCurrencyCode(NakedObject object);

    NakedObject createValue(float amount, String currencyCode);

}

// Copyright (c) Naked Objects Group Ltd.
