package org.nakedobjects.metamodel.examples.facets.namefile;

import org.nakedobjects.applib.AbstractDomainObject;

public class DomainObjectWithoutNameFileEntry extends AbstractDomainObject {

    private String lastName;

    public String getLastName() {
        resolve(lastName);
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        objectChanged();
    }
    
}
