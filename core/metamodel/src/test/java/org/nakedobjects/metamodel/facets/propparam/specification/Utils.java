package org.nakedobjects.metamodel.facets.propparam.specification;

import java.util.ArrayList;
import java.util.List;

import org.nakedobjects.applib.spec.Specification;

public final class Utils {

    private Utils(){}

    @SuppressWarnings("serial")
    public static List<Specification> listOf(final Specification specification) {
        return new ArrayList<Specification>() {{
            add(specification);
        }};
    }
}


// Copyright (c) Naked Objects Group Ltd.
