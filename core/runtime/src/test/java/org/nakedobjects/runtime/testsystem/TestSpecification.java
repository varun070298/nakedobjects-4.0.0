package org.nakedobjects.runtime.testsystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.adapter.Instance;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.exceptions.NotYetImplementedException;
import org.nakedobjects.metamodel.commons.filters.Filter;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;
import org.nakedobjects.metamodel.consent.InteractionResult;
import org.nakedobjects.metamodel.facets.FacetHolderNoop;
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
import org.nakedobjects.metamodel.testspec.TestProxySpecification;


/**
 * @deprecated replaced by {@link TestProxySpecification}
 */
@Deprecated
public class TestSpecification extends FacetHolderNoop implements NakedObjectSpecification {

    private static int next = 100;
    private NakedObjectAction action;
    public NakedObjectAssociation[] fields = new NakedObjectAssociation[0];
    private final int id = next++;
    private final String name;
    private NakedObjectSpecification[] subclasses = new NakedObjectSpecification[0];
    private String title;
	private RuntimeContext runtimeContext;

    public TestSpecification() {
        this((String) null);
        this.runtimeContext = new RuntimeContextNoRuntime();
    }

    public TestSpecification(final Class<?> cls) {
        this(cls.getName());
    }

    public TestSpecification(final String name) {
        this.name = name == null ? "DummyNakedObjectSpecification#" + id : name;
        title = "";
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

    public NakedObject getAggregate(final NakedObject object) {
        return null;
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

    public Object getFieldExtension(final String name, final Class<?> cls) {
        return null;
    }

    public Class<?>[] getFieldExtensions(final String name) {
        return new Class[0];
    }

    public NakedObjectAssociation[] getAssociations() {
        return fields;
    }

    public List<? extends NakedObjectAssociation> getAssociationList() {
        return Arrays.asList(getAssociations());
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
        return getObjectAction(type, id);
    }

    public NakedObjectAction[] getObjectActions(final NakedObjectActionType... type) {
        return null;
    }

    public List<? extends NakedObjectAction> getObjectActionList(final NakedObjectActionType... type) {
        return null;
    }

    public String getPluralName() {
        return null;
    }

    public Class<?> getSearchViaRepository() {
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
        return false;
    }

    public boolean isParseable() {
        return false;
    }

    public boolean isValueOrIsAggregated() {
        return false;
    }

    public void markDirty(final NakedObject object) {}

    public Object newInstance() {
        throw new NakedObjectException("Not able to create instance of " + getFullName()
                + "; newInstance() method should be overridden");
    }

    public Persistability persistability() {
        return Persistability.USER_PERSISTABLE;
    }

    public boolean queryByExample() {
        return false;
    }

    public void setupAction(final NakedObjectAction action) {
        this.action = action;
    }

    public void setupFields(final NakedObjectAssociation[] fields) {
        this.fields = fields;
    }

    public void setupSubclasses(final NakedObjectSpecification[] subclasses) {
        this.subclasses = subclasses;
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

    @Override
    public Identifier getIdentifier() {
        return null;
    }

    public boolean isCollectionOrIsAggregated() {
        return false;
    }

    public Object createObject(CreationMode creationMode) {
        throw new NotYetImplementedException();
    }

    public boolean isCollection() {
        return false;
    }

    public boolean isObject() {
        return !isCollection();
    }
    
    public boolean isImmutable() {
        return false;
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
    
    public Instance getInstance(NakedObject nakedObject) {
        return nakedObject;
    }

	public RuntimeContext getRuntimeContext() {
		return runtimeContext;
	}

}
// Copyright (c) Naked Objects Group Ltd.
