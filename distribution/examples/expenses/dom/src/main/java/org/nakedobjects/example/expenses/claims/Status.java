package org.nakedobjects.example.expenses.claims;

import org.nakedobjects.applib.AbstractDomainObject;
import org.nakedobjects.applib.annotation.Hidden;


public abstract class Status extends AbstractDomainObject {

    public String title() {
        return getTitleString();
    }

    private String titleString;

    @Hidden
    public String getTitleString() {
        return titleString;
    }

    public void setTitleString(final String description) {
        this.titleString = description;
    }

}
