package org.nakedobjects.plugins.hibernate.objectstore.metamodel.criteria;

import org.nakedobjects.remoting.data.Data;
import org.nakedobjects.remoting.data.query.PersistenceQueryDataAbstract;


public class HibernateQueryData extends PersistenceQueryDataAbstract {

    private static final long serialVersionUID = 1L;
    private final int resultType;
    private final Data[] objectData;
    private final byte[] queryAsBytes;

    public HibernateQueryData(final HibernateQueryCriteria criteria, final byte[] queryAsBytes, final Data[] data) {
        super(criteria.getSpecification());
        this.objectData = data;
        this.queryAsBytes = queryAsBytes;
        resultType = criteria.getResultType();
    }

    public Data[] getData() {
        return objectData;
    }

    public Class<HibernateQueryCriteria> getPersistenceQueryClass() {
        return HibernateQueryCriteria.class;
    }

    public int getResultType() {
        return resultType;
    }

    public byte[] getQueryAsBytes() {
        return queryAsBytes;
    }
}
