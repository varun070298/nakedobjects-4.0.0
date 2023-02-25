package org.nakedobjects.plugins.hibernate.objectstore.metamodel.criteria;

import java.util.List;

import org.hibernate.Session;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;


/**
 * Superclass of all InstancesCriteria which use Hibernate to access the database.
 */
public abstract class HibernateInstancesCriteria implements PersistenceQuery {
    public static final int LIST = 0;
    public static final int UNIQUE_RESULT = 1;

    private final Class<?> type;
    private final int resultType;

    public HibernateInstancesCriteria(final Class<?> type, final int resultType) {
        this.resultType = resultType;
        this.type = type;
    }

    /**
     * provided for serialization purposes only.
     */
    protected HibernateInstancesCriteria() {
        type = null;
        resultType = LIST;
    }

    public NakedObjectSpecification getSpecification() {
        return NakedObjectsContext.getSpecificationLoader().loadSpecification(type);
    }

    /**
     * Not required as this will be in the Hibernate query/criteria
     */
    public boolean includeSubclasses() {
        return false;
    }

    /**
     * Not required as this will be decided by the Hibernate query/criteria
     */
    public boolean matches(final NakedObject object) {
        return false;
    }

    /**
     * Return the results of executing the Hibernate query.
     * 
     * @return a List of persistent Objects
     */
    public abstract List<?> getResults();

    public abstract void setSession(final Session session);

    public int getResultType() {
        return resultType;
    }

    protected Class<?> getCls() {
        return type;
    }

}
// Copyright (c) Naked Objects Group Ltd.
