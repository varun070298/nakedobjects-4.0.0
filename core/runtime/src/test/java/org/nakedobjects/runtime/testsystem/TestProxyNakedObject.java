package org.nakedobjects.runtime.testsystem;

import java.util.Hashtable;

import org.nakedobjects.metamodel.adapter.Instance;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.exceptions.NotYetImplementedException;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.metamodel.facets.actcoll.typeof.TypeOfFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.Specification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;
import org.nakedobjects.runtime.persistence.ConcurrencyException;


public class TestProxyNakedObject implements NakedObject {
    private final Hashtable fieldContents = new Hashtable();
    private Object object;
    private Oid oid;
    private NakedObjectSpecification spec;
    private ResolveState state;
    private String titleString = "default title string";
    private Version version;
    private static int next;
    private final int id = next++;
    private String iconName;

    public void checkLock(final Version version) {
        if (this.version.different(version)) {
            throw new ConcurrencyException("", getOid());
        }
    }

    public NakedObject getField(final NakedObjectAssociation field) {
        return (NakedObject) fieldContents.get(field.getId());
    }

    public String getIconName() {
        return iconName;
    }

    public Object getObject() {
        return object;
    }

    public Oid getOid() {
        return oid;
    }

    public ResolveState getResolveState() {
        return state;
    }


	public boolean isPersistent() {
		return getResolveState().isPersistent();
	}

	public boolean isTransient() {
		return getResolveState().isTransient();
	}

    public NakedObjectSpecification getSpecification() {
        return spec;
    }

    public Version getVersion() {
        return version;
    }

    public void setOptimisticLock(final Version version) {
        this.version = version;
    }

    public void setupFieldValue(final String name, final NakedObject field) {
        this.fieldContents.put(name, field);
    }

    public void setupIconName(final String iconName) {
        this.iconName = iconName;
    }

    public void setupObject(final Object object) {
        if (object instanceof NakedObject) {
            throw new NakedObjectException("can't create a naked object for a naked object: " + object.toString());
        }
        this.object = object;
    }

    public void setupOid(final Oid oid) {
        this.oid = oid;
    }

    public void setupResolveState(final ResolveState state) {
        this.state = state;
    }

    public void setupSpecification(final NakedObjectSpecification spec) {
        this.spec = spec;
    }

    public void setupTitleString(final String titleString) {
        this.titleString = titleString;
    }

    public void setupVersion(final Version version) {
        this.version = version;
    }

    public void setValue(final OneToOneAssociation field, final Object object) {}

    public String titleString() {
        return titleString;
    }

    @Override
    public String toString() {
        final ToString str = new ToString(this, id);
        str.append("title", titleString);
        str.append("oid", oid);
        str.append("pojo", object);
        return str.toString();
    }

    public void changeState(final ResolveState state) {
        this.state.isValidToChangeTo(state);
        this.state = state;
    }

    public void replacePojo(final Object pojo) {
        throw new NotYetImplementedException();
    }

    public void fireChangedEvent() {}

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

}
// Copyright (c) Naked Objects Group Ltd.
