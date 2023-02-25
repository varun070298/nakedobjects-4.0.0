package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.session;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.impl.CriteriaImpl;
import org.nakedobjects.metamodel.commons.exceptions.NotYetImplementedException;
import org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.query.QueryPlaceholder;


/**
 * An implementation of {@link Session} and {@link SessionImplementor} that
 * is mostly throws {@link NotYetImplementedException} except for those methods
 * pertaining to creating {@link Criteria} and {@link Query}.
 */
public class SessionPlaceHolder extends SessionPlaceHolderNotImplemented {

    private static final long serialVersionUID = 1L;


    @Override
    @SuppressWarnings("unchecked")
    public Criteria createCriteria(final Class persistentClass, final String alias) {
        return new CriteriaImpl(persistentClass.getName(), alias, this);
    }

    @Override
    public Criteria createCriteria(final String entityName, final String alias) {
        return new CriteriaImpl(entityName, alias, this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Criteria createCriteria(final Class persistentClass) {
        return new CriteriaImpl(persistentClass.getName(), this);
    }

    @Override
    public Criteria createCriteria(final String entityName) {
        return new CriteriaImpl(entityName, this);
    }

    @Override
    public Query createQuery(final String queryString) throws HibernateException {
        final Query query = new QueryPlaceholder(queryString);
        query.setComment(queryString);
        return query;
    }

    
    
    
}
