package org.nakedobjects.example.expenses.services.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.nakedobjects.plugins.hibernate.objectstore.metamodel.criteria.HibernateFactoryAndRepository;
import org.nakedobjects.plugins.hibernate.objectstore.metamodel.criteria.HibernateInstancesCriteria;


/**
 * Prototype for a Hibernate Helper class to be added to app lib.
 * 
 * @author Richard
 * 
 */
public class HibernateHelper extends HibernateFactoryAndRepository {

    /**
     * Create a Hibernate Criteria for the class persisted by this repository
     */
    @Override
    protected Criteria createCriteria(final Class<?> cls) {
        return super.createCriteria(cls);
    }

    /**
     * Create a Hibernate Query using a complete query string. This can be used to query any entity.
     */
    @Override
    protected Query createQuery(final String query) {
        return super.createQuery(query);
    }

    /**
     * Return a named Hibernate Query which is defined in hibernate.cfg.xml.
     * 
     * @param name
     *            the Query name
     */
    @Override
    protected Query getNamedQuery(final String name) {
        return super.getNamedQuery(name);
    }

    /**
     * A shortcut for creating a count(*) Query on the class for this repository. The query generated is
     * 
     * <pre>
     * select count(*) from &lt;cls&gt; as o {where &lt;whereClause&gt;}
     * </pre>
     * 
     * The whereClause is optional.
     * <p>
     * To generate more complex queries, possibly using other classes, use the {@link #createQuery(String)}}
     * method
     * 
     * @param whereClause
     *            the where clause, not including "where", or null to count all instances.
     */
    @Override
    protected Query createCountQuery(final String whereClause, final Class<?> cls) {
        return super.createCountQuery(whereClause, cls);
    }

    /**
     * A shortcut for creating a Query on the class for this repository. The query generated is
     * 
     * <pre>
     * from &lt;cls&gt; as o where &lt;whereClause&gt;
     * </pre>
     * 
     * The whereClause must be specified, to select all instances use {@link #allInstances()}.
     * <p>
     * To generate more complex queries, possibly using other classes, use {@link #createQuery(String)}}.
     * 
     * @param whereClause
     *            the where clause, not including "where"
     */
    @Override
    protected Query createEntityQuery(final String whereClause, final Class<?> cls) {
        return super.createEntityQuery(whereClause, cls);
    }

    @Override
    protected List findByCriteria(final Criteria criteria, final Class<?> cls) {
        return super.findByCriteria(criteria, cls);
    }

    @Override
    protected Object findUniqueResultByCriteria(final Criteria criteria, final Class<?> cls) {
        return super.findUniqueResultByCriteria(criteria, cls);
    }

    @Override
    protected Object findUniqueResultByQuery(final Query query, final Class<?> cls) {
        return super.findUniqueResultByQuery(query, cls);
    }

    @Override
    protected Object findFirstByCriteria(final Criteria criteria, final Class<?> cls) {
        return super.findFirstByCriteria(criteria, cls);
    }

    @Override
    protected List findByQuery(final Query query, final Class<?> cls) {
        return super.findByQuery(query, cls);
    }

    @Override
    protected Object findFirstByQuery(final Query query, final Class<?> cls) {
        return super.findFirstByQuery(query, cls);
    }

    @Override
    protected List findByQuery(final String query, final Class<?> cls) {
        return super.findByQuery(createQuery(query), cls);
    }

    @Override
    protected Object findFirstByQuery(final String query, final Class<?> cls) {
        return super.findFirstByQuery(createQuery(query), cls);
    }

    /**
     * Return the first object in a list. If the array is null, or is empty, then return null.
     */
    @Override
    protected Object getFirst(final List list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    /**
     * Return true if the Query returns an integer > 0. The Query MUST return a single value, which must be an
     * integer, e.g. "select count(*) from myClass". This is a utility method to help with common repository
     * usage.
     */
    @Override
    protected boolean countNotZero(final Query query) {
        final int count = ((Integer) query.uniqueResult()).intValue();
        return count > 0;
    }

    @Override
    protected List findByCriteria(final HibernateInstancesCriteria criteria, final Class<?> cls) {
        return super.findByCriteria(criteria, cls);
    }

}
