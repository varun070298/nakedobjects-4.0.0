package org.nakedobjects.metamodel.specloader.internal.peer;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.specloader.ReflectiveActionException;


public interface NakedObjectActionPeer extends NakedObjectMemberPeer {

    // ////////////// execute, consent /////////////////

    NakedObject execute(NakedObject target, NakedObject[] parameters) throws ReflectiveActionException;

    // ////////////// Parameters /////////////////

    NakedObjectActionParamPeer[] getParameters();

}
// Copyright (c) Naked Objects Group Ltd.
