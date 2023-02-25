package org.nakedobjects.metamodel.facets.propparam.typicallength;

import org.nakedobjects.metamodel.facets.FacetHolder;


public class TypicalLengthFacetZero extends TypicalLengthFacetAbstract {

    public TypicalLengthFacetZero(final FacetHolder holder) {
        super(holder, false);
    }

    @Override
    public boolean isNoop() {
        return true;
    }

	@Override
	public int value() {
		return 0;
	}

}

// Copyright (c) Naked Objects Group Ltd.
