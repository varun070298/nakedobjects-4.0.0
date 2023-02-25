package org.nakedobjects.runtime.system.specpeer;

import java.util.Vector;

import junit.framework.Assert;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.consent.Allow;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.facets.FacetHolderImpl;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.specloader.internal.peer.NakedObjectAssociationPeer;


@SuppressWarnings("unchecked")
public class DummyOneToOnePeer extends FacetHolderImpl implements NakedObjectAssociationPeer {
	
    private final ExpectedSet expectedActions = new ExpectedSet();
    Vector actions = new Vector();
    public NakedObject getObject;
    public boolean isEmpty;
    public boolean isVisible;

	public void clearAssociation(final NakedObject inObject, final NakedObject associate) {
        actions.addElement("clear " + inObject);
        actions.addElement("clear " + associate);
    }

    public void expect(final String string) {
        expectedActions.addExpected(string);
    }

    public NakedObject getAssociation(final NakedObject inObject) {
        actions.addElement("get " + inObject);
        return getObject;
    }

    public NakedObject getDefault(final NakedObject target) {
        return null;
    }

    public Object[] getOptions(final NakedObject target) {
        return null;
    }

    public Identifier getIdentifier() {
        return null;
    }

    public NakedObjectSpecification getSpecification() {
        return null;
    }

    public void initAssociation(final NakedObject inObject, final NakedObject associate) {}

    public boolean isPersisted() {
        return true;
    }

    public boolean isEmpty(final NakedObject inObject) {
        actions.addElement("empty " + inObject);
        return isEmpty;
    }

    public boolean isMandatory() {
        return false;
    }

    public void setAssociation(final NakedObject inObject, final NakedObject associate) {
        actions.addElement("associate " + inObject);
        actions.addElement("associate " + associate);
    }

    public void verify() {
        expectedActions.verify();
    }

    public void assertAction(final int index, final String expected) {
        Assert.assertEquals(expected, actions.elementAt(index));
    }

    public Consent isAssociationValid(final NakedObject inObject, final NakedObject value) {
        return null;
    }

    public String getDescription() {
        return null;
    }

    public String getName() {
        return null;
    }

    public boolean isVisibleDeclaratively() {
        return true;
    }

    public boolean isVisibleForSession(final AuthenticationSession session) {
        return true;
    }

    public boolean isVisible(final NakedObject target) {
        return isVisible;
    }

    public void setupVisible(final boolean isVisible) {
        this.isVisible = isVisible;
    }

    public Consent isUsableDeclaratively() {
        return Allow.DEFAULT;
    }

    public Consent isUsableForSession(final AuthenticationSession session) {
        return Allow.DEFAULT;
    }

    public Consent isUsable(final NakedObject target) {
        return null;
    }

    public boolean isOptionEnabled() {
        return false;
    }

    public String getHelp() {
        return null;
    }

    public void debugData(final DebugString debugString) {}

    public String getBusinessKeyName() {
        return null;
    }

    public boolean isOneToMany() {
        return false;
    }

    public boolean isOneToOne() {
        return true;
    }

}
// Copyright (c) Naked Objects Group Ltd.
