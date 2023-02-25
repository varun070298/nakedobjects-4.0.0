package org.nakedobjects.plugins.hibernate.objectstore.metamodel.criteria;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.query.Parameter;
import org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.query.QueryPlaceholder;
import org.nakedobjects.remoting.data.Data;
import org.nakedobjects.remoting.data.query.PersistenceQueryData;
import org.nakedobjects.remoting.exchange.KnownObjectsRequest;
import org.nakedobjects.remoting.protocol.encoding.internal.PersistenceQueryEncoderAbstract;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;


public class HibernateQueryEncoder extends PersistenceQueryEncoderAbstract {

    public Class<HibernateQueryCriteria> getPersistenceQueryClass() {
        return HibernateQueryCriteria.class;
    }

    public PersistenceQueryData encode(final PersistenceQuery criteria) {
        final HibernateQueryCriteria hibernateCriteria = downcast(criteria);
        final List<Parameter> parms = hibernateCriteria.getQuery().getParameters();
        final NakedObjectSpecification[] specs = new NakedObjectSpecification[parms.size()];
        final NakedObject[] objects = new NakedObject[parms.size()];
        for (int i = 0; i < parms.size(); i++) {
            final Parameter parm = parms.get(i);
            final NakedObject nakedObject = getAdapterManager().getAdapterFor(parm.getValue());
            specs[i] = nakedObject.getSpecification();
            objects[i] = nakedObject;
        }
        final Data[] objectData = getObjectEncoder().encodeActionParameters(specs, objects, new KnownObjectsRequest());
        final byte[] serialisedQuery = serialise(hibernateCriteria.getQuery());
        return new HibernateQueryData(downcast(criteria), serialisedQuery, objectData);
    }

    // REVIEW serialise query inline here as part of strategy rather than in Marshaller - correct ?
    private byte[] serialise(final Object toSerialise) {
        try {
            final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            final ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
            objectStream.writeObject(toSerialise);
            objectStream.flush();
            return byteStream.toByteArray();
        } catch (final Exception e) {
            throw new NakedObjectException(e);
        }
    }

    private Object deSerialise(final byte[] bytes) {
        try {
            final ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
            final ObjectInputStream objectStream = new ObjectInputStream(byteStream);
            return objectStream.readObject();
        } catch (final Exception e) {
            throw new NakedObjectException(e);
        }
    }

    @Override
    protected PersistenceQuery doDecode(
            final NakedObjectSpecification specification,
            final PersistenceQueryData persistenceQueryData) {

        final HibernateQueryData queryData = downcast(persistenceQueryData);
        final QueryPlaceholder query = (QueryPlaceholder) deSerialise(queryData.getQueryAsBytes());

        for (int i = 0; i < queryData.getData().length; i++) {
            final Parameter parm = query.getParameters().get(i);
            final Data data = queryData.getData()[i];
            final NakedObject nakedObject = getObjectEncoder().decode(data);
            parm.setValue(nakedObject.getObject());
        }

        return new HibernateQueryCriteria(persistenceQueryData.getPersistenceQueryClass(), query, (downcast(persistenceQueryData))
                .getResultType());
    }

	private HibernateQueryCriteria downcast(final PersistenceQuery criteria) {
		return (HibernateQueryCriteria) criteria;
	}

	private HibernateQueryData downcast(
			final PersistenceQueryData persistenceQueryData) {
		return (HibernateQueryData) persistenceQueryData;
	}
    
    
    //////////////////////////////////////////////////////////
    // Dependencies (from context)
    //////////////////////////////////////////////////////////
    
    private static PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }

    private static AdapterManager getAdapterManager() {
        return getPersistenceSession().getAdapterManager();
    }
    

}
