package org.nakedobjects.metamodel.facets.propparam.specification;

import org.nakedobjects.applib.AbstractDomainObject;
import org.nakedobjects.applib.annotation.MustSatisfy;

public class DomainObjectWithMustSatisfyAnnotations extends AbstractDomainObject {

    private String firstName;
    @MustSatisfy(SpecificationAlwaysSatisfied.class)
    public String getFirstName() {
        resolve(firstName);
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
        objectChanged();
    }
    
    private String lastName;
    @MustSatisfy(SpecificationRequiresFirstLetterToBeUpperCase.class)
    public String getLastName() {
        resolve(lastName);
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
        objectChanged();
    }

    public void changeLastName(
            @MustSatisfy(SpecificationRequiresFirstLetterToBeUpperCase.class)
            String lastName
            ) {
        setLastName(lastName);
    }

}
