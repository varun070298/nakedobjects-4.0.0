package org.nakedobjects.progmodel.java5.reflect;

import org.nakedobjects.noa.reflect.NakedObjectAction;
import org.nakedobjects.noa.reflect.NakedObjectField;
import org.nakedobjects.nof.reflect.peer.ActionPeer;
import org.nakedobjects.nof.reflect.peer.OneToManyPeer;
import org.nakedobjects.nof.reflect.peer.OneToOnePeer;
import org.nakedobjects.nof.reflect.peer.ValuePeer;
import org.nakedobjects.nof.reflect.remote.spec.DummyAction;
import org.nakedobjects.nof.reflect.remote.spec.DummyOneToManyAssociation;
import org.nakedobjects.nof.reflect.remote.spec.DummyOneToOneAssociation;
import org.nakedobjects.nof.reflect.remote.spec.DummyValueAssociation;
import org.nakedobjects.nof.reflect.spec.ReflectionPeerBuilder;



public class DummyBuilder extends ReflectionPeerBuilder {
    public NakedObjectAction createAction(final ActionPeer actionPeer) {
        return new DummyAction(actionPeer);
    }

    public NakedObjectField createField(final OneToManyPeer fieldPeer) {
        return new DummyOneToManyAssociation(fieldPeer);
    }

    public NakedObjectField createField(final OneToOnePeer fieldPeer) {
        return new DummyOneToOneAssociation(fieldPeer);
    }
    
    public NakedObjectField createField(ValuePeer fieldPeer) {
        return new DummyValueAssociation(fieldPeer);
    }
}
// Copyright (c) Naked Objects Group Ltd.
