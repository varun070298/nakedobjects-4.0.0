package org.nakedobjects.metamodel.runtimecontext.spec;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.UnexpectedCallException;
import org.nakedobjects.metamodel.commons.names.NameConvertorUtils;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.metamodel.facetdecorator.FacetDecoratorSet;
import org.nakedobjects.metamodel.interactions.InteractionContext;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.Persistability;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;


/**
 * A simple implementation used for objects that have no members (fields or actions). Used for primitives and
 * as a fall-back if no specification can be otherwise generated.
 */
public class NakedObjectSpecificationNoMember extends IntrospectableSpecificationAbstract {
    private final String name;

    public NakedObjectSpecificationNoMember(
    		final String className, final RuntimeContext runtimeContext) {
    	super(runtimeContext);
        this.fullName = className;
        this.name = NameConvertorUtils.simpleName(className.substring(className.lastIndexOf('.') + 1));
        identifier = Identifier.classIdentifier(className);

        throw new UnexpectedCallException(className);

    }

    public void markAsService() {}

    public void introspect(final FacetDecoratorSet decorator) {
        fields = new NakedObjectAssociation[0];
        superClassSpecification = null;
        
        setIntrospected(true);
    }

    public String getTitle(final NakedObject naked) {
        return "no title";
    }

    public String getShortName() {
        return name;
    }

    public String getSingularName() {
        return name;
    }

    public String getPluralName() {
        return name;
    }

    public String getDescription() {
        return name;
    }

    public NakedObjectAssociation getAssociation(final String name) {
        return null;
    }

    @Override
    public NakedObjectAction[] getObjectActions(final NakedObjectActionType... type) {
        return new NakedObjectAction[0];
    }

    public NakedObjectAction getObjectAction(
            final NakedObjectActionType type,
            final String id,
            final NakedObjectSpecification[] parameters) {
        return null;
    }

    public NakedObjectAction getObjectAction(final NakedObjectActionType type, final String id) {
        return null;
    }

    @Override
    public Consent isValid(final NakedObject transientObject) {
        return Veto.DEFAULT;
    }

    @Override
    public Persistability persistability() {
        return Persistability.TRANSIENT;
    }

    public void debugData(final DebugString debug) {
        debug.append("There are no reflective actions");
    }

    public String debugTitle() {
        return "NO Member Specification";
    }

    /**
     * Does nothing, but should never be called.
     */
    public InteractionContext createPersistInteractionContext(
            final AuthenticationSession session,
            final boolean programmatic,
            final NakedObject targetNakedObject) {
        return null;
    }

}
// Copyright (c) Naked Objects Group Ltd.
