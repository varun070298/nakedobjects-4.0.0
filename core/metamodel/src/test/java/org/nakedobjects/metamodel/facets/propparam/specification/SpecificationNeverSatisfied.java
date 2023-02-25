package org.nakedobjects.metamodel.facets.propparam.specification;

import org.nakedobjects.applib.spec.Specification;

public class SpecificationNeverSatisfied implements Specification {

    public String satisfies(Object obj) {
        return "not satisfied";
    }

}
