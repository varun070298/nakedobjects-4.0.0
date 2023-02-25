package org.nakedobjects.metamodel.specloader.internal.peer;

import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.spec.identifier.Identified;


/**
 * Details about action and field members gained via reflection.
 * 
 * @see org.nakedobjects.metamodel.specloader.internal.peer.NakedObjectActionPeer
 * @see org.nakedobjects.metamodel.specloader.internal.peer.NakedObjectAssociationPeer
 */
public interface NakedObjectMemberPeer extends Identified {

    void debugData(DebugString debugString);

}
// Copyright (c) Naked Objects Group Ltd.
