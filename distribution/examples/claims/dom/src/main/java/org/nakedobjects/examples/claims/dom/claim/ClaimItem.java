package org.nakedobjects.examples.claims.dom.claim;

import org.nakedobjects.applib.AbstractDomainObject;
import org.nakedobjects.applib.annotation.MemberOrder;
import org.nakedobjects.applib.value.Date;
import org.nakedobjects.applib.value.Money;


public class ClaimItem extends AbstractDomainObject {

	// {{ Title
    public String title() {
        return getDescription();
    }
    // }}
	
    
	// {{ DateIncurred
	private Date dateIncurred;
    @MemberOrder(sequence="1")
    public Date getDateIncurred() {
        return dateIncurred;
    }
    public void setDateIncurred(Date dateIncurred) {
        this.dateIncurred = dateIncurred;
    }
    // }}
    
    
    // {{ Description
    private String description;
    @MemberOrder(sequence="2")
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    // }}


    // {{ Amount
    private Money amount;
    @MemberOrder(sequence="3")
    public Money getAmount() {
        return amount;
    }
    public void setAmount(Money price) {
        this.amount = price;
    }
    // }}

}
