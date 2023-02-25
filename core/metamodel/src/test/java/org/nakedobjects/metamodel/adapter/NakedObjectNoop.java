package org.nakedobjects.metamodel.adapter;

import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.metamodel.facets.actcoll.typeof.TypeOfFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.Specification;


/**
 * Has no functionality but makes it easier to write tests that require an instance of an {@link NakedObject}.
 */
public class NakedObjectNoop implements NakedObject {

    public void changeState(final ResolveState newState) {}

    public void checkLock(final Version version) {}

    public void fireChangedEvent() {}

    public String getIconName() {
        return null;
    }

    public Object getObject() {
        return null;
    }

    public Oid getOid() {
        return null;
    }

    public ResolveState getResolveState() {
        return null;
    }

    public NakedObjectSpecification getSpecification() {
        return null;
    }

    public Version getVersion() {
        return null;
    }

    public void replacePojo(final Object pojo) {}

    public void setOptimisticLock(final Version version) {}

    public String titleString() {
        return null;
    }

    public TypeOfFacet getTypeOfFacet() {
        return null;
    }

    public void setTypeOfFacet(final TypeOfFacet typeOfFacet) {}

    public NakedObject getOwner() {
        return null;
    }

    public Instance getInstance(Specification specification) {
        return null;
    }

    public boolean isAggregated() {
        return false;
    }

	public boolean isPersistent() {
		return false;
	}

	public boolean isTransient() {
		return false;
	}



}

// Copyright (c) Naked Objects Group Ltd.
