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
import org.nakedobjects.metamodel.testspec.TestProxySpecification;


@SuppressWarnings("unchecked")
public class DummyOneToManyPeer extends FacetHolderImpl implements NakedObjectAssociationPeer {

    Vector actions = new Vector();
    private final ExpectedSet expectedActions = new ExpectedSet();
    public NakedObject getCollection;
    public boolean isEmpty;
    public String label;
    String name;
    private final TestProxySpecification specification;

    public DummyOneToManyPeer(final TestProxySpecification specification) {
        this.specification = specification;
    }

	public void addAssociation(final NakedObject inObject, final NakedObject associate) {
        actions.addElement("add " + inObject);
        actions.addElement("add " + associate);
    }

    public void assertAction(final int index, final String expected) {
        Assert.assertEquals(expected, actions.elementAt(index));
    }

    public void assertActions(final int noOfActions) {
        if (noOfActions != actions.size()) {
            Assert.fail("Expected " + noOfActions + ", but got " + actions.size());
        }
    }

    public void debugData(final DebugString debugString) {}

    public void expect(final String string) {
        expectedActions.addExpected(string);
    }

    public NakedObject getAssociations(final NakedObject inObject) {
        actions.addElement("get " + inObject);
        return getCollection;
    }

    public String getBusinessKeyName() {
        return null;
    }

    public NakedObject getDefault(final NakedObject target) {
        return null;
    }

    public String getDescription() {
        return null;
    }

    public String getHelp() {
        return null;
    }

    public Identifier getIdentifier() {
        return Identifier.classIdentifier("SomeClassName");
    }

    public String getName() {
        return null;
    }

    public Object[] getOptions(final NakedObject target) {
        return null;
    }

    public NakedObjectSpecification getSpecification() {
        return specification;
    }

    public void initAssociation(final NakedObject inObject, final NakedObject associate) {}

    public void initOneToManyAssociation(final NakedObject inObject, final NakedObject[] instances) {}

    public Consent isAddValid(final NakedObject container, final NakedObject element) {
        return null;
    }

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

    public Consent isRemoveValid(final NakedObject container, final NakedObject element) {
        return null;
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

    public boolean isVisibleDeclaratively() {
        return false;
    }

    public boolean isVisibleForSession(final AuthenticationSession session) {
        return false;
    }

    public boolean isVisible(final NakedObject target) {
        return true;
    }

    public void removeAllAssociations(final NakedObject inObject) {
        actions.addElement("removeall " + inObject);
    }

    public void removeAssociation(final NakedObject inObject, final NakedObject associate) {
        actions.addElement("remove " + inObject);
        actions.addElement("remove " + associate);
    }

    public void verify() {
        expectedActions.verify();
    }

    public boolean isOneToMany() {
        return true;
    }

    public boolean isOneToOne() {
        return false;
    }

}
// Copyright (c) Naked Objects Group Ltd.
