package org.nakedobjects.plugins.hibernate.objectstore.metamodel.criteria;

import org.nakedobjects.metamodel.commons.exceptions.NotYetImplementedException;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.remoting.data.query.PersistenceQueryData;
import org.nakedobjects.remoting.protocol.encoding.internal.PersistenceQueryEncoderAbstract;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;


// REVIEW this creates a dependency between hibernate and remote command - should it live 
// else where ?
public class HibernateCriteriaEncoder extends PersistenceQueryEncoderAbstract {

    public PersistenceQueryData encode(final PersistenceQuery criteria) {

        // HibernateCriteriaCriteria hibernateCriteria = (HibernateCriteriaCriteria) criteria;
        // SerializedObjectData objectData = new SerializedObjectData();
        // objectData.setData(encode(hibernateCriteria.getCriteria()));
        // objectData.setType(hibernateCriteria.getCriteria().getClass().getName());
        // return new HibernateCriteriaData((HibernateCriteriaCriteria) criteria, objectData);

        throw new NotYetImplementedException();
    }

    @Override
    protected PersistenceQuery doDecode(
            final NakedObjectSpecification specification,
            final PersistenceQueryData criteriaData) {
        // SerializedObjectData patternData = ((HibernateCriteriaData) criteriaData).getData();
        // Criteria criteria = (Criteria) decode(patternData);
        // return new HibernateCriteriaCriteria(((HibernateCriteriaData) criteriaData).getClass(), criteria,
        // ((HibernateCriteriaData) criteriaData).getResultType());
        throw new NotYetImplementedException();
    }

    public Class<HibernateCriteriaCriteria> getPersistenceQueryClass() {
        return HibernateCriteriaCriteria.class;
    }
}
