package org.nakedobjects.metamodel.examples.facets.jsr303;

import org.nakedobjects.applib.AbstractDomainObject;

public class DomainObjectVanilla extends AbstractDomainObject {

    private String firstName;

    public String getFirstName() {
        resolve(firstName);
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        objectChanged();
    }
    
}
