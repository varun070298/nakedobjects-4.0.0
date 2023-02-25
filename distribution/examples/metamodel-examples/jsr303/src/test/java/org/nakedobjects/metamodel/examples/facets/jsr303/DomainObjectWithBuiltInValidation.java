package org.nakedobjects.metamodel.examples.facets.jsr303;

import javax.validation.Pattern;
import javax.validation.Patterns;


public class DomainObjectWithBuiltInValidation {

    private String serialNumber;

    @Patterns( { @Pattern(regex = "^[A-Z0-9-]+$", message = "must contain alphabetical characters only"),
            @Pattern(regex = "^....-....-....$", message = "must match ....-....-....") })
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(final String serialNumber) {
        this.serialNumber = serialNumber;
    }

}
