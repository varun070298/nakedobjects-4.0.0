package org.nakedobjects.plugins.hibernate.objectstore.metamodel.criteria;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hibernate.Session;
import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.query.QueryPlaceholder;


/**
 * Criteria based on a Hibernate Query
 */
public class HibernateQueryCriteria extends HibernateInstancesCriteria {
    private final QueryPlaceholder query;
    private transient Session session;

    public HibernateQueryCriteria(final Class<?> cls, final QueryPlaceholder query, final int resultType) {
        super(cls, resultType);
        this.query = query;
    }

    @Override
    public void setSession(final Session session) {
        this.session = session;
    }

    @Override
    public List<?> getResults() {
        Assert.assertNotNull(session);
        query.setSession(session);
        switch (getResultType()) {
        case LIST: {
            return query.list();
        }
        case UNIQUE_RESULT: {
            final Object result = query.uniqueResult();
            return result == null ? Collections.EMPTY_LIST : Arrays.asList(new Object[] { result });
        }
        default:
            throw new RuntimeException("Result type out of range");
        }
    }

    public QueryPlaceholder getQuery() {
        return query;
    }

}
// Copyright (c) Naked Objects Group Ltd.
