package org.nakedobjects.metamodel.facets.propparam.specification;

import org.nakedobjects.applib.spec.Specification;

public class SpecificationRequiresFirstLetterToBeUpperCase implements Specification {

    public String satisfies(Object obj) {
        if (!(obj instanceof String)) {
            return null;
        }
        String str = (String) obj;
        if (str.length() < 0) {
            return "Must contain at least one letter";
        }
        char firstLetter = str.charAt(0);
        return Character.isUpperCase(firstLetter)? null: "Must start with upper case";
    }

}
