package org.nakedobjects.metamodel.facets.ordering;

import java.util.StringTokenizer;

import org.nakedobjects.metamodel.commons.names.NameConvertorUtils;
import org.nakedobjects.metamodel.specloader.internal.peer.NakedObjectMemberPeer;


public class SimpleOrderSet extends OrderSet {
    public static SimpleOrderSet createOrderSet(final String order, final NakedObjectMemberPeer[] members) {
        SimpleOrderSet set = new SimpleOrderSet(members);

        final StringTokenizer st = new StringTokenizer(order, ",");
        while (st.hasMoreTokens()) {
            String element = st.nextToken().trim();

            boolean ends;
            if (ends = element.endsWith(")")) {
                element = element.substring(0, element.length() - 1).trim();
            }

            if (element.startsWith("(")) {
                final int colon = element.indexOf(':');
                final String groupName = element.substring(1, colon).trim();
                element = element.substring(colon + 1).trim();
                set = set.createSubOrderSet(groupName, element);
            } else {
                set.add(element);
            }

            if (ends) {
                set = set.parent;
            }
        }
        set.addAnyRemainingMember();
        return set;
    }

    private final SimpleOrderSet parent;
    private final NakedObjectMemberPeer[] members;

    private SimpleOrderSet(final NakedObjectMemberPeer[] members) {
        super("");
        this.members = members;
        parent = null;
    }

    private SimpleOrderSet(
            final SimpleOrderSet set,
            final String groupName,
            final String name,
            final NakedObjectMemberPeer[] members) {
        super(groupName);
        parent = set;
        parent.addElement(this);
        this.members = members;
        add(name);
    }

    private void add(final String name) {
        final NakedObjectMemberPeer memberWithName = getMemberWithName(name);
        if (memberWithName != null) {
            addElement(memberWithName);
        }
    }

    private void addAnyRemainingMember() {
        for (int i = 0; i < members.length; i++) {
            if (members[i] != null) {
                addElement(members[i]);
            }
        }

    }

    private SimpleOrderSet createSubOrderSet(final String groupName, final String memberName) {
        return new SimpleOrderSet(this, groupName, memberName, members);
    }

    private NakedObjectMemberPeer getMemberWithName(final String name) {
        final String searchName = NameConvertorUtils.simpleName(name);
        for (int i = 0; i < members.length; i++) {
            final NakedObjectMemberPeer member = members[i];
            if (member != null) {
                final String testName = NameConvertorUtils.simpleName(member.getIdentifier().getMemberName());
                if (testName.equals(searchName)) {
                    members[i] = null;
                    return member;
                }
            }
        }
        return null;
    }

}

// Copyright (c) Naked Objects Group Ltd.
