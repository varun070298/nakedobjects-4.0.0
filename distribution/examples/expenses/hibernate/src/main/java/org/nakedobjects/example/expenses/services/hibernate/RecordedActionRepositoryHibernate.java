package org.nakedobjects.example.expenses.services.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.example.expenses.recordedAction.RecordedActionContext;
import org.nakedobjects.example.expenses.recordedAction.impl.RecordedAction;
import org.nakedobjects.example.expenses.recordedAction.impl.RecordedActionRepositoryAbstract;


public class RecordedActionRepositoryHibernate extends RecordedActionRepositoryAbstract {
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
    public void setHibernateHelper(final HibernateHelper hibernateHelper) {
        this.hibernateHelper = hibernateHelper;
    }

    // }}

    @Hidden
    public List<RecordedAction> allRecordedActions(final RecordedActionContext context) {
        final Criteria criteria = hibernateHelper.createCriteria(RecordedAction.class);
        criteria.add(Restrictions.eq("context", context));
        return hibernateHelper.findByCriteria(criteria, RecordedAction.class);
    }
}
