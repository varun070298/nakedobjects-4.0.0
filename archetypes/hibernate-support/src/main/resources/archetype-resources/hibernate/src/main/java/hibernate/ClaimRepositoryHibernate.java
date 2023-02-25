package ${package}.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.nakedobjects.applib.annotation.Named;
import ${package}.service.ClaimRepository;
import ${package}.dom.Claim;
import ${package}.dom.Employee;


public class ClaimRepositoryHibernate extends ClaimRepository {

    // {{ Injected Services
    /*
     * This region contains references to the services (Repositories, Factories or other Services) used by
     * this domain object. The references are injected by the application container.
     */

    // {{ Injected: HibernateHelper
    private HibernateHelper hibernateHelper;

    /**
     * This field is not persisted, nor displayed to the user.
     */
    protected HibernateHelper getHibernateHelper() {
        return this.hibernateHelper;
    }

    /**
     * Injected by the application container.
     */
    public void setHibernateHelper(HibernateHelper hibernateHelper) {
        this.hibernateHelper = hibernateHelper;
    }

    // }}

    // }}

	final static int MAX_CLAIMS = 10;


    public List<Claim> findClaims(@Named("Description") String description) {
        final Criteria criteria = hibernateHelper.createCriteria(Claim.class);
	     criteria.add(Restrictions.like("description", description, MatchMode.ANYWHERE));
        criteria.addOrder(Order.desc("dateCreated"));        List<Claim> foundClaims = hibernateHelper.findByCriteria(criteria, Employee.class);
        if (foundClaims.size() > MAX_CLAIMS) {
            warnUser("Too many claims found - refine search");
            return null;
        } else if (foundClaims.size() == 0) {
            informUser("No claims found");
        }
		  return foundClaims;
    }
    
    public List<Claim> claimsFor(@Named("Claimant") Employee claimant) {
        final Criteria criteria = hibernateHelper.createCriteria(Claim.class);
        criteria.add(Restrictions.eq("claimant", claimant));
        criteria.addOrder(Order.desc("dateCreated"));
        List<Claim> foundClaims = hibernateHelper.findByCriteria(criteria, Employee.class);
        if (foundClaims.size() > MAX_CLAIMS) {
            warnUser("Too many claims found - refine search");
            return null;
        } else if (foundClaims.size() == 0) {
            informUser("No claims found");
        }
        return foundClaims;
    }

}
