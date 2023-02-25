package org.nakedobjects.examples;

import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.runtime.context.NakedObjectsContext;


public class SpecificationExamples {

    
    public void testSpecificationDetails() {
        // @extract one
        NakedObjectSpecification spec;
        spec = NakedObjectsContext.getSpecificationLoader().loadSpecification(Location.class);
        String screenName = spec.getSingularName();
        // @extract-end

        System.out.println(screenName);
        
        // @extract two
        NakedObjectAssociation[] associations = spec.getAssociations();
        for (int i = 0; i < associations.length; i++) {
            String name = associations[i].getName();
            boolean mustEnter = associations[i].isMandatory();
            // @skip
            // @skip-end
            // @ignore
           
                System.out.println(name + " " + mustEnter);
            // @ignore-end
        }
        // @extract-end
    }
}

// Copyright (c) Naked Objects Group Ltd.
