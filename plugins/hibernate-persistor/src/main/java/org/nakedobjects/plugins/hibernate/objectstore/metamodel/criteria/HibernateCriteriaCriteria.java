package org.nakedobjects.plugins.hibernate.objectstore.metamodel.criteria;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.impl.CriteriaImpl;
import org.nakedobjects.metamodel.commons.ensure.Assert;


/**
 * InstancesCriteria based on a Hibernate Criteria
 */
public class HibernateCriteriaCriteria extends HibernateInstancesCriteria {
    private final CriteriaImpl criteria;

    public HibernateCriteriaCriteria(final Class<?> cls, final Criteria criteria, final int resultType) {
        super(cls, resultType);
        Criteria rootCriteria = criteria;
        while (!(rootCriteria instanceof CriteriaImpl)) {
            Assert.assertTrue(rootCriteria instanceof CriteriaImpl.Subcriteria);
            rootCriteria = ((CriteriaImpl.Subcriteria) rootCriteria).getParent();
            Assert.assertFalse(criteria == rootCriteria); // if circular reference could loop forever
        }
        this.criteria = (CriteriaImpl) rootCriteria;
    }

    @Override
    public void setSession(final Session session) {
        Assert.assertTrue(session instanceof SessionImplementor);
        criteria.setSession((SessionImplementor) session);
    }

    public Criteria getCriteria() {
        return criteria;
    }

    @Override
    public List<?> getResults() {
        switch (getResultType()) {
        case LIST: {
            return criteria.list();
        }
        case UNIQUE_RESULT: {
            final Object result = criteria.uniqueResult();
            return result == null ? Collections.EMPTY_LIST : Arrays.asList(new Object[] { result });
        }
        default:
            throw new RuntimeException("Result type out of range");
        }
    }
}
// Copyright (c) Naked Objects Group Ltd.
