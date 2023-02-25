package org.nakedobjects.plugins.headless.embedded.dom.claim;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.nakedobjects.applib.AbstractDomainObject;
import org.nakedobjects.applib.annotation.Disabled;
import org.nakedobjects.applib.annotation.MemberOrder;
import org.nakedobjects.applib.value.Color;
import org.nakedobjects.applib.value.Date;

public class Claim extends AbstractDomainObject {

    // {{ Title
    public String title() {
        return getStatus() + " - " + getDate();
    }
    // }}

    
    // {{ Lifecycle
    public void created() {
        status = "New";
        date = new Date();
    }
    // }}

    
    // {{ Description
    private String description;
    @MemberOrder(sequence = "1")
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String defaultDescription() {
		return "enter a description here";
	}
    // }}

    
    // {{ Date
    private Date date;
    @MemberOrder(sequence="2")
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    // }}

    
    // {{ Status
    private String status;
    @Disabled
    @MemberOrder(sequence = "3")
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    // }}

    
    // {{ Claimant
    private Claimant claimant;
    @Disabled
    @MemberOrder(sequence = "4")
    public Claimant getClaimant() {
        return claimant;
    }
    public void setClaimant(Claimant claimant) {
        this.claimant = claimant;
    }
    // }}


    
    // {{ BigDecimal
	private BigDecimal bigDecimal;

	@MemberOrder(sequence = "9")
	public BigDecimal getBigDecimal() {
		return bigDecimal;
	}

	public void setBigDecimal(final BigDecimal bigDecimal) {
		this.bigDecimal = bigDecimal;
	}

	public void modifyBigDecimal(final BigDecimal bigDecimal) {
		// check for no-op
		if (bigDecimal == null || bigDecimal.equals(getBigDecimal())) {
			return;
		}
		// associate new
		setBigDecimal(bigDecimal);
		// additional business logic
		onModifyBigDecimal(bigDecimal);
	}

	public void clearBigDecimal() {
		// check for no-op
		if (getBigDecimal() == null) {
			return;
		}
		// dissociate existing
		setBigDecimal(null);
		// additional business logic
		onClearBigDecimal();
	}

	protected void onModifyBigDecimal(final BigDecimal bigDecimal) {
	}

	protected void onClearBigDecimal() {
	}
	// }}


    // {{ Color
	private Color color;

	@MemberOrder(sequence = "8")
	public Color getColor() {
		return color;
	}

	public void setColor(final Color color) {
		this.color = color;
	}

	public void modifyColor(final Color color) {
		// check for no-op
		if (color == null || color.equals(getColor())) {
			return;
		}
		// associate new
		setColor(color);
		// additional business logic
		onModifyColor(color);
	}

	public void clearColor() {
		// check for no-op
		if (getColor() == null) {
			return;
		}
		// dissociate existing
		setColor(null);
		// additional business logic
		onClearColor();
	}

	protected void onModifyColor(final Color color) {
	}

	protected void onClearColor() {
	}
	// }}


    // {{ Approver
    private Approver approver;
    @Disabled
    @MemberOrder(sequence = "5")
    public Approver getApprover() {
        return approver;
    }
    public void setApprover(Approver approver) {
        this.approver = approver;
    }
    // }}


    // {{ Items
    private List<ClaimItem> items = new ArrayList<ClaimItem>();
    @MemberOrder(sequence = "6")
    public List<ClaimItem> getItems() {
        return items;
    }
    public void addToItems(ClaimItem item) {
        items.add(item);
    }
    // }}

    
    // {{ action: Submit
    public void submit(Approver approver) {
        setStatus("Submitted");
        setApprover(approver);
    }
    public String disableSubmit() {
        return getStatus().equals("New") ? null : "Claim has already been submitted";
    }
    public Object[] defaultSubmit() {
        return new Object[] { getClaimant().getApprover() };
    }
    // }}
    
}
