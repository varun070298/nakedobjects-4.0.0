package org.nakedobjects.metamodel.facets.propparam.typicallength;

import org.nakedobjects.metamodel.facets.FacetHolder;


public class TypicalLengthFacetAnnotation extends TypicalLengthFacetAbstract {

    private final int value;

	public TypicalLengthFacetAnnotation(final int value, final FacetHolder holder) {
        super(holder, false);
        this.value = value;
    }

	@Override
	public int value() {
		return value;
	}

}

// Copyright (c) Naked Objects Group Ltd.
