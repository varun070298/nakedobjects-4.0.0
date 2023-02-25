package org.nakedobjects.runtime.testsystem;

import java.util.Hashtable;

import junit.framework.Assert;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.adapter.Instance;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.exceptions.UnexpectedCallException;
import org.nakedobjects.metamodel.consent.Allow;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.ConsentAbstract;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;
import org.nakedobjects.metamodel.consent.InteractionResult;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolderNoop;
import org.nakedobjects.metamodel.interactions.InteractionContext;
import org.nakedobjects.metamodel.interactions.PropertyAccessContext;
import org.nakedobjects.metamodel.interactions.UsabilityContext;
import org.nakedobjects.metamodel.interactions.ValidityContext;
import org.nakedobjects.metamodel.interactions.VisibilityContext;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.runtimecontext.noruntime.RuntimeContextNoRuntime;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;
import org.nakedobjects.metamodel.testspec.TestProxySpecification;


public class TestProxyAssociation extends FacetHolderNoop implements OneToOneAssociation {

    private final String name;
    private final Hashtable<NakedObject, Object> values = new Hashtable<NakedObject, Object>();
    private final TestProxySpecification spec;
    
    private boolean isVisible = true;
    
    @SuppressWarnings("unused")
	private boolean isUsable = true;
    
    private NakedObject unuseableForObject;
	private final RuntimeContext runtimeContext;

    public TestProxyAssociation(final String name, final TestProxySpecification valueFieldSpec) {
        this.name = name;
        this.spec = valueFieldSpec;
		runtimeContext = new RuntimeContextNoRuntime();
    }

    // TODO: this is inconsistent with #get that casts the value to a NakedObject
    public void clearAssociation(final NakedObject inObject) {
        values.put(inObject, "NULL");
    }

    public String debugData() {
        return "";
    }

    public NakedObject get(final NakedObject inObject) {
        return (NakedObject) values.get(inObject);
    }

    public String getBusinessKeyName() {
        return null;
    }

    public String getDescription() {
        return "no description";
    }

    @Override
    public Facet getFacet(final Class cls) {
        throw new UnexpectedCallException();
    }

    public String getHelp() {
        return "no help";
    }

    public String getId() {
        return name;
    }

    public String getName() {
        return name;
    }

    public NakedObject[] getChoices(final NakedObject target) {
        return null;
    }

    public NakedObjectSpecification getSpecification() {
        return spec;
    }

    public void initAssociation(final NakedObject inObject, final NakedObject associate) {
        setAssociation(inObject, associate);
    }

    public Consent isAssociationValid(final NakedObject inObject, final NakedObject value) {
        throw new UnexpectedCallException();
    }

    public boolean isDerived() {
        return false;
    }

    public boolean isEmpty(final NakedObject inObject) {
        throw new UnexpectedCallException();
    }

    public boolean isMandatory() {
        throw new UnexpectedCallException();
    }

    public boolean hasChoices() {
        return false;
    }

    public Consent isUsable(final AuthenticationSession session, final NakedObject target) {
        return ConsentAbstract.allowIf(target != unuseableForObject);
    }

    public Consent isVisible(final AuthenticationSession session, final NakedObject target) {
        return ConsentAbstract.allowIf(isVisible);
    }

    public void setAssociation(final NakedObject inObject, final NakedObject associate) {
        values.put(inObject, associate);
    }

    public void set(NakedObject owner, NakedObject newValue) {
        setAssociation(owner, newValue);
    }

    public NakedObject getDefault(final NakedObject nakedObject) {
        return null;
    }

    public void toDefault(final NakedObject target) {}

    @Override
    public Identifier getIdentifier() {
        return null;
    }

    public Consent isUsable(final InteractionContext ic) {
        return Allow.DEFAULT;
    }

    public boolean isVisible(final InteractionContext ic) {
        return true;
    }

    public void assertField(final Object inObject, final Object value) {
        final NakedObject field = (NakedObject) values.get(inObject);
        Assert.assertEquals(value, field.getObject());
    }

    public void assertFieldEmpty(final NakedObject object) {
        final Object field = values.get(object);
        Assert.assertEquals("NULL", field);

    }

    public void setUpIsVisible(final boolean isVisible) {
        this.isVisible = isVisible;
    }

    public void setUpIsUsable(final boolean isUsable) {
        this.isUsable = isUsable;
    }

    public void setUpIsUnusableFor(final NakedObject object) {
        this.unuseableForObject = object;
    }

    public VisibilityContext<?> createVisibleInteractionContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject targetNakedObject) {
        return null;
    }

    public UsabilityContext<?> createUsableInteractionContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject target) {
        return null;
    }

    public ValidityContext<?> createValidateInteractionContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject owningNakedObject,
            final NakedObject newValue) {
        return null;
    }

    public InteractionResult isAssociationValidResult(final NakedObject targetObject, final NakedObject newValue) {
        return null;
    }

    public InteractionResult isUsableResult(final AuthenticationSession session, final NakedObject target) {
        return null;
    }

    public InteractionResult isVisibleResult(final AuthenticationSession session, final NakedObject target) {
        return null;
    }

    public PropertyAccessContext createAccessInteractionContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod interactionMethod,
            final NakedObject targetNakedObject) {
        return null;
    }


    // /////////////////////////////////////////////////////////////
    // isAction, isAssociation
    // /////////////////////////////////////////////////////////////

    public boolean isAction() {
        return false;
    }

    public boolean isAssociation() {
        return true;
    }

    public boolean isOneToManyAssociation() {
        return false;
    }
    public boolean isOneToOneAssociation() {
        return true;
    }

    // /////////////////////////////////////////////////////////////
    // getInstance
    // /////////////////////////////////////////////////////////////
    
    public Instance getInstance(NakedObject nakedObject) {
        OneToOneAssociation specification = this;
        return nakedObject.getInstance(specification);
    }

    
    // /////////////////////////////////////////////////////////////
    // RuntimeContext
    // /////////////////////////////////////////////////////////////

	public RuntimeContext getRuntimeContext() {
		return runtimeContext;
	}

}
// Copyright (c) Naked Objects Group Ltd.
