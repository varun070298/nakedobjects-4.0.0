package org.nakedobjects.example.expenses.claims;

import org.nakedobjects.applib.AbstractDomainObject;
import org.nakedobjects.applib.annotation.Bounded;
import org.nakedobjects.applib.annotation.Immutable;
import org.nakedobjects.applib.annotation.When;


@Bounded
@Immutable(When.ONCE_PERSISTED)
public class ProjectCode extends AbstractDomainObject {

    // {{ Title & Icon
    public String title() {
        final StringBuilder t = new StringBuilder();
        t.append(getCode()).append(" ").append(getDescription());
        return t.toString();
    }

    public String iconName() {
        return "Look Up";
    }

    // }}

    // {{ Code
    private String code;

    public String getCode() {
        return this.code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    // }}

    // {{ Description
    private String description;

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    // }}

}
