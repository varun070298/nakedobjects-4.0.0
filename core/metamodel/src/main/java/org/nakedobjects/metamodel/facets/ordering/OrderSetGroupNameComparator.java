package org.nakedobjects.metamodel.facets.ordering;

import java.util.Comparator;


/**
 * Compares by (simple) group name of each {@link OrderSet}.
 * 
 * <p>
 * Note that it only makes sense to use this comparator for {@link OrderSet}s that are known to have the same
 * parent {@link OrderSet}s.
 */
public class OrderSetGroupNameComparator implements Comparator {

    public OrderSetGroupNameComparator(final boolean ensureInSameGroupPath) {
        this.ensureInSameGroupPath = ensureInSameGroupPath;
    }

    private final boolean ensureInSameGroupPath;

    public int compare(final Object o1, final Object o2) {
        return compare((OrderSet) o1, ((OrderSet) o2));
    }

    public int compare(final OrderSet o1, final OrderSet o2) {
        if (ensureInSameGroupPath && !o1.getGroupPath().equals(o2.getGroupPath())) {
            throw new IllegalArgumentException("OrderSets being compared do not have the same group path");
        }

        final String groupName1 = o1.getGroupName();
        final String groupName2 = o2.getGroupName();

        return groupName1.compareTo(groupName2);
    }
}
