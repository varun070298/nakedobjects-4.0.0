package org.nakedobjects.metamodel.testspec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.factory.InstanceFactory;
import org.nakedobjects.metamodel.commons.filters.Filter;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;
import org.nakedobjects.metamodel.consent.InteractionResult;
import org.nakedobjects.metamodel.facets.FacetHolderImpl;
import org.nakedobjects.metamodel.facets.object.immutable.ImmutableFacet;
import org.nakedobjects.metamodel.interactions.ObjectTitleContext;
import org.nakedobjects.metamodel.interactions.ObjectValidityContext;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.runtimecontext.noruntime.RuntimeContextNoRuntime;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.Persistability;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociationFilters;
import org.nakedobjects.metamodel.spec.feature.OneToManyAssociation;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;


public class TestProxySpecification extends FacetHolderImpl implements NakedObjectSpecification {

    private NakedObjectAction action;
    public NakedObjectAssociation[] fields = new NakedObjectAssociation[0];
    private final String name;
    private NakedObjectSpecification[] subclasses = new NakedObjectSpecification[0];
    private String title;

    private Persistability persistable;
    private boolean isEncodeable;
    private boolean hasNoIdentity;
	private RuntimeContextNoRuntime runtimeContext;

    public TestProxySpecification(final Class<?> type) {
        this(type.getName());
		runtimeContext = new RuntimeContextNoRuntime();
    }

    public TestProxySpecification(final String name) {
        this.name = name;
        title = "";
        persistable = Persistability.USER_PERSISTABLE;
    }

    public void addSubclass(final NakedObjectSpecification specification) {}

    public void clearDirty(final NakedObject object) {}

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void debugData(final DebugString debug) {}

    public String debugInterface() {
        return null;
    }

    public String debugTitle() {
        return "";
    }

    public NakedObjectAction getClassAction(
            final NakedObjectActionType type,
            final String name,
            final NakedObjectSpecification[] parameters) {
        return null;
    }

    public NakedObjectAction[] getServiceActionsFor(final NakedObjectActionType... type) {
        return null;
    }

    public int getFeatures() {
        return 0;
    }

    public boolean isAbstract() {
        return false;
    }

    public boolean isService() {
        return false;
    }

    public NakedObjectAssociation getAssociation(final String name) {
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getId().equals(name)) {
                return fields[i];
            }
        }
        throw new NakedObjectException("Field not found: " + name);
    }

    public NakedObjectAssociation[] getAssociations() {
        return fields;
    }

    public List<? extends NakedObjectAssociation> getAssociationList() {
        return Arrays.asList(fields);
    }

    @SuppressWarnings("unchecked")
    public List<OneToOneAssociation> getPropertyList() {
        List<OneToOneAssociation> list = new ArrayList<OneToOneAssociation>();
        List associationList = getAssociationList(NakedObjectAssociationFilters.PROPERTIES);
        list.addAll(associationList);
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<OneToManyAssociation> getCollectionList() {
        List<OneToManyAssociation> list = new ArrayList<OneToManyAssociation>();
        List associationList = getAssociationList(NakedObjectAssociationFilters.COLLECTIONS);
        list.addAll(associationList);
        return list;
    }

    public NakedObjectAssociation[] getAssociations(final Filter<NakedObjectAssociation> filter) {
        final NakedObjectAssociation[] allFields = getAssociations();

        final NakedObjectAssociation[] selectedFields = new NakedObjectAssociation[allFields.length];
        int v = 0;
        for (int i = 0; i < allFields.length; i++) {
            if (filter.accept(allFields[i])) {
                selectedFields[v++] = allFields[i];
            }
        }

        final NakedObjectAssociation[] fields = new NakedObjectAssociation[v];
        System.arraycopy(selectedFields, 0, fields, 0, v);
        return fields;
    }

    public List<? extends NakedObjectAssociation> getAssociationList(final Filter<NakedObjectAssociation> filter) {
        return Arrays.asList(getAssociations(filter));
    }

    public String getFullName() {
        return name;
    }

    public String getIconName(final NakedObject reference) {
        return null;
    }

    public NakedObjectAction getObjectAction(
            final NakedObjectActionType type,
            final String name,
            final NakedObjectSpecification[] parameters) {
        if (action != null && action.getId().equals(name)) {
            return action;
        }
        return null;
    }

    public NakedObjectAction getObjectAction(final NakedObjectActionType type, final String id) {
    	int openBracket = id.indexOf('(');
        return getObjectAction(type, id.substring(0, openBracket), null);
    }

    public NakedObjectAction[] getObjectActions(final NakedObjectActionType... types) {
        return null;
    }

    public List<? extends NakedObjectAction> getObjectActionList(final NakedObjectActionType... types) {
        return null;
    }

    public String getPluralName() {
        return null;
    }

    public String getShortName() {
        return name.substring(name.lastIndexOf('.') + 1);
    }

    public String getSingularName() {
        return name + " (singular)";
    }

    public String getDescription() {
        return getSingularName();
    }

    public String getTitle(final NakedObject naked) {
        return title;
    }

    public boolean hasSubclasses() {
        return false;
    }

    public NakedObjectSpecification[] interfaces() {
        return new NakedObjectSpecification[0];
    }

    public void introspect() {}

    public boolean isDirty(final NakedObject object) {
        return false;
    }

    public boolean isOfType(final NakedObjectSpecification cls) {
        return cls == this;
    }

    public boolean isEncodeable() {
        return isEncodeable;
    }

    public boolean isParseable() {
        return false;
    }

    public boolean isValueOrIsAggregated() {
        return false;
    }

    public void markDirty(final NakedObject object) {}

    public Object newInstance() {
        return InstanceFactory.createInstance(name);
    }

    public Persistability persistability() {
        return persistable;
    }

    public void setupAction(final NakedObjectAction action) {
        this.action = action;
    }

    public void setupFields(final NakedObjectAssociation[] fields) {
        this.fields = fields;
    }

    public void setupIsEncodeable() {
        isEncodeable = true;
    }

    public void setupSubclasses(final NakedObjectSpecification[] subclasses) {
        this.subclasses = subclasses;
    }

    public void setupHasNoIdentity(final boolean hasNoIdentity) {
        this.hasNoIdentity = hasNoIdentity;
    }

    public void setupTitle(final String title) {
        this.title = title;
    }

    public NakedObjectSpecification[] subclasses() {
        return subclasses;
    }

    public NakedObjectSpecification superclass() {
        return null;
    }

    @Override
    public String toString() {
        return getFullName();
    }

    public Consent isValid(final NakedObject transientObject) {
        return null;
    }

    public Object getDefaultValue() {
        return null;
    }

    public Identifier getIdentifier() {
        return Identifier.classIdentifier(name);
    }

    public boolean isCollectionOrIsAggregated() {
        return hasNoIdentity;
    }

    public Object createObject(CreationMode creationMode) {
        try {
            final Class<?> cls = Class.forName(name);
            return cls.newInstance();
        } catch (ClassNotFoundException e) {
            throw new NakedObjectException(e);
        } catch (InstantiationException e) {
            throw new NakedObjectException(e);
        } catch (IllegalAccessException e) {
            throw new NakedObjectException(e);
        }
    }

    public void setupPersistable(final Persistability persistable) {
        this.persistable = persistable;
    }

    public boolean isCollection() {
        return false;
    }

    public boolean isObject() {
        return !isCollection();
    }
    
    public boolean isImmutable() {
        return containsFacet(ImmutableFacet.class);
    }

    public ObjectValidityContext createValidityInteractionContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject targetNakedObject) {
        return null;
    }

    public ObjectTitleContext createTitleInteractionContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject targetNakedObject) {
        return null;
    }

    public InteractionResult isValidResult(final NakedObject transientObject) {
        return null;
    }

    // /////////////////////////////////////////////////////////////
    // getInstance
    // /////////////////////////////////////////////////////////////
    
    public NakedObject getInstance(NakedObject nakedObject) {
        return nakedObject;
    }

	public RuntimeContext getRuntimeContext() {
		return runtimeContext;
	}


}

// Copyright (c) Naked Objects Group Ltd.
