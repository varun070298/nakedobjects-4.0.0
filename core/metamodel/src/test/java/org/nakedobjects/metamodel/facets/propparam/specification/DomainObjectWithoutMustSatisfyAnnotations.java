package org.nakedobjects.metamodel.facets.propparam.specification;

import org.nakedobjects.applib.AbstractDomainObject;

public class DomainObjectWithoutMustSatisfyAnnotations extends AbstractDomainObject {

    private String firstName;

    public String getFirstName() {
        resolve(firstName);
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        objectChanged();
    }
 
 
    private String lastName;
    public String getLastName() {
        resolve(lastName);
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
        objectChanged();
    }

    public void changeLastName(
            String lastName
            ) {
        setLastName(lastName);
    }

}
