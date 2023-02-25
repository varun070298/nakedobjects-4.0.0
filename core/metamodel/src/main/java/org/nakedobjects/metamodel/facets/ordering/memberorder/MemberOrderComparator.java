package org.nakedobjects.metamodel.facets.ordering.memberorder;

import java.util.Comparator;
import java.util.StringTokenizer;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.ordering.OrderSet;
import org.nakedobjects.metamodel.facets.ordering.OrderSetGroupNameComparator;
import org.nakedobjects.metamodel.specloader.internal.peer.NakedObjectMemberPeer;


/**
 * Compares by {@link MemberOrderFacet} obtained from each {@link NakedObjectMemberPeer}).
 * 
 * <p>
 * Will also compare {@link OrderSet}s; these are put after any {@link NakedObjectMemberPeer}s. If there is
 * more than one OrderSet then these are compared by an {@link OrderSetGroupNameComparator}.
 * 
 * <p>
 * If there is no annotation on either member, then will compare the members by name instead.
 * 
 * <p>
 * Can specify if requires that members are in the same (group) name.
 */
public class MemberOrderComparator implements Comparator {

    private final boolean ensureInSameGroup;

    /**
     * 
     * @param ensureGroupIsSame
     */
    public MemberOrderComparator(final boolean ensureGroupIsSame) {
        this.ensureInSameGroup = ensureGroupIsSame;
    }

    private final Comparator fallbackComparator = new MemberIdentifierComparator();
    private final OrderSetGroupNameComparator orderSetComparator = new OrderSetGroupNameComparator(true);

    public int compare(final Object o1, final Object o2) {
        if (o1 instanceof NakedObjectMemberPeer && o2 instanceof NakedObjectMemberPeer) {
            return compare((NakedObjectMemberPeer) o1, (NakedObjectMemberPeer) o2);
        }
        if (o1 instanceof OrderSet && o2 instanceof OrderSet) {
            return orderSetComparator.compare(o1, o2);
        }
        if (o1 instanceof NakedObjectMemberPeer && o2 instanceof OrderSet) {
            return -1; // members before OrderSets.
        }
        if (o1 instanceof OrderSet && o2 instanceof NakedObjectMemberPeer) {
            return +1; // members before OrderSets.
        }
        throw new IllegalArgumentException("can only compare MemberPeers and OrderSets");
    }

    public int compare(final NakedObjectMemberPeer o1, final NakedObjectMemberPeer o2) {
        final MemberOrderFacet m1 = getMemberOrder(o1);
        final MemberOrderFacet m2 = getMemberOrder(o2);

        if (m1 == null && m2 == null) {
            return fallbackComparator.compare(o1, o2);
        }

        if (m1 == null && m2 != null) {
            return +1; // annotated before non-annotated
        }
        if (m1 != null && m2 == null) {
            return -1; // annotated before non-annotated
        }

        if (ensureInSameGroup && !m1.name().equals(m2.name())) {
            throw new IllegalArgumentException("Not in same group");
        }

        final StringTokenizer tokens1 = new StringTokenizer(m1.sequence(), ".", false);
        final String[] components1 = new String[tokens1.countTokens()];
        for (int i = 0; tokens1.hasMoreTokens(); i++) {
            components1[i] = tokens1.nextToken();
        }
        final StringTokenizer tokens2 = new StringTokenizer(m2.sequence(), ".", false);
        final String[] components2 = new String[tokens2.countTokens()];
        for (int i = 0; tokens2.hasMoreTokens(); i++) {
            components2[i] = tokens2.nextToken();
        }

        final int length1 = components1.length;
        final int length2 = components2.length;

        // shouldn't happen but just in case.
        if (length1 == 0 && length2 == 0) {
            return fallbackComparator.compare(o1, o2);
        }

        // continue to loop until we run out of components.
        int n = 0;
        while (true) {
            final int length = n + 1;
            // check if run out of components in either side
            if (length1 < length && length2 >= length) {
                return -1; // o1 before o2
            }
            if (length2 < length && length1 >= length) {
                return +1; // o2 before o1
            }
            if (length1 < length && length2 < length) {
                // run out of components
                return fallbackComparator.compare(o1, o2);
            }
            // we have this component on each side

            int componentCompare = 0;
            try {
                final Integer c1 = Integer.valueOf(components1[n]);
                final Integer c2 = Integer.valueOf(components2[n]);
                componentCompare = c1.compareTo(c2);
            } catch (final NumberFormatException nfe) {
                // not integers compare as strings}
                componentCompare = components1[n].compareTo(components2[n]);
            }

            if (componentCompare != 0) {
                return componentCompare;
            }
            // this component is the same; lets look at the next
            n++;
        }
    }

    private MemberOrderFacet getMemberOrder(final FacetHolder facetHolder) {
        return facetHolder.getFacet(MemberOrderFacet.class);
    }

}
