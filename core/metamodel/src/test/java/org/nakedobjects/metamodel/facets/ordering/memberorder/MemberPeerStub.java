package org.nakedobjects.metamodel.facets.ordering.memberorder;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.consent.Allow;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.facets.NamedAndDescribedFacetHolderImpl;
import org.nakedobjects.metamodel.specloader.internal.peer.NakedObjectMemberPeer;


final class MemberPeerStub extends NamedAndDescribedFacetHolderImpl implements NakedObjectMemberPeer {

    public MemberPeerStub(final String name) {
        super(name);
    }

    public void debugData(final DebugString debugString) {}

    public String getHelp() {
        return null;
    }

    public Identifier getIdentifier() {
        return Identifier.classIdentifier(MemberPeerStub.this.getName());
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
        return false;
    }

    @Override
    public String toString() {
        return getName();
    }

}
