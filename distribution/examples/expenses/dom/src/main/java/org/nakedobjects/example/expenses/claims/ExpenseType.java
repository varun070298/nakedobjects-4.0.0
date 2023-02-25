package org.nakedobjects.example.expenses.claims;

import org.nakedobjects.applib.AbstractDomainObject;
import org.nakedobjects.applib.ApplicationException;
import org.nakedobjects.applib.annotation.Bounded;
import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.applib.annotation.Immutable;
import org.nakedobjects.applib.annotation.When;


@Bounded
@Immutable(When.ONCE_PERSISTED)
public class ExpenseType extends AbstractDomainObject {

    // {{ Title

    @Override
    public String toString() {
        final StringBuffer t = new StringBuffer();
        t.append(titleString);
        return t.toString();
    }

    // }}

    // {{ TitleString field
    private String titleString;

    public String getTitleString() {
        return this.titleString;
    }

    public void setTitleString(final String titleString) {
        this.titleString = titleString;
    }

    // }}

    /**
     * This method potentially allows each instance of ExpenseType to have the same icon as its corresponding
     * classname.
     */
    public String iconName() {
        return getTitleString();
    }

    // {{ Corresponding Class
    private String correspondingClass;

    /**
     * The fully-qualified path name for the class that this instance of ApplicationType corresponds to -
     * typically a sub-class of Application
     */
    @Hidden
    public String getCorrespondingClassName() {
        return correspondingClass;
    }

    /**
     * @see #geTitleString
     */
    public void setCorrespondingClassName(final String correspondingClass) {
        this.correspondingClass = correspondingClass;
    }

    /**
     * Converts the correspondingClassName into a java.lang.class.
     * 
     * @return
     */
    public Class<?> correspondingClass() {
        try {
            return Class.forName(getCorrespondingClassName());
        } catch (final ClassNotFoundException e) {
            throw new ApplicationException("Not a valid class " + getCorrespondingClassName());
        }
    }
    // }}
}
