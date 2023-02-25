package org.nakedobjects.runtime.persistence.services;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;


public final class SimpleRepository {

    private final Class<?> type;
    private NakedObjectSpecification spec;

    public SimpleRepository(final Class<?> cls) {
        this.type = cls;
    }

    public String getId() {
        return "repository#" + getClass().getName();
    }

    public String iconName() {
        return getSpec().getShortName();
    }

    public String title() {
        return getSpec().getPluralName();
    }

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("class", type.getName());
        return str.toString();
    }

    // //////////////////////////////////////////////////////
    // allInstances
    // //////////////////////////////////////////////////////

    public Object[] allInstances() {
        return RepositoryHelper.allInstances(getSpec(), type);
    }

    public String disableAllInstances() {
        return hasInstances() ? null : "No " + getSpec().getPluralName();
    }

    // //////////////////////////////////////////////////////
    // allInstances
    // //////////////////////////////////////////////////////

    public Object[] findByTitle(final String title) {
        return RepositoryHelper.findByTitle(getSpec(), type, title).toArray();
    }

    public String disableFindByTitle() {
        return disableAllInstances();
    }

    public static String[] parameterNamesFindByTitle() {
        return new String[] { "Title to find" };
    }

    public static boolean[] parametersRequiredFindByTitle() {
        return new boolean[] { true };
    }

    private boolean hasInstances() {
        return RepositoryHelper.hasInstances(getSpec());
    }

    // //////////////////////////////////////////////////////
    // newPersistentInstance
    // //////////////////////////////////////////////////////

    public Object newPersistentInstance() {
        final NakedObject adapter = getPersistenceSession().createInstance(getSpec());
        getPersistenceSession().makePersistent(adapter);
        return adapter.getObject();
    }

    // //////////////////////////////////////////////////////
    // newTransientInstance
    // //////////////////////////////////////////////////////

    public Object newTransientInstance() {
        return getPersistenceSession().createInstance(getSpec()).getObject();
    }

    // //////////////////////////////////////////////////////
    // getSpec (hidden)
    // //////////////////////////////////////////////////////

    protected NakedObjectSpecification getSpec() {
        if (spec == null) {
            spec = NakedObjectsContext.getSpecificationLoader().loadSpecification(type);
        }
        return spec;
    }

    public static boolean alwaysHideSpec() {
        return true;
    }


    // //////////////////////////////////////////////////////
    // Dependencies (from singleton)
    // //////////////////////////////////////////////////////

    private PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }

}

// Copyright (c) Naked Objects Group Ltd.
