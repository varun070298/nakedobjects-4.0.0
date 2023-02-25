package org.nakedobjects.runtime.system.specpeer;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.consent.Allow;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.facets.FacetHolderImpl;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.Target;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;
import org.nakedobjects.metamodel.specloader.internal.peer.NakedObjectActionParamPeer;
import org.nakedobjects.metamodel.specloader.internal.peer.NakedObjectActionPeer;
import org.nakedobjects.runtime.testsystem.TestSpecification;



public final class DummyActionPeer extends FacetHolderImpl implements NakedObjectActionPeer {

    private final ExpectedSet expectedActions = new ExpectedSet();
    private String name;
    private NakedObjectSpecification[] paramterTypes;
    private NakedObject returnObject;
    private NakedObjectSpecification returnType;

    public void debugData(final DebugString debugString) {}

    public NakedObject execute(final NakedObject object, final NakedObject[] parameters) {
        expectedActions.addActual("execute " + getIdentifier() + " " + object);
        return returnObject;
    }

    public void expect(final String string) {
        expectedActions.addExpected(string);
    }

    public String getDescription() {
        return null;
    }

    public String getHelp() {
        return null;
    }

    public Identifier getIdentifier() {
        return Identifier.classIdentifier(name);
    }

    public String getName() {
        return name;
    }

    public NakedObjectSpecification getOnType() {
        return new TestSpecification();
    }

    public int getParameterCount() {
        return 3;
    }

    public Object[] getParameterDefaults(final NakedObject target) {
        return new Object[] { new String(), new Integer(123), null };
    }

    public String[] getParameterDescriptions() {
        return new String[] { "description one", "description two", "description three" };
    }

    public String[] getParameterNames() {
        return new String[] { "one", "two", "three" };
    }

    public Object[][] getParameterOptions(final NakedObject target) {
        return new Object[][] { { "test", "the", "options" }, null, null };
    }

    public NakedObjectSpecification[] getParameterTypes() {
        return paramterTypes;
    }

    public boolean[] getOptionalParameters() {
        return new boolean[3];
    }

    public NakedObjectSpecification getReturnType() {
        return returnType;
    }

    public Target getTarget() {
        return null;
    }

    public NakedObjectActionType getType() {
        return null;
    }

    public boolean isVisibleDeclaratively() {
        return true;
    }

    public boolean isVisibleForSession(final AuthenticationSession session) {
        return true;
    }

    public boolean isVisible(final NakedObject target) {
        return true;
    }

    public Consent isUsableDeclaratively() {
        return Allow.DEFAULT;
    }

    public Consent isUsableForSession(final AuthenticationSession session) {
        return Allow.DEFAULT;
    }

    public Consent isUsable(final NakedObject target) {
        return Allow.DEFAULT;
    }

    public boolean isOnInstance() {
        return true;
    }

    public Consent isParameterSetValidImperatively(final NakedObject object, final NakedObject[] parameters) {
        return null;
    }

    public void setupName(final String name) {
        this.name = name;
    }

    public void setUpParamterTypes(final NakedObjectSpecification[] paramterTypes) {
        this.paramterTypes = paramterTypes;
    }

    public void setupReturnObject(final NakedObject returnObject) {
        this.returnObject = returnObject;
    }

    public void setupReturnType(final NakedObjectSpecification returnType) {
        this.returnType = returnType;
    }

    public void verify() {
        expectedActions.verify();
    }

    public boolean[] canParametersWrap() {
        return new boolean[3];
    }

    public int[] getParameterMaxLengths() {
        return new int[3];
    }

    public int[] getParameterNoLines() {
        return new int[3];
    }

    public int[] getParameterTypicalLengths() {
        return new int[3];
    }

    public NakedObjectActionParamPeer[] getParameters() {
        return new NakedObjectActionParamPeer[] { new DummyActionParamPeer(), new DummyActionParamPeer(),
                new DummyActionParamPeer(), };
    }

}
// Copyright (c) Naked Objects Group Ltd.
