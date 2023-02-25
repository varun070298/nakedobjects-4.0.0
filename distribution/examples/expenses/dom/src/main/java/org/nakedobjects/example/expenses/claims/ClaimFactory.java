package org.nakedobjects.example.expenses.claims;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.nakedobjects.applib.AbstractFactoryAndRepository;
import org.nakedobjects.applib.ApplicationException;
import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.example.expenses.employee.Employee;


/**
 * Contains common logic for creating a new Claim, which may be called from several contexts.
 * 
 */
public class ClaimFactory extends AbstractFactoryAndRepository {

    // {{ Injected Services
    /*
     * This region contains references to the services (Repositories, Factories or other Services) used by
     * this domain object. The references are injected by the application container.
     */

    // {{ Injected: ClaimRepository
    private ClaimRepository claimRepository;

    /**
     * This field is not persisted, nor displayed to the user.
     */
    protected ClaimRepository getClaimRepository() {
        return this.claimRepository;
    }

    /**
     * Injected by the application container.
     */
    public void setClaimRepository(final ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
    }

    // }}

    // }}

    @Hidden
    public Claim createNewClaim(final Employee employee, final String description) {
        final Claim claim = newTransientInstance(Claim.class);
        claim.setClaimant(employee);
        claim.setApprover(employee.getNormalApprover());
        claim.initialiseTotal();
        claim.setDescription(createUniqueDescription(employee, description));
        persist(claim);
        claim.changeStatusToNew();
        return claim;
    }

    @Hidden
    public String defaultUniqueClaimDescription(final Employee employee) {
        return createUniqueDescription(employee, createDefaultClaimDescription(null));
    }

    public String createDefaultClaimDescription(final String inputDescription) {
        if (inputDescription == null || inputDescription.length() == 0) {
            return new SimpleDateFormat("dd-MMM-yy").format(new Date());
        }
        return inputDescription;
    }

    private String createUniqueDescription(final Employee employee, final String initialDescription) {
        int increment = 2;
        String description = initialDescription;
        while (!claimRepository.descriptionIsUniqueForClaimant(employee, description)) {
            description = initialDescription + CLAIM_DIFFERENTIATOR + increment++;
        }
        return description;
    }

    public static final String CLAIM_DIFFERENTIATOR = " - ";

    @Hidden
    public ExpenseItem createNewExpenseItem(final AbstractClaim claim, final ExpenseType type) {
        try {
            final ExpenseItem item = newTransientInstance(classFor(type));
            item.setExpenseType(type);
            item.modifyProjectCode(claim.getProjectCode());
            item.setClaim(claim);
            item.initialiseAmount();
            return item;
        } catch (final ClassNotFoundException e) {
            throw new ApplicationException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private Class<ExpenseItem> classFor(final ExpenseType type) throws ClassNotFoundException {
        return (Class<ExpenseItem>) Class.forName(type.getCorrespondingClassName());
    }

    @Hidden
    public void makePersistent(final ExpenseItem transientExpenseItem) {
        super.persist(transientExpenseItem);
    }

}
