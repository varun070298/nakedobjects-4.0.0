package org.nakedobjects.plugins.headless.embedded.dom.employee;

import org.nakedobjects.applib.AbstractDomainObject;
import org.nakedobjects.applib.annotation.Disabled;
import org.nakedobjects.applib.annotation.MemberOrder;
import org.nakedobjects.applib.annotation.When;
import org.nakedobjects.plugins.headless.embedded.dom.claim.Approver;
import org.nakedobjects.plugins.headless.embedded.dom.claim.Claimant;


public class Employee extends AbstractDomainObject implements Claimant, Approver {

	// {{ Title
    public String title() {
        return getName();
    }
    // }}

    
    // {{ Name
    private String name;
    @MemberOrder(sequence="1")
    public String getName() {
        return name;
    }
    public void setName(final String name) {
        this.name = name;
    }
    public void modifyName(final String name) {
        setName(name);
    }
    public void clearName() {
        setName(null);
    }
    
    public boolean whetherHideName;
    public boolean hideName() {
        return whetherHideName;
    }
    public String reasonDisableName;
    public String disableName() {
    	return reasonDisableName;
    }
    public String reasonValidateName;
    public String validateName(String name) {
    	return reasonValidateName;
    }
    // }}
    
    // {{ Password
    private String password;
    @MemberOrder(sequence="2")
    @Disabled(When.ONCE_PERSISTED)
    public String getPassword() {
        return password;
    }
    public void setPassword(final String password) {
        this.password = password;
    }
    // }}
    

    // {{ Approver
    private Approver approver;
    @MemberOrder(sequence="2")
    public Approver getApprover() {
        return approver;
    }
    public void setApprover(final Approver approver) {
        this.approver = approver;
    }
    // }}
 
    
    
}


// Copyright (c) Naked Objects Group Ltd.
