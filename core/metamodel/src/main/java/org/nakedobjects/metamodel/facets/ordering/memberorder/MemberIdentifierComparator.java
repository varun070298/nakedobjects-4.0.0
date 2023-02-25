package org.nakedobjects.metamodel.facets.ordering.memberorder;

import java.util.Comparator;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.specloader.internal.peer.NakedObjectMemberPeer;


/**
 * Compares {@link NakedObjectMemberPeer}) by {@link NakedObjectMemberPeer#getIdentifier()}
 * 
 */
public class MemberIdentifierComparator implements Comparator<NakedObjectMemberPeer> {

    @SuppressWarnings("unchecked")
    public int compare(final NakedObjectMemberPeer o1, final NakedObjectMemberPeer o2) {
        final Identifier identifier1 = o1.getIdentifier();
        final Identifier identifier2 = o2.getIdentifier();
        return identifier1.compareTo(identifier2);
    }

}
