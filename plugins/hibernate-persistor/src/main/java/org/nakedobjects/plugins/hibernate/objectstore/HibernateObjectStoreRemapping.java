package org.nakedobjects.plugins.hibernate.objectstore;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.plugins.hibernate.objectstore.util.HibernateUtil;
import org.nakedobjects.runtime.persistence.ObjectNotFoundException;
import org.nakedobjects.runtime.persistence.objectstore.NakedObjectStoreDelegating;
import org.nakedobjects.runtime.persistence.objectstore.ObjectStore;
import org.nakedobjects.runtime.persistence.objectstore.transaction.CreateObjectCommand;
import org.nakedobjects.runtime.persistence.objectstore.transaction.DestroyObjectCommand;
import org.nakedobjects.runtime.persistence.objectstore.transaction.SaveObjectCommand;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;
import org.nakedobjects.runtime.transaction.ObjectPersistenceException;


/**
 * Wraps a {@link HibernateObjectStore} to ensure objects are mapped, even if they aren't specified within the
 * properties file or picked up from related objects.
 * 
 * <p>
 * This class is useful when initially setting up a project, as it will tell you which entity classes 
 * aren't listed in the properties and would cause a failure using the standard {@link HibernateObjectStore}.
 */
public class HibernateObjectStoreRemapping extends NakedObjectStoreDelegating {

    public HibernateObjectStoreRemapping(final ObjectStore decorated) {
        super(decorated, "RemappingHibernateObjectStore");
    }
    
    //////////////////////////////////////////////////
    // createXxxCommand
    //////////////////////////////////////////////////

    @Override
    public CreateObjectCommand createCreateObjectCommand(final NakedObject object) {
        ensureMapped(object);
        return super.createCreateObjectCommand(object);
    }

    @Override
    public SaveObjectCommand createSaveObjectCommand(final NakedObject object) {
        ensureMapped(object);
        return super.createSaveObjectCommand(object);
    }

    @Override
    public DestroyObjectCommand createDestroyObjectCommand(final NakedObject object) {
        ensureMapped(object);
        return super.createDestroyObjectCommand(object);
    }

    //////////////////////////////////////////////////
    // getObject, resolveField, resolveImmediately
    //////////////////////////////////////////////////

    @Override
    public NakedObject getObject(final Oid oid, final NakedObjectSpecification hint) throws ObjectNotFoundException,
            ObjectPersistenceException {
        ensureMapped(hint);
        return super.getObject(oid, hint);
    }

    @Override
    public void resolveField(final NakedObject object, final NakedObjectAssociation field) {
        ensureMapped(object);
        if (field.isOneToOneAssociation() || field.isOneToManyAssociation()) {
            ensureMapped(field.getSpecification());
        }
        super.resolveField(object, field);
    }

    public void resolveImmediately(final NakedObject object) {
        ensureMapped(object);
        super.resolveImmediately(object);
    }

    //////////////////////////////////////////////////
    // getInstances, hasInstances
    //////////////////////////////////////////////////

    public NakedObject[] getInstances(final PersistenceQuery criteria) {
        ensureMapped(criteria.getSpecification());
        return super.getInstances(criteria);
    }

    @Override
    public boolean hasInstances(final NakedObjectSpecification specification) {
        ensureMapped(specification);
        return super.hasInstances(specification);
    }


    //////////////////////////////////////////////////
    // Helpers: ensureMapped
    //////////////////////////////////////////////////

    private void ensureMapped(final NakedObject object) {
        ensureMapped(object.getSpecification());
    }

    private void ensureMapped(final NakedObjectSpecification specification) {
        HibernateUtil.ensureMapped(specification);
    }


}
// Copyright (c) Naked Objects Group Ltd.
