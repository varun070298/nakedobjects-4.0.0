package org.nakedobjects.example.expenses.claims;

import org.nakedobjects.applib.AbstractService;
import org.nakedobjects.applib.ApplicationException;
import org.nakedobjects.applib.annotation.MemberOrder;
import org.nakedobjects.applib.annotation.Named;
import org.nakedobjects.applib.annotation.Optional;
import org.nakedobjects.example.expenses.employee.Employee;
import org.nakedobjects.example.expenses.services.UserFinder;

import java.util.List;


/**
 * Defines the user actions available on the 'Claims' desktop icon (or tab).
 * 
 * @author Richard
 * 
 */
public class Claims extends AbstractService {

    @Override
    public String getId() {
        return "Claims";
    }

    // {{ Title & Icon
    public String title() {
        return "Expense Claims";
    }

    public String iconName() {
        return Claim.class.getSimpleName();
    }
    // }}

    // {{ Injected Services
    /*
     * This region contains references to the services (Repositories, Factories or other Services) used by
     * this domain object. The references are injected by the application container.
     */

    // {{ Injected: UserFinder
    private UserFinder userFinder;

    /**
     * This field is not persisted, nor displayed to the user.
     */
    protected UserFinder getUserFinder() {
        return this.userFinder;
    }

    /**
     * Injected by the application container.
     */
    public void setUserFinder(final UserFinder userFinder) {
        this.userFinder = userFinder;
    }
    // }}

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

    // {{ Injected: ClaimFactory
    private ClaimFactory claimFactory;

    /**
     * This field is not persisted, nor displayed to the user.
     */
    protected ClaimFactory getClaimFactory() {
        return this.claimFactory;
    }

    /**
     * Injected by the application container.
     */
    public void setClaimFactory(final ClaimFactory claimFactory) {
        this.claimFactory = claimFactory;
    }

    // }}

    // }}

    @MemberOrder(sequence = "2")
    public List<Claim> findMyClaims(@Named("Status")
    @Optional
    final ClaimStatus status, @Optional
    @Named("Description")
    final String description) {
        return claimRepository.findClaims(meAsEmployee(), status, description);
    }

    @MemberOrder(sequence = "1")
    public List<Claim> myRecentClaims() {
        return claimRepository.findRecentClaims(meAsEmployee());
    }

    private Employee meAsEmployee() {
        final Object user = userFinder.currentUserAsObject();
        if (user instanceof Employee) {
            return (Employee) user;
        }
        throw new ApplicationException("Current user is not an Employee");
    }

    @MemberOrder(sequence = "3")
    public Claim createNewClaim(@Named("Description")
    final String description) {
        return claimFactory.createNewClaim(meAsEmployee(), description);
    }

    public String default0CreateNewClaim() {
        return claimFactory.defaultUniqueClaimDescription(meAsEmployee());
    }

    @MemberOrder(sequence = "6")
    public List<Claim> claimsAwaitingMyApproval() {
        return claimRepository.findClaimsAwaitingApprovalBy(meAsEmployee());
    }

}
