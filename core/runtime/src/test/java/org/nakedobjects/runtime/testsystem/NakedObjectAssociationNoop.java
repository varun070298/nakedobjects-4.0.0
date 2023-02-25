package org.nakedobjects.runtime.testsystem;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.adapter.Instance;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;
import org.nakedobjects.metamodel.interactions.UsabilityContext;
import org.nakedobjects.metamodel.interactions.VisibilityContext;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.runtimecontext.spec.feature.NakedObjectAssociationAbstract;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.identifier.IdentifiedImpl;


public class NakedObjectAssociationNoop extends NakedObjectAssociationAbstract {

    public NakedObjectAssociationNoop(
    		final String name, 
    		final NakedObjectSpecification spec, 
    		final RuntimeContext runtimeContext) {
        super(name, spec, MemberType.ONE_TO_ONE_ASSOCIATION, new IdentifiedImpl(), runtimeContext);
    }

    public String debugData() {
        return "";
    }

    @Override
    public NakedObject get(final NakedObject fromObject) {
        return ((TestProxyNakedObject) fromObject).getField(this); // contentObject;
    }

    @Override
    public String getBusinessKeyName() {
        return null;
    }

    @Override
    public final boolean isDerived() {
        return false;
    }

    @Override
    public boolean isEmpty(final NakedObject adapter) {
        return false;
    }

    @Override
    public boolean hasChoices() {
        return false;
    }

    public NakedObject getDefault(final NakedObject nakedObject) {
        return null;
    }

    public void toDefault(final NakedObject target) {}

    @Override
    public Identifier getIdentifier() {
        return null;
    }

    public NakedObject[] getChoices(final NakedObject object) {
        return new NakedObject[] {};
    }

    public UsabilityContext<?> createUsableInteractionContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject target) {
        return null;
    }

    public VisibilityContext<?> createVisibleInteractionContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject targetNakedObject) {
        return null;
    }

    // /////////////////////////////////////////////////////////////
    // getInstance
    // /////////////////////////////////////////////////////////////
    
    public Instance getInstance(NakedObject nakedObject) {
        return null;
    }


}
// Copyright (c) Naked Objects Group Ltd.
